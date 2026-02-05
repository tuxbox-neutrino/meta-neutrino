include neutrino-common-vars.inc

HOMEPAGE = "https://www.neutrino-images.de"
MAINTAINER = "Sven Hoefer"

SRC_URI:append = " git://github.com/neutrino-images/ni-neutrino;protocol=https;branch=master \
"

EXTRA_OECONF:append = " \
			--with-boxtype=${NEUTRINO_BOXTYPE} \
			--with-boxmodel=${NEUTRINO_BOXMODEL} \
			--with-targetroot=/usr \
			--enable-pkg-management \
"
