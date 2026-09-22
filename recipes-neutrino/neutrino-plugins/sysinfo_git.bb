include neutrino-plugins-tuxbox-env.inc

DESCRIPTION = "Shows graphically processed system informations"

FILES:${PN} += " \
	/var/tuxbox/plugins/sysinfo.so \
	/var/tuxbox/plugins/sysinfo.cfg \
	/var/tuxbox/plugins/sysinfo_hint.png \
"
