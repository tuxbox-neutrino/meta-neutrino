SUMMARY = "Neutrino Plugins"
DESCRIPTION = "A collection of Neutrino-Plugins from different providers."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6 \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PR = "r4"
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
    neutrino-plugin-data \
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

# On these machines, vendor DVB modules already install /usr/bin/turnoff_power.
# Removing the plugin package avoids rootfs file clashes during image creation.
RDEPENDS:${PN}:remove:hd60 = "turnoff-power"
RDEPENDS:${PN}:remove:hd61 = "turnoff-power"
RDEPENDS:${PN}:remove:hd66se = "turnoff-power"
