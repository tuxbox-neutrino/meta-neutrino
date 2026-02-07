SUMMARY = "Neutrino, a fast and beautiful graphical user interface"
DESCRIPTION = "${SUMMARY} for ${MACHINE_BRAND}-${MACHINE}."
SECTION = "libs"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCPV_WORKSPACE = "9999"
PR = "r14"
SRCREV = "${AUTOREV}"

PATCHTOOL = "git"

SRC_URI = " \
	file://neutrino.ttf \
	file://neutrino.service \
	file://neutrino-log.service \
	file://neutrino.sh_tuxbox \
	file://timezone.xml \
	file://custom-poweroff.init \
	file://pre-wlan0.sh \
	file://post-wlan0.sh \
	file://mount.mdev \
	file://tobackup.conf \
"

S = "${WORKDIR}/git"

include neutrino.inc
include tuxbox.inc

do_configure:prepend() {
	cp ${WORKDIR}/neutrino.ttf ${S}/data/fonts
}
