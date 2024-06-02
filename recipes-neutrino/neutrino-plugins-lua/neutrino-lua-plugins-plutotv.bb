SUMMARY = "Neutrino LUA-Script Plugins for Pluto-TV."
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "vanhofen"
LICENSE = "WTFPL"
LIC_FILES_CHKSUM = "file://${THISDIR}/files/${LICENSE};md5=7993e3336259bdb618ad5a1afc872165"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PR = "r1"
PACKAGES = "${PN}"

RDEPENDS:${PN} = " \
        plutotv-update \
        plutotv-vod \
"

