include neutrino-lua-plugins-target-pattern.inc

SUMMARY = "Neutrino Lua Plugin: Logo Updater"
DESCRIPTION = "Standalone logoupdater plugin for channel logo updates."
HOMEPAGE = "https://github.com/tuxbox-neutrino/plugin-lua-logoupdater"

SRC_NAME = "logoupdater"

SRC_URI = "git://github.com/tuxbox-neutrino/plugin-lua-logoupdater.git;protocol=https;branch=master"
SRCREV = "fab467b1e3d665f90e4b6b0bf0458c4f5dce9eb7"
S = "${WORKDIR}/git"

MIGIT_ENABLED = "0"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=a5f8f5771e40cfa0fef989e421db6c7e"

inherit gitpkgv
PKGV = "${GITPKGV}"

# Keep package feed ordering monotonic after migration from the monorepo recipe.
PE = "1"
PR = "r2"

PLUGIN_SOURCE_DIR = "${S}/plugin"
PLUGIN_SCRIPT_NAME = "logoupdater"

# Runtime tools used by the plugin for online updates.
RDEPENDS:${PN} += "lua-feedparser lua-expat lua-json luaposix curl rsync unzip ca-certificates"
RRECOMMENDS:${PN} += "git"
