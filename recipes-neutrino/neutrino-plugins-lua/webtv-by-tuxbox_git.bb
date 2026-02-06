include neutrino-lua-plugins.inc

SUMMARY = "WebTV content from tuxbox"
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "tuxbox"
SECTION = "optional"
HOMEPAGE = "https://github.com/tuxbox-neutrino/plugin-scripts-lua.git"

SRC_NAME = "webtv"
MIGIT_SUBDIR = "plugins/${SRC_NAME}"
MIGIT_REPO_NAME = "${SRC_NAME}"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PR = "r1"

do_compile[noexec] = "1"

do_install () {
	suffix="by-tuxbox"
	prefix="tuxbox"

	normalize_name() {
		local name="$1"

		case "$name" in
			${prefix}-*) name="${name#${prefix}-}" ;;
		esac

		case "$name" in
			*-by-tuxbox) name="${name%-by-tuxbox}" ;;
			*-by-ni) name="${name%-by-ni}" ;;
		esac

		echo "$name"
	}

	install -d ${D}${N_WEBTV_DIR}

	scripts_map=""
	for s in ${S}/*.lua; do
		[ -e "$s" ] || continue
		raw=$(basename "$s" .lua)
		name=$(normalize_name "$raw")
		[ -n "$name" ] || name="$raw"
		scripts_map="$scripts_map ${raw}:${name}-${suffix}"
	done

	for f in ${S}/*.lua ${S}/*.xml; do
		[ -e "$f" ] || continue
		base=$(basename "$f")
		name="${base%.*}"
		ext="${base##*.}"
		norm=$(normalize_name "$name")
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
			install -m 644 "$tmp" "${D}${N_WEBTV_DIR}/${new}"
		else
			install -m 644 "$f" "${D}${N_WEBTV_DIR}/${new}"
		fi
	done
}

FILES:${PN} += " \
	${N_WEBTV_DIR} \
"
