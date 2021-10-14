DESCRIPTION = "CTDB is a cluster implementation of the TDB database \
used by Samba and other projects to store temporary data. If an \
application is already using TDB for temporary data it is very easy \
to convert that application to be cluster aware and use CTDB instead."
DESCRIPTION = "CTDB is a cluster implementation of the TDB database \
used by Samba and other projects to store temporary data. If an \
application is already using TDB for temporary data it is very easy \
to convert that application to be cluster aware and use CTDB instead."
HOMEPAGE = "https://ctdb.samba.org/"
LICENSE = "GPL-2.0+ & LGPL-3.0+ & GPL-3.0+"

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://${COMMON_LICENSE_DIR}/LGPL-3.0;md5=bfccfe952269fff2b407dd11f2f3083b \
                    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
                    "

SRC_URI = "https://ftp.samba.org/pub/${PN}/${BP}.tar.gz \
           file://01-support-cross-compile-for-linux-os.patch \
           file://02-link-rep_snprintf-for-ltdbtool.patch \
          "

SRC_URI[md5sum] = "d0cd91726ff4ca2229e1b21859c94717"
SRC_URI[sha256sum] = "d5bf3f674cae986bb6178b1db215a703ac94adc5f75fadfdcff63dcbb5e98ab5"

inherit autotools-brokensep pkgconfig systemd

PARALLEL_MAKE = ""

DEPENDS += "popt libtevent libtalloc"
RDEPENDS_${PN} += "bash"

do_configure() {
    oe_runconf
}

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/config/ctdb.service ${D}${systemd_unitdir}/system
    sed -i -e 's,/usr/sbin/,${sbindir}/,' ${D}${systemd_unitdir}/system/ctdb.service
}

SYSTEMD_SERVICE_${PN} = "ctdb.service"

FILES_${PN} += "/run"
