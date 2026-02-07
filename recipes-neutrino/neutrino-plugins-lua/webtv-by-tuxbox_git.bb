include neutrino-lua-plugins.inc
include neutrino-provider-webtv.inc

SUMMARY = "WebTV content from tuxbox"
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "tuxbox"
SECTION = "optional"
HOMEPAGE = "https://github.com/tuxbox-neutrino/plugin-scripts-lua.git"

SRC_NAME = "webtv"
MIGIT_SUBDIR = "plugins/${SRC_NAME}"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PR = "r2"

do_compile[noexec] = "1"

FILES:${PN} += " \
	${N_WEBTV_DIR} \
"
