include neutrino-plugins-ni-env.inc

DESCRIPTION = "Small terminal based ascII to unicode converter."

do_install() {
	install -d ${D}${bindir}
	oe_runmake install DESTDIR=${D}
}
