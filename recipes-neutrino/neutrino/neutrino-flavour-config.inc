# Common flavour/box mapping for Neutrino + libstb-hal.
# Keep provider-specific SRC_URI/PKGV in the respective *tuxbox.inc files.

NEUTRINO_OECONF_BOXMAP = " \
    --with-boxtype=${NEUTRINO_BOXTYPE} \
    --with-boxmodel=${NEUTRINO_BOXMODEL} \
"

NEUTRINO_OECONF_TARGETROOT = " \
    --with-targetroot= \
    --with-targetprefix=/usr \
"
