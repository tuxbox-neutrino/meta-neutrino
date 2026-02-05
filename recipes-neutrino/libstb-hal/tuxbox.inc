HOMEPAGE = "http://www.tuxbox.org"
MAINTAINER = "Tuxbox-Developers"

SRC_URI += "git://github.com/tuxbox-neutrino/library-stb-hal.git;branch=mpx;protocol=https \
"

PKGV = "${GITPKGVTAG}"

EXTRA_OECONF:append = " \
			--with-boxtype=${NEUTRINO_BOXTYPE} \
			--with-boxmodel=${NEUTRINO_BOXMODEL} \
			--with-targetroot= \
			--with-targetprefix= /usr \
"
