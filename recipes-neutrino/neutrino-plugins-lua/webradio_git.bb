SUMMARY = "Webradio content meta package"
DESCRIPTION = "Install webradio provider content packages."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

MAINTAINER = "community"
SECTION = "optional"

PR = "r3"
PV = "1.0"

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} = " \
	webradio-by-ni \
"
