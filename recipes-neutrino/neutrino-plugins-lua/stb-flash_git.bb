include neutrino-lua-plugins-target-pattern.inc

SRC_NAME = "stb_flash"
PR = "r4"

# Settings backup for flash workflows is provided by Neutrino's backup.sh
# using /etc/neutrino/config/tobackup.conf. Keep etckeeper optional.
RDEPENDS:${PN}:append = " curl util-linux-mountpoint ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
