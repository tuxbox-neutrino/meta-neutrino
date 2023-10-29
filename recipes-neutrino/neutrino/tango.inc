include neutrino-common-vars.inc

HOMEPAGE = "https://github.com/TangoCash"
MAINTAINER = "TangoCash"

SRC_URI:append += "git://github.com/TangoCash/neutrino-tangos.git;protocol=https \
"

PV = "${FLAVOUR}-git${SRCPV}"

EXTRA_OECONF:append += " \
			--enable-reschange \
                        --with-boxtype=${MACHINE} \
"
