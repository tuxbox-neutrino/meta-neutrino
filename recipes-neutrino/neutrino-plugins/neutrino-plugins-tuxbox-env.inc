include neutrino-plugins.inc

MAINTAINER = "tuxbox"
HOMEPAGE = "https://tuxbox-neutrino.org"

PKGV = "${GITPKGVTAG}"

SRCREV_${PLUGIN_NAME} = "${AUTOREV}"
SRCREV_FORMAT = "${MAINTAINER}-${PLUGIN_NAME}"

SUB_PATH ?= ""
PLUGIN_BRANCH ?= "master"

SRC_URI += " \
	git://github.com/tuxbox-neutrino/plugin-${PLUGIN_NAME}.git;name=${PLUGIN_NAME};subpath=${SUB_PATH};protocol=https;branch=${PLUGIN_BRANCH};destsuffix=${PLUGIN_NAME} \
"
