SUMMARY = "Neutrino Plugins"
DESCRIPTION = "A collection of Neutrino-Plugins from different providers."
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6 \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PR = "r1"
PACKAGES = "${PN}"

RDEPENDS:${PN} = " \
    asc2uni \
    autoreboot \
    epgscan \
    getrc \
    imgbackup \
    initfb \
    input  \
    logomask \
    msgbox  \
    neutrino-fonts-extra \
    oled-ctrl  \
    pr-auto-timer \
    satfind  \
    shellexec \
    showiframe \
    stbup  \
    sysinfo \
    turnoff-power \
    tuxcal   \
    tuxcom \
    tuxmail \
    tuxwetter \
"

