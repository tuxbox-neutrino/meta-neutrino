include neutrino-plugins-ni-env.inc

DESCRIPTION = "Graphical e-mail client."

DEPENDS += "neutrino-fonts-extra"

# We need an extra template for configure.ac
SRC_URI += " \
	file://configure.ac.${PLUGIN_NAME} \
"
