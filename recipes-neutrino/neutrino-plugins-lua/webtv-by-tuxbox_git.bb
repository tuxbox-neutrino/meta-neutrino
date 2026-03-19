include neutrino-lua-plugins.inc
include neutrino-provider-webtv.inc

SUMMARY = "WebTV content from tuxbox"
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "tuxbox"
SECTION = "optional"
HOMEPAGE = "https://github.com/tuxbox-neutrino/plugin-scripts-lua.git"

SRC_NAME = "webtv"
MIGIT_SUBDIR = "plugins/${SRC_NAME}"
SRC_URI:append = " \
	file://streamlink-by-tuxbox.lua \
	file://yt_live-by-tuxbox.xml \
"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PE = "1"
PR = "r5"

do_compile[noexec] = "1"

do_install:append() {
	# Override upstream YT list with currently verified @channel/live URLs.
	install -m 0644 ${WORKDIR}/streamlink-by-tuxbox.lua ${D}${N_WEBTV_DIR}/streamlink-by-tuxbox.lua
	install -m 0644 ${WORKDIR}/yt_live-by-tuxbox.xml ${D}${N_WEBTV_DIR}/yt_live-by-tuxbox.xml
}

RDEPENDS:${PN}:append = " curl lua-json"
RRECOMMENDS:${PN}:append = " streamlink"

FILES:${PN} += " \
	${N_WEBTV_DIR} \
"
