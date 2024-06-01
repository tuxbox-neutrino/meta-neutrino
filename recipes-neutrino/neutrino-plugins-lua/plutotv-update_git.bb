include neutrino-lua-plugins-target-pattern.inc

SUMMARY = "Neutrino LUA-Script Plugin for updating Pluto-TV."
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "vanhofen"
HOMEPAGE = "https://github.com/neutrino-images/ni-neutrino-plugins"
LICENSE = "WTFPLv2"

SRC_SUBPATH = "scripts-lua/plugins/${SRC_NAME}"

SRC_URI = " \
	git://github.com/neutrino-images/ni-neutrino-plugins.git;protocol=https;subpath=${SRC_SUBPATH};destsuffix=${SRC_DEST_SUFFIX} \
"
