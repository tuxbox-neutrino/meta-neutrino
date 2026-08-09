SUMMARY = "Neutrino Lua Plugin: Webmin Setup"
DESCRIPTION = "Start, autostart and port of the Webmin web administration, \
reachable from the Neutrino network settings menu."
HOMEPAGE = "https://github.com/tuxbox-neutrino/plugin-lua-webmin-setup"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=67020d1d365d3fb5e3e589ac50900256"

PR = "r0"

inherit gitpkgv
PKGV = "${GITPKGVTAG}"

SRC_URI = "git://github.com/tuxbox-neutrino/plugin-lua-webmin-setup.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

inherit allarch

# for common neutrino paths (plugin dirs, prefix, ...)
include ../neutrino/neutrino-common-vars.inc

# The plugin controls the webmin service through systemctl; without the
# service there is nothing for it to switch.
RDEPENDS:${PN} = "webmin neutrino-lua-plugins-shared-files"
RDEPENDS:${PN}:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"

do_compile[noexec] = "1"

do_install () {
	oe_runmake \
		DESTDIR=${D} \
		PREFIX=${N_PREFIX}${N_DATADIR}/neutrino \
		PLUGIN_SUBDIR=$(basename ${N_PLUGIN_DIR}) \
		LUAPLUGIN_SUBDIR=$(basename ${N_LUAPLUGIN_DIR}) \
		install

	chown -R root:root ${D}
}

FILES:${PN} += " \
	${datadir}/tuxbox/neutrino \
	${N_PLUGIN_DIR} \
	${N_LUAPLUGIN_DIR} \
"
