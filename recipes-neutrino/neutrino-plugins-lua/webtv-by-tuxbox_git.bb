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
	file://yt_live-by-tuxbox.xml;subdir=webtv-by-tuxbox-overrides \
"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PE = "1"
PR = "r8"

do_compile[noexec] = "1"

do_install:append() {
	install -m 0644 ${WORKDIR}/streamlink-by-tuxbox.lua ${D}${N_WEBTV_DIR}/streamlink-by-tuxbox.lua
	# Override upstream YT list with currently verified @channel/live URLs.
	install -m 0644 ${WORKDIR}/webtv-by-tuxbox-overrides/yt_live-by-tuxbox.xml ${D}${N_WEBTV_DIR}/yt_live-by-tuxbox.xml

	yt_live_lua="${D}${N_WEBTV_DIR}/yt_live-by-tuxbox.lua"
	if [ ! -f "$yt_live_lua" ]; then
		bbfatal "yt_live-by-tuxbox.lua missing after install"
	fi
	sed -i -e 's|python /usr/bin/yt-dlp|/usr/bin/yt-dlp|g' "$yt_live_lua"
}

RDEPENDS:${PN}:append = " curl lua-json streamlink python3-yt-dlp"

FILES:${PN} += " \
	${N_WEBTV_DIR} \
"
