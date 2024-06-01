SUMMARY = "Neutrino LUA-Script Plugins for Pluto-TV."
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "vanhofen"
LICENSE = "WTFPLv2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PR = "r1"
PACKAGES = "${PN}"

RDEPENDS:${PN} = " \
        plutotv-update \
        plutotv-vod \
"

