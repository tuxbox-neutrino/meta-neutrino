include neutrino-plugins-tuxbox-env.inc

DESCRIPTION = "A terminal controlled message box handler."

DEPENDS += "neutrino-fonts-extra"
RDEPENDS:${PN}:append = " neutrino-fonts-extra"
PR = "r3"
