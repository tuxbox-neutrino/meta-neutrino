SUMMARY = "OSCam: Open Source Conditional Access Module"
HOMEPAGE = "http://www.streamboard.tv/oscam/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "libusb1 openssl"

DEPENDS_APPEND_libc-uclibc += "virtual/libstb-hal"

SRC_URI = "git://github.com/nx111/oscam.git;protocol=https \
"

SRCREV = "f07272ae3d8ee2abb8ac884f038a1e7497e2c543"

S = "${WORKDIR}/git"
	
INHIBIT_PACKAGE_STRIP = "1"

inherit cmake systemd

do_configure_append_coolstream-hd2 () {
	if [ ${BOXTYPE} = "kronos" ];then
		sed -i "s|^#define MAX_COOL_DMX.*|#define MAX_COOL_DMX 3|" ${S}/module-dvbapi-coolapi.c
	fi
}

EXTRA_OECMAKE = " \
		 -DCS_SVN_VERSION="${SRCPV}" \
		 -DDEFAULT_CS_CONFDIR="/etc/neutrino/config" \
		 -DWEBIF=1 \
		 -DHAVEDVBAPI=1 \
		 -DUSE_LIBCRYPTO=1 \
		 -DUSE_LIBUSB=1 \
		 -DUSE_PCSC=1 \ 
		 -DUSE_STAPI=1 \
		 -DREADER_IRDETO=1 \
		 -DREADER_NAGRA=1 \
		 -DREADER_SECA=1 \
		 -DREADER_CRYPTOWORKS=1 \
		 -DREADER_CONAX=1 \
		 -DREADER_VIACCESS=1 \
		 -DREADER_VIDEOGUARD=1 \
		 -DMODULE_CAMD35=1 \
		 -DMODULE_GBOX=1 \
		 -DMODULE_CCCAM=1 \
		 -DMODULE_CCCSHARE=1 \
		 -DCARDREADER_SC8IN1=1 \
		 -DCARDREADER_SMARGO=1 \
"

EXTRA_OECMAKE_append_coolstream-hd1 += "-DOSCAM_SYSTEM_NAME=Coolstream \
"

EXTRA_OECMAKE_append_coolstream-hd2 += "-DOSCAM_SYSTEM_NAME=CST2 \
"

EXTRA_OECMAKE_append_hd51 += "-DOSCAM_SYSTEM_NAME=NONE \
"

do_install () {
	install -d ${D}/usr/bin ${D}/etc/neutrino/bin
	install -D -m 755 ${WORKDIR}/build/oscam ${D}/etc/neutrino/bin/oscam
	rm -rf ${D}/usr
}

INSANE_SKIP_${PN} = "already-stripped"