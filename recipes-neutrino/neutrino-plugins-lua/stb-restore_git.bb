include neutrino-lua-plugins-target-pattern.inc

SRC_NAME = "stb_restore"
PR = "r3"

RDEPENDS:${PN}:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
