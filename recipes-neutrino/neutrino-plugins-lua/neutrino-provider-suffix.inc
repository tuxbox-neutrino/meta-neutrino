PROVIDER_SUFFIX ?= "${@('by-' + (d.getVar('PN') or '').split('-by-')[-1]) if '-by-' in (d.getVar('PN') or '') else ''}"
PROVIDER_PREFIX ?= "${@((d.getVar('PN') or '').split('-by-')[-1]) if '-by-' in (d.getVar('PN') or '') else ''}"

# Written into every shipped XML as provider="...", so Neutrino can tell
# the variants apart without having to parse it out of the file name.
PROVIDER_ID ?= "${PROVIDER_PREFIX}"

PROVIDER_SKIP_BASENAMES ?= ""

# Overridable by the provider type include (webtv, webradio). Scripts are
# the default because any list may reference one, and step 3 below fails
# the build for a reference whose script was never installed.
PROVIDER_SCRIPT_GLOB ??= "*.lua"
PROVIDER_XML_GLOB ??= "*.xml"

provider_normalize_name() {
	local name="$1"

	if [ -n "${PROVIDER_PREFIX}" ]; then
		case "$name" in
			${PROVIDER_PREFIX}-*) name="${name#${PROVIDER_PREFIX}-}" ;;
		esac
	fi

	# Strip any existing provider suffix to keep renaming idempotent.
	case "$name" in
		*-by-*) name="${name%-by-*}" ;;
	esac

	echo "$name"
}

provider_should_skip() {
	local name="$1"

	for skip in ${PROVIDER_SKIP_BASENAMES}; do
		if [ "$name" = "$skip" ]; then
			return 0
		fi
	done

	return 1
}

do_install () {
	suffix="${PROVIDER_SUFFIX}"
	if [ -z "$suffix" ]; then
		bbfatal "provider suffix missing (PN should include -by-)"
	fi

	install -d ${D}${PROVIDER_TARGET_DIR}

	for f in ${S}/${PROVIDER_SCRIPT_GLOB} ${S}/${PROVIDER_XML_GLOB}; do
		[ -f "$f" ] || continue
		base=$(basename "$f")
		name="${base%.*}"
		ext="${base##*.}"

		if provider_should_skip "$name"; then
			continue
		fi

		norm=$(provider_normalize_name "$name")
		[ -n "$norm" ] || norm="$name"
		install -m 644 "$f" "${D}${PROVIDER_TARGET_DIR}/${norm}-${suffix}.${ext}"
	done
}

# Runs after the complete do_install, including recipe appends that add
# layer files (streamlink-by-*.lua, override XMLs), so every shipped file
# is covered. Renaming a script breaks everything that names it: XML
# script="x.lua" as well as Lua dofile("/x.lua") of a shared helper.
do_install[postfuncs] += "provider_finalize"

provider_finalize() {
	suffix="${PROVIDER_SUFFIX}"
	dir="${D}${PROVIDER_TARGET_DIR}"
	[ -d "$dir" ] || return 0

	files=$(find "$dir" -maxdepth 1 -type f \( -name '*.lua' -o -name '*.xml' \))
	[ -n "$files" ] || return 0

	# 1. Point every reference at the renamed script. A reference may use
	#    the upstream name or the prefixed form provider_normalize_name
	#    strips; the renamed form itself no longer matches, so this is
	#    idempotent.
	for s in "$dir"/*-${suffix}.lua; do
		[ -e "$s" ] || continue
		ren=$(basename "$s" .lua)
		raw="${ren%-${suffix}}"
		for old in "$raw" "${PROVIDER_PREFIX}-${raw}"; do
			esc=$(printf '%s' "$old" | sed 's#[][(){}.*+?^$|\\/#-]#\\&#g')
			sed -i -E "s#([\"'/])${esc}\.lua([\"'])#\1${ren}.lua\2#g" $files
		done
	done

	# 2. State the variant in the file itself. The rename above already
	#    carries it, but only as a file-name convention both sides have to
	#    agree on; the attribute says it outright.
	#
	#    A regex cannot parse XML, and upstream content changes under this
	#    recipe without a pin bump (AUTOREV), so this only rewrites a root
	#    tag it can read with certainty: it walks to the first ">" outside
	#    quotes and leaves self-closing tags (no children, no bouquet) and
	#    tags spanning a line break alone. Whatever stays untouched simply
	#    keeps the file-name fallback - never a broken document.
	if [ -n "${PROVIDER_ID}" ]; then
		for x in "$dir"/*.xml; do
			[ -e "$x" ] || continue
			awk -v id="${PROVIDER_ID}" '
				BEGIN { done = 0 }
				!done && match($0, /<(webtvs|webradios)[ \t>/]/) {
					start = RSTART
					inq = 0
					end = 0
					for (i = start; i <= length($0); i++) {
						c = substr($0, i, 1)
						if (c == "\"") inq = !inq
						else if (c == ">" && !inq) { end = i; break }
					}
					done = 1
					if (end > 0 && substr($0, end - 1, 1) != "/") {
						tag = substr($0, start, end - start + 1)
						gsub(/[ \t]provider="[^"]*"/, "", tag)
						tag = substr(tag, 1, length(tag) - 1) " provider=\"" id "\">"
						$0 = substr($0, 1, start - 1) tag substr($0, end + 1)
					}
				}
				{ print }
			' "$x" > "$x.tmp" && mv "$x.tmp" "$x"
		done
	fi

	# 3. Every *.lua the shipped files mention must exist next to them.
	missing=""
	for ref in $(grep -ohE "[\"'/][A-Za-z0-9_.-]+\.lua[\"']" $files | sed -E "s#^[\"'/]##; s#[\"']\$##" | sort -u); do
		[ -e "$dir/$ref" ] || missing="$missing $ref"
	done
	if [ -n "$missing" ]; then
		bbfatal "provider ${suffix}: shipped files reference missing scripts:${missing}"
	fi
}
