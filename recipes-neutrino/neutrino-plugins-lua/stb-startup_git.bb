include neutrino-lua-plugins-target-pattern.inc

SUMMARY = "Neutrino Lua Plugin: STB-Startup"
DESCRIPTION = "Standalone stb-startup plugin for multiboot startup switching."
HOMEPAGE = "https://github.com/tuxbox-neutrino/plugin-lua-stb-startup"

SRC_NAME = "stb-startup"

SRC_URI = "git://github.com/tuxbox-neutrino/plugin-lua-stb-startup.git;protocol=https;branch=master"
SRCREV = "39e3dd6ac2f29d9a26d56453f00b8096c0290eed"
S = "${WORKDIR}/git"

MIGIT_ENABLED = "0"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=a5f8f5771e40cfa0fef989e421db6c7e"

inherit gitpkgv
PKGV = "${GITPKGV}"

PE = "2"
PR = "r5"

PLUGIN_SOURCE_DIR = "${S}/plugin"
PLUGIN_SCRIPT_NAME = "stb-startup"

do_install:append () {
	rm -f ${D}${N_PLUGIN_DIR}/stb-startup_old.lua
}
