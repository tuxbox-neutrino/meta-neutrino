# Packagegroup: Tuxbox Neutrino Stack
#
# Neutrino GUI and related packages

DESCRIPTION = "Tuxbox-OS Neutrino GUI packages"
LICENSE = "MIT"
PR = "r4"

inherit packagegroup

RDEPENDS:${PN} = " \
    neutrino \
    libstb-hal \
    logoupdater \
    mediathek \
    neutrino-plugins \
    ${@bb.utils.contains('MACHINE_FEATURES', 'fastboot', 'neutrino-lua-stb-plugins', '', d)} \
"

# Optional themes and generic script plugin bundles beyond the default runtime set
RRECOMMENDS:${PN} = " \
    neutrino-lua-plugins \
    neutrino-themes \
    neutrino-logos \
"
