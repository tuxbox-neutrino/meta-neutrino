SUMMARY = "LCD4Linux is a small program that grabs information from the kernel and some subsystems and displays it on an external liquid crystal display."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "libusb1 libusb-compat gd ncurses readline jpeg dbus-glib sqlite3"
RDEPENDS_${PN} = "jpeg samsunglcd4linux"

SRC_URI = "git://github.com/TangoCash/lcd4linux.git;protocol=https \
	file://lcd4linux.service \
	file://0001-Makefile.am-fix-libtool-path.patch \
	file://0002-gitignore-add-some-build-stuff.patch \
	file://0003-drv_TEW673GRU_c-fix-build.patch \
	file://0004-INSTALL-update.patch \
"

include lcd4linux.inc

SRCREV = "${AUTOREV}"
PV = "${SRCPV}"
S =  "${WORKDIR}/git"

addtask setlibtool before do_configure after do_patch

do_setlibtool() {
    sed -i "s#LIBTOOL=libtool#LIBTOOL=\${STAGING_BINDIR_CROSS}\/\${HOST_SYS}-libtool#" ${S}/Makefile.am
}

do_setlibtool_arm (){
    sed -i "s#LIBTOOL=libtool#LIBTOOL=\${STAGING_BINDIR_CROSS}\/arm-oe-linux-gnueabi-libtool#" ${S}/Makefile.am
}

do_setlibtool_aarch64 (){
    perl -pi -e "s#LIBTOOL=libtool#LIBTOOL=\${STAGING_BINDIR_CROSS}\/aarch64-oe-linux-libtool#" ${S}/Makefile.am
}

EXTRE_OECONF += "\
    --with-ncurses=${STAGING_LIBDIR}/..\
    --with-drivers='DPF,SamsungSPF,VUSOLO4K,PNG' \
    --with-plugins='all,!dbus,!mpris_dbus,!asterisk,!isdn,!pop3,!ppp,!seti,!huawei,!imon,!kvv,!sample,!w1retap,!wireless,!xmms,!gps,!mpd,!mysql,!qnaplog,!iconv,!apm,!raspi,!dvb,!hddtemp,!python' \
    --without-x \
"

LDFLAGS:append += "-lcurses"

inherit autotools systemd gettext pkgconfig

SYSTEMD_SERVICE_${PN} = "lcd4linux.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0755 ${WORKDIR}/lcd4linux.service ${D}${systemd_unitdir}/system
    rm -rf ${D}${sysconfdir}
}

FILES_${PN} += "/usr"
