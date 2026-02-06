include neutrino-lua-plugins.inc

SUMMARY = "Webradio content from ni"
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "ni"
SECTION = "optional"
HOMEPAGE = "https://github.com/neutrino-images/ni-neutrino-plugins"

SRC_NAME = "webradio"
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

	install -d ${D}${N_WEBRADIO_DIR}

	for f in ${S}/*.xml; do
		[ -e "$f" ] || continue
		base=$(basename "$f")
		name="${base%.*}"
		ext="${base##*.}"
		new="${name}-${suffix}.${ext}"
		install -m 644 "$f" "${D}${N_WEBRADIO_DIR}/${new}"
	done
}

FILES:${PN} += " \
	${N_WEBRADIO_DIR} \
"
