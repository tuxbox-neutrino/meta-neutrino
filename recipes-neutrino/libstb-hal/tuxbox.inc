include ../neutrino/neutrino-flavour-config.inc

HOMEPAGE = "http://www.tuxbox.org"
MAINTAINER = "Tuxbox-Developers"

SRC_URI += "git://github.com/tuxbox-neutrino/library-stb-hal.git;branch=mpx;protocol=https \
"

PKGV = "${GITPKGVTAG}"
GITPKGV_PREFIX = "."
GITPKGVTAG_STYLE = "count-short"

EXTRA_OECONF:append = " \
			${NEUTRINO_OECONF_BOXMAP} \
			${NEUTRINO_OECONF_TARGETROOT} \
"
