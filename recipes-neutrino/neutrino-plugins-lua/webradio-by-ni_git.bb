include neutrino-lua-plugins.inc
include neutrino-provider-webradio.inc

SUMMARY = "Webradio content from ni"
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "ni"
SECTION = "optional"
HOMEPAGE = "https://github.com/neutrino-images/ni-neutrino-plugins"

SRC_NAME = "webradio"
MIGIT_SUBDIR = "scripts-lua/plugins/${SRC_NAME}"

SRC_URI = " \
	git://github.com/neutrino-images/ni-neutrino-plugins.git;protocol=https;branch=master \
"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PR = "r2"

do_compile[noexec] = "1"

FILES:${PN} += " \
	${N_WEBRADIO_DIR} \
"
