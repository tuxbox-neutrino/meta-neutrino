SUMMARY = "Neutrino 3rd Party Themes"
DESCRIPTION = "A group themes from different providers."
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6 \
"

PACKAGE_ARCH = "all"

inherit packagegroup

PR = "r1"
PACKAGES = "${PN}"

RDEPENDS:${PN} = "\
    neutrino-themes-ni \
    neutrino-themes-tango \
"
