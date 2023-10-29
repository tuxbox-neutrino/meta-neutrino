SUMMARY = "Neutrino HD"
DESCRIPTION = "Neutrino-MP for AX HD51 Platform."
HOMEPAGE = "http://www.tuxbox.org"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

SRCREV = "${AUTOREV}"
PV = "3.5.1"

SRC_URI = "git://github.com/tuxbox-neutrino/gui-neutrino.git;branch=master;protocol=http \
	file://0007-imageinfo.cpp-change-version-output.patch \
	file://0008-rcsim.c-fix-eventdev-for-yocto.patch \
	file://0009-src-nhttpd-tuxboxapi-controlapi.cpp-fix-eventdev-for.patch \
	file://0012-import-proper-working-format-device-function.patch \
	file://opkg/0001-opkg_manager-remove-reboot-and-restart-trigger-files.patch \
	file://opkg/0003-opkg-0.3.x-uses-opkg-instead-of-opkg-cl-as-binary-na.patch \
	file://neutrino.service \
	file://neutrino.sh \
	file://timezone.xml \
	file://custom-poweroff.init \
	file://COPYING.GPL \
	file://pre-wlan0.sh \
	file://post-wlan0.sh \
	file://mount.mdev \
	file://icons.tar.gz \
	file://var.tar.gz \
"

include neutrino.inc

EXTRA_OECONF:append += "--with-boxtype=coolstream \
						--with-boxmodel=hd2 \
"

N_CFLAGS:append += "-I${STAGING_INCDIR}/sigc++-2.0"

N_CPPFLAGS:append += "-D_GLIBCXX_USE_CXX11_ABI=0"
