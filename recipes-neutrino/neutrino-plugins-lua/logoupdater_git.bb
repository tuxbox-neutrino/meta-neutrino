SUMMARY = "Neutrino Lua Plugin: Logo Updater"
DESCRIPTION = "Standalone logoupdater plugin for channel logo updates."
HOMEPAGE = "https://github.com/tuxbox-neutrino/plugin-lua-logoupdater"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=a5f8f5771e40cfa0fef989e421db6c7e"

# Recipe metadata update (install style migration from makeit to oe_runmake).
PE = "1"
PR = "r6"

inherit gitpkgv
PKGV = "${GITPKGV}"

SRC_URI = "git://github.com/tuxbox-neutrino/plugin-lua-logoupdater.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

# for common neutrino paths (icons, plugin dirs, etc.)
include ../neutrino/neutrino-common-vars.inc

# The plugin exposes a git download mode in its UI, so git must be present for
# the default-installed package as well as feed installations.
RDEPENDS:${PN} = "lua-feedparser lua-expat lua-json luaposix curl rsync unzip ca-certificates git"

do_compile[noexec] = "1"

do_install () {
	oe_runmake \
		DESTDIR=${D} \
		PREFIX=${N_PREFIX}${N_DATADIR}/neutrino \
		PLUGIN_SUBDIR=$(basename ${N_PLUGIN_DIR}) \
		install
}

FILES:${PN} += " \
	${datadir}/tuxbox/neutrino \
	${N_PLUGIN_DIR} \
"
