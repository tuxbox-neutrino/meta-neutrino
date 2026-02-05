include neutrino-plugins-tuxbox-env.inc

DESCRIPTION = "Simple graphic file manager known as Tuxbox Commander."

DEPENDS += "neutrino-fonts-extra"

SRC_URI += " \
	file://0001-tuxcom-Makefile.am-fix-install.patch \
	file://0002-tuxcom-generic-GetRCCode.patch \
"

do_patch () {
	git -C ${S}/${PLUGIN_NAME} apply ${S}/0001-tuxcom-Makefile.am-fix-install.patch
	git -C ${S}/${PLUGIN_NAME} apply ${S}/0002-tuxcom-generic-GetRCCode.patch
}
