SUMMARY = "Kodi Media Center PVR plugins"

PV = "${@bb.utils.contains("MACHINE_FEATURES", "kodi18", "2.4.5+git${SRCPV}", "1.4.10+git${SRCPV}", d)}"
PKGV = "${@bb.utils.contains("MACHINE_FEATURES", "kodi18", "2.4.5+git${GITPKGV}", "1.4.10+git${SRCPV}", d)}"

KODIADDONPLUGIN = "wmc"

require kodi-addon-pvr.inc

SRC_URI:append = "${@bb.utils.contains("MACHINE_FEATURES", "kodi18", " file://0001-Fix-build-pvr-wmc.patch ", "", d)}"
