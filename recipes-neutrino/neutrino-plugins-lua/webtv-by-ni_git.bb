include neutrino-lua-plugins.inc

SUMMARY = "WebTV content from ni"
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "ni"
SECTION = "optional"
HOMEPAGE = "https://github.com/neutrino-images/ni-neutrino-plugins"

SRC_NAME = "webtv"
MIGIT_SUBDIR = "scripts-lua/plugins/${SRC_NAME}"
MIGIT_REPO_NAME = "${SRC_NAME}"

SRC_URI = " \
	git://github.com/neutrino-images/ni-neutrino-plugins.git;protocol=https;branch=master \
"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PR = "r0"

do_compile[noexec] = "1"

do_install () {
	suffix="by-ni"

	install -d ${D}${N_WEBTV_DIR}

	scripts=""
	for s in ${S}/*.lua; do
		[ -e "$s" ] || continue
		name=$(basename "$s" .lua)
		if [ "$name" = "filmon" ]; then
			continue
		fi
		scripts="$scripts $name"
	done

	for f in ${S}/*.lua ${S}/*.xml; do
		[ -e "$f" ] || continue
		base=$(basename "$f")
		name="${base%.*}"
		ext="${base##*.}"

		if [ "$name" = "filmon" ]; then
			continue
		fi

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
