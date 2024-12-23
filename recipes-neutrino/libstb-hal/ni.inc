HOMEPAGE = "https://www.neutrino-images.de"
MAINTAINER = "Sven Hoefer"

SRC_URI += "git://github.com/neutrino-images/ni-libstb-hal;protocol=https \
"

EXTRA_OECONF:append += " \
			--with-boxtype=${TARGET_ARCH}box \
			--with-boxmodel=${MACHINE} \
			--with-targetroot= \
			--with-targetprefix= /usr \
"
