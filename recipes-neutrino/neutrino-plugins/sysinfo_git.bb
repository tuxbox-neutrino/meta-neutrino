include neutrino-plugins-tuxbox-env.inc

DESCRIPTION = "Shows graphically processed system informations"

SRC_URI += " \
	file://0002-sysinfo-fix-format-security.patch \
	file://0003-install-use-srcdir.patch \
"

FILES:${PN} += " \
	/var/tuxbox/plugins/sysinfo.so \
	/var/tuxbox/plugins/sysinfo.cfg \
	/var/tuxbox/plugins/sysinfo_hint.png \
"
