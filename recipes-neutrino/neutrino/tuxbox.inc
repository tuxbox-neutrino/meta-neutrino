include neutrino-common-vars.inc
include neutrino-flavour-config.inc

HOMEPAGE = "http://www.tuxbox.org"
MAINTAINER = "Tuxbox-Developers"

SRC_URI:append = " git://github.com/tuxbox-neutrino/gui-neutrino.git;protocol=https;branch=master \
		   file://0001-hdd_menu.cpp-assume-device-mounted.patch \
"

PKGV = "${GITPKGVTAG}"

DEFAULT_THEME ?= "Neutrino-3.0"

EXTRA_OECONF:append = " --with-default-theme=${DEFAULT_THEME} \
                        ${NEUTRINO_OECONF_BOXMAP} \
			--with-lcd4liconsdir=${N_LCD4L_ICONSDIR} \
			--with-lcd4liconsdir_var=${N_LCD4L_ICONSDIR_VAR} \
			--enable-pkg-management \
"

do_install:append = " mv -f ${D}${sysconfdir}/neutrino/config/bad_package_pattern.list.sample ${D}${sysconfdir}/neutrino/config/bad_package_pattern.list"

