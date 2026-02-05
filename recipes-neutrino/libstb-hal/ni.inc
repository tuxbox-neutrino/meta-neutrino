HOMEPAGE = "https://www.neutrino-images.de"
MAINTAINER = "Sven Hoefer"

SRC_URI += "git://github.com/neutrino-images/ni-libstb-hal;protocol=https;branch=master \
"

EXTRA_OECONF:append = " \
			--with-boxtype=${NEUTRINO_BOXTYPE} \
			--with-boxmodel=${NEUTRINO_BOXMODEL} \
			--with-targetroot= \
			--with-targetprefix= /usr \
"
