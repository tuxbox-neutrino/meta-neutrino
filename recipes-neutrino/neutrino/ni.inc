include neutrino-common-vars.inc

HOMEPAGE = "https://www.neutrino-images.de"
MAINTAINER = "Sven Hoefer"

SRC_URI:append += "git://github.com/neutrino-images/ni-neutrino;protocol=https \
"

EXTRA_OECONF:append += " \
			--with-boxtype=${TARGET_ARCH}box \
			--with-boxmodel=${MACHINE} \
			--with-targetroot=/usr \
			--enable-pkg-management \
"
