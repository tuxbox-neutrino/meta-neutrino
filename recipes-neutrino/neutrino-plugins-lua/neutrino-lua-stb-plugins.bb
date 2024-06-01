SUMMARY = "Neutrino LUA STB-Plugins"
DESCRIPTION = "A collection of Neutrino-Script Plugins for specific machine operations for your Set Top Box."
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6 \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PR = "r1"
PACKAGES = "${PN}"

RDEPENDS:${PN} = " \
        stb-backup \
        stb-flash \
        stb-flash-local \
        stb-log \
        stb-move \
        stb-restore \
        stb-shell \
        stb-startup \
"

