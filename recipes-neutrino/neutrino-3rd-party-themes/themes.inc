
inherit allarch pkgconfig

SECTION = "themes"
PRE_DESCRIPTION = "Theme set"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6 \
"
SUBPATH = "data"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}"

FILES:${PN} = "\
    /var/tuxbox/themes \
"
