include neutrino-lua-plugins-target-pattern.inc

SRC_NAME = "stb_move"
PR = "r3"

RDEPENDS:${PN}:append = " util-linux-mountpoint"
