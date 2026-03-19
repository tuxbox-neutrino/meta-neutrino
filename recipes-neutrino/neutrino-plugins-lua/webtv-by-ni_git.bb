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
	file://yt_live-by-ni.xml \
"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PE = "1"
PR = "r5"

do_compile[noexec] = "1"

PROVIDER_SKIP_BASENAMES = "filmon"

do_install:append() {
	# Keep streamlink optional: fallback script returns resolved URL when
	# streamlink is unavailable on small images.
	install -m 0644 ${WORKDIR}/streamlink-by-ni.lua ${D}${N_WEBTV_DIR}/streamlink-by-ni.lua
	# Override upstream YT list with currently verified @channel/live URLs.
	install -m 0644 ${WORKDIR}/yt_live-by-ni.xml ${D}${N_WEBTV_DIR}/yt_live-by-ni.xml
}

RDEPENDS:${PN}:append = " curl lua-json"
RRECOMMENDS:${PN}:append = " streamlink"

FILES:${PN} += " \
	${N_WEBTV_DIR} \
"
