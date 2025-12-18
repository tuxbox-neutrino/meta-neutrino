include neutrino-plugins-ni-env.inc

DESCRIPTION = "Plugin for weather data and forecast known as Tuxwetter."

DEPENDS += "neutrino-fonts-extra"

SRC_URI += " \
	file://0001-tuxwetter-Makefile.am-fix-install.patch \
	file://0002-format-security.patch \
"

# "bindir" will never installed
do_install:prepend () {
	install -d ${D}${bindir}
}
