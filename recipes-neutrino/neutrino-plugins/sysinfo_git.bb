include neutrino-plugins-tuxbox-env.inc

DESCRIPTION = "Shows graphically processed system informations"

SRC_URI += " \
	file://0001-sysinfo-fix-install-of-sysinfo-in-Makefile.am.patch \
	file://0002-sysinfo-fix-format-security.patch \
"

do_patch () {
	git -C ${S}/${PLUGIN_NAME} apply ${S}/0001-sysinfo-fix-install-of-sysinfo-in-Makefile.am.patch
	git -C ${S}/${PLUGIN_NAME} apply ${S}/0002-sysinfo-fix-format-security.patch
}
