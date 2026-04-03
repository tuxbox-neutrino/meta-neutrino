include neutrino-lua-plugins-target-pattern.inc

SRC_NAME = "stb_flash"
PR = "r3"

RDEPENDS:${PN}:append = " curl etckeeper util-linux-mountpoint ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
