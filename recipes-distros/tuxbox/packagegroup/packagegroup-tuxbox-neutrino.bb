# Packagegroup: Tuxbox Neutrino Stack
#
# Neutrino GUI and related packages

DESCRIPTION = "Tuxbox-OS Neutrino GUI packages"
LICENSE = "MIT"
PR = "r1"

inherit packagegroup

RDEPENDS:${PN} = " \
    neutrino \
    libstb-hal \
    neutrino-plugins \
"

# Optional themes and script plugin bundles
RRECOMMENDS:${PN} = " \
    neutrino-lua-plugins \
    neutrino-lua-stb-plugins \
    neutrino-themes \
    neutrino-logos \
"
