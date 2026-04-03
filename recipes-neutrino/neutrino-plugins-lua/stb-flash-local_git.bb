include neutrino-lua-plugins-target-pattern.inc

SRC_NAME = "stb_local-flash"
PR = "r4"

# Local flash uses the same Neutrino backup.sh/tobackup.conf path as online
# flash, so etckeeper stays an optional extra tool.
RDEPENDS:${PN}:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
