include neutrino-lua-plugins-target-pattern.inc

MAINTAINER = "vanhofen"
HOMEPAGE = "https://github.com/neutrino-images/ni-neutrino-plugins"

PV = "ni-git-${SRCPV}"

SRC_SUBPATH = "scripts-lua/plugins/${SRC_NAME}"

MIGIT_SUBDIR = "${SRC_SUBPATH}"

SRC_URI = " \
	git://github.com/neutrino-images/ni-neutrino-plugins.git;protocol=https;branch=master \
"

#SRC_NAME = "replay"
