include neutrino-common-vars.inc

HOMEPAGE = "https://github.com/TangoCash"
MAINTAINER = "TangoCash"

SRC_URI:append = " git://github.com/TangoCash/neutrino-tangos.git;protocol=https;branch=evo \
"

PKGV = "${FLAVOUR}-${GITPKGV}"

EXTRA_OECONF:append = " \
			--enable-reschange \
                        --with-boxtype=${MACHINE} \
"
