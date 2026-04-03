include neutrino-lua-plugins-target-pattern.inc

SRC_NAME = "stb_local-flash"
PR = "r3"

RDEPENDS:${PN}:append = " etckeeper ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
