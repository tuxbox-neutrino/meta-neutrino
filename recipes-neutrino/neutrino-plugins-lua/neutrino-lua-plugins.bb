SUMMARY = "Neutrino LUA Plugins"
DESCRIPTION = "A collection of Neutrino-Script Plugins from different providers."
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6 \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PR = "r1"
PACKAGES = "${PN}"

RDEPENDS:${PN} = " \
        ard-mediathek \
        browser \
        favorites-to-bin \
        heizoelpreise \
        local-tv \
        logoupdater \
        mediathek \
        mtv \
        myspass \
        netzkino \
        neutrino-lua-plugins-shared-files \
        neutrino-lua-plugins-plutotv \
        rcu-switcher \
        replay \
        rss \
        settingsupdater \
        to-web-tv-xml \
        webradio \
        webtv \
        xupnpd-content \
        zdfhbbtv \
"

