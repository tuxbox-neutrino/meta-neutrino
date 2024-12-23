include neutrino-plugins-ni-env.inc

DESCRIPTION = "Terminal controlled menu manager known as Flexmenu."

DEPENDS += "neutrino-fonts-extra"

PR = "r2"

## Hack: make install is doing strange things, try to fix it here
do_install () {
	install -d ${D}${bindir}
	install -d ${D}${N_PLUGIN_DIR}
	oe_runmake install DESTDIR=${D}
}

FILES:${PN} += " \
	/share \
	/var \
"
