SUMMARY = "Neutrino Lua Plugin: STB-Startup"
DESCRIPTION = "Standalone stb-startup plugin for multiboot startup switching."
HOMEPAGE = "https://github.com/tuxbox-neutrino/plugin-lua-stb-startup"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=a5f8f5771e40cfa0fef989e421db6c7e"

PE = "2"
PR = "r7"

inherit gitpkgv
PKGV = "${GITPKGV}"

SRC_URI = "git://github.com/tuxbox-neutrino/plugin-lua-stb-startup.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

# for common neutrino paths (icons, plugin dirs, etc.)
include ../neutrino/neutrino-common-vars.inc

RDEPENDS:${PN} = "luaposix"

do_compile[noexec] = "1"

do_install () {
	oe_runmake \
		DESTDIR=${D} \
		PREFIX=${N_PREFIX}${N_DATADIR}/neutrino \
		PLUGIN_SUBDIR=$(basename ${N_PLUGIN_DIR}) \
		install

	# Keep startup config writable/persistent via /etc with plugin-side symlink.
	install -d ${D}${N_CONFIG_DIR}
	install -m 644 ${S}/plugin/stb-startup.conf ${D}${N_CONFIG_DIR}/stb-startup.conf
	rm -f ${D}${N_PLUGIN_DIR}/stb-startup.conf
	ln -sf ${N_CONFIG_DIR}/stb-startup.conf ${D}${N_PLUGIN_DIR}/stb-startup.conf

	# Cleanup legacy filename from older package revisions.
	rm -f ${D}${N_PLUGIN_DIR}/stb-startup_old.lua
}

FILES:${PN} += " \
	${datadir}/tuxbox/neutrino \
	${N_PLUGIN_DIR} \
	${N_CONFIG_DIR}/stb-startup.conf \
"
