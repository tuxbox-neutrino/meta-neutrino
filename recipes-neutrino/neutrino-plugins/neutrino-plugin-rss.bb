DESCRIPTION = "Neutrino Lua RSS Reader"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
HOMEPAGE = ""
MAINTAINER = "Jacek Jendrzej"
DEPENDS = "lua expat"
RDEPENDS_${PN} = "lua-expat lua-feedparser lua-curl"

PV = "0.50"
PR = "1"


SRC_URI = "file://rss.tar.gz \
"

S = "${WORKDIR}"

do_install () {
	install -d ${D}/usr/share/tuxbox/plugins/rss_addon ${D}$sysconfdir/neutrino/config
	install -m 644 ${S}/rss.lua ${D}/usr/share/tuxbox/plugins
	install -m 644 ${S}/rss.cfg ${D}/usr/share/tuxbox/plugins
	install -m 644 ${S}/rss_addon/* ${D}/usr/share/tuxbox/plugins/rss_addon
	install -m 644 ${S}/rss_hint.png ${D}/usr/share/tuxbox/plugins/
	install -m 644 ${S}/rssreader.conf ${D}$sysconfdir/neutrino/config
}

FILES_${PN} =  "/usr \
		/etc \	
"
