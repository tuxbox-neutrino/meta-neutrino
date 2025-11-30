SUMMARY = "Neutrino Lua Plugin: Mediathek"
DESCRIPTION = "Standalone Mediathek plugin for Neutrino"
HOMEPAGE = "https://github.com/tuxbox-neutrino/neutrino-mediathek"

LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=e7f9ee1b2802417caeac2327f3f6817b"

PR = "r2"

inherit gitpkgv
PKGV = "${GITPKGVTAG}"

SRC_URI = "git://github.com/tuxbox-neutrino/neutrino-mediathek.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

inherit allarch

# for common neutrino paths (icons, plugin dirs, etc.)
include ../neutrino/neutrino-common-vars.inc

RDEPENDS:${PN} = "lua-feedparser lua-expat lua-json luaposix neutrino-lua-plugins-shared-files"

do_compile[noexec] = "1"

do_install () {
	oe_runmake \
		DESTDIR=${D} \
		PREFIX=${N_PREFIX}${N_DATADIR}/neutrino \
		PLUGIN_SUBDIR=$(basename ${N_PLUGIN_DIR}) \
		LUAPLUGIN_SUBDIR=$(basename ${N_LUAPLUGIN_DIR}) \
		ICONSDIR=${N_ICONS_DIR} \
		ICONSDIR_VAR=${N_ICONS_DIR_VAR} \
		install

	# Avoid host uid/gid leakage from upstream Makefile's cp -a
	chown -R root:root ${D}
}

FILES:${PN} += " \
	${datadir}/tuxbox/neutrino \
	${N_PLUGIN_DIR} \
	${N_LUAPLUGIN_DIR} \
"
