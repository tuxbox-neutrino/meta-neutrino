SUMMARY = "Neutrino, a fast and beautiful graphical user interface"
DESCRIPTION = "${SUMMARY} for ${MACHINE_BRAND}-${MACHINE}."
SECTION = "libs"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCPV_WORKSPACE = "9999"
PR = "r21"
SRCREV = "${AUTOREV}"

PATCHTOOL = "git"

SRC_URI = " \
	file://neutrino.ttf \
	file://neutrino.service \
	file://neutrino-log.service \
	file://neutrino.sh_tuxbox \
	file://streamripper.sh \
	file://timezone.xml \
	file://pre-wlan0.sh \
	file://post-wlan0.sh \
	file://mount.mdev \
	file://tobackup.conf \
	file://0001-neutrino-prefer-image-version-first.patch \
	file://0002-libnet-guard-netGetIP-against-failed-ioctls.patch \
"

S = "${WORKDIR}/git"

include neutrino.inc
include tuxbox.inc

do_configure:prepend() {
	cp ${WORKDIR}/neutrino.ttf ${S}/data/fonts
}
