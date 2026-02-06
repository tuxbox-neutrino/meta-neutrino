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
PR = "r0"

do_compile[noexec] = "1"

do_install () {
	suffix="by-tuxbox"

	install -d ${D}${N_WEBTV_DIR}

	scripts=""
	for s in ${S}/*.lua; do
		[ -e "$s" ] || continue
		scripts="$scripts $(basename "$s" .lua)"
	done

	for f in ${S}/*.lua ${S}/*.xml; do
		[ -e "$f" ] || continue
		base=$(basename "$f")
		name="${base%.*}"
		ext="${base##*.}"
		new="${name}-${suffix}.${ext}"

		if [ "$ext" = "xml" ]; then
			tmp="${WORKDIR}/${new}"
			cp "$f" "$tmp"
			for s in $scripts; do
				sed -i "s/script=\"${s}\\.lua\"/script=\"${s}-${suffix}\\.lua\"/g" "$tmp"
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
