HOMEPAGE = "https://github.com/TangoCash"
MAINTAINER = "TangoCash"

SRC_URI += "git://github.com/TangoCash/libstb-hal-tangos.git;protocol=https \
"

PKGV = "${FLAVOUR}-${GITPKGV}"

EXTRA_OECONF:append += " \
			--enable-reschange \
                        --with-boxtype=${MACHINE} \
"
