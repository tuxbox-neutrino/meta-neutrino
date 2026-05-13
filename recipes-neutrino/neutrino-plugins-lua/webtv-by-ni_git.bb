include neutrino-lua-plugins.inc
include neutrino-provider-webtv.inc

SUMMARY = "WebTV content from ni"
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "ni"
SECTION = "optional"
HOMEPAGE = "https://github.com/neutrino-images/ni-neutrino-plugins"

SRC_NAME = "webtv"
MIGIT_SUBDIR = "scripts-lua/plugins/${SRC_NAME}"

SRC_URI = " \
	git://github.com/neutrino-images/ni-neutrino-plugins.git;protocol=https;branch=master \
"
SRC_URI:append = " \
	file://streamlink-by-ni.lua \
	file://yt_live-by-ni.xml;subdir=webtv-by-ni-overrides \
"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PE = "1"
PR = "r6"

do_compile[noexec] = "1"

PROVIDER_SKIP_BASENAMES = "filmon"

do_install:append() {
	install -m 0644 ${WORKDIR}/streamlink-by-ni.lua ${D}${N_WEBTV_DIR}/streamlink-by-ni.lua
	# Override upstream YT list with currently verified @channel/live URLs.
	install -m 0644 ${WORKDIR}/webtv-by-ni-overrides/yt_live-by-ni.xml ${D}${N_WEBTV_DIR}/yt_live-by-ni.xml

	yt_live_lua="${D}${N_WEBTV_DIR}/yt_live-by-ni.lua"
	if [ ! -f "$yt_live_lua" ]; then
		bbfatal "yt_live-by-ni.lua missing after install"
	fi
	sed -i -e 's|python /usr/bin/yt-dlp|/usr/bin/yt-dlp|g' "$yt_live_lua"
}

RDEPENDS:${PN}:append = " curl lua-json streamlink python3-yt-dlp"

FILES:${PN} += " \
	${N_WEBTV_DIR} \
"
