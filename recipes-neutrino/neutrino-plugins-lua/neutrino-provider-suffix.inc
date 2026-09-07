PROVIDER_SUFFIX ?= "${@('by-' + (d.getVar('PN') or '').split('-by-')[-1]) if '-by-' in (d.getVar('PN') or '') else ''}"
PROVIDER_PREFIX ?= "${@((d.getVar('PN') or '').split('-by-')[-1]) if '-by-' in (d.getVar('PN') or '') else ''}"

PROVIDER_SKIP_BASENAMES ?= ""

# Set by the provider type include (webtv, webradio).
PROVIDER_SCRIPT_GLOB ??= ""
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

	scripts_map=""
	for s in ${S}/${PROVIDER_SCRIPT_GLOB}; do
		[ -f "$s" ] || continue
		raw=$(basename "$s" .lua)
		if provider_should_skip "$raw"; then
			continue
		fi
		name=$(provider_normalize_name "$raw")
		[ -n "$name" ] || name="$raw"
		scripts_map="$scripts_map ${raw}:${name}-${suffix}"
	done

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
		new="${norm}-${suffix}.${ext}"

		if [ "$ext" = "xml" ]; then
			tmp="${WORKDIR}/${new}"
			cp "$f" "$tmp"
			for pair in $scripts_map; do
				raw="${pair%%:*}"
				ren="${pair##*:}"
				sed -i "s/script=\"${raw}\\.lua\"/script=\"${ren}\\.lua\"/g" "$tmp"
			done
			install -m 644 "$tmp" "${D}${PROVIDER_TARGET_DIR}/${new}"
		else
			install -m 644 "$f" "${D}${PROVIDER_TARGET_DIR}/${new}"
		fi
	done
}
