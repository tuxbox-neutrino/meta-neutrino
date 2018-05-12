SUMMARY = "Secure and configurable FTP server"
SECTION = "console/network"
HOMEPAGE = "http://www.proftpd.org"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=fb0d1484d11915fa88a6a7702f1dc184"

SRC_URI = "ftp://ftp.proftpd.org/distrib/source/${BPN}-${PV}.tar.gz \
           file://basic.conf.patch \
           file://proftpd-basic.init \
           file://default \
           file://close-RequireValidShell-check.patch \
           file://contrib.patch  \
           file://build_fixup.patch \
           file://proftpd.service \
	   file://ftpusers \
	   file://proftpd_banner \
	   file://proftpd.conf \
	   file://ftp.pam \
           "

SRC_URI[md5sum] = "13270911c42aac842435f18205546a1b"
SRC_URI[sha256sum] = "91ef74b143495d5ff97c4d4770c6804072a8c8eb1ad1ecc8cc541b40e152ecaf"

RDEPENDS_${PN} = "pam-plugin-listfile"

inherit autotools-brokensep useradd systemd

PACKAGECONFIG ??= "pam shadow largefile"
PACKAGECONFIG += " ${@bb.utils.contains('DISTRO_FEATURES', 'ipv6', 'ipv6', '', d)}"
PACKAGECONFIG += " ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam', '', d)}"

PACKAGECONFIG[curses] = "--enable-curses --enable-ncurses, --disable-curses --disable-ncurses, ncurses"
PACKAGECONFIG[openssl] = "--enable-openssl, --disable-openssl, openssl, openssl"
PACKAGECONFIG[pam] = "--enable-auth-pam, --disable-auth-pam, libpam, libpam"
PACKAGECONFIG[ipv6] = "--enable-ipv6, --disable-ipv6"
PACKAGECONFIG[shadow] = "--enable-shadow, --disable-shadow"
PACKAGECONFIG[pcre] = "--enable-pcre, --disable-pcre, libpcre "

# enable POSIX.1e capabilities
PACKAGECONFIG[cap] = "--enable-cap, --disable-cap, libcap, libcap"

#enable support for POSIX ACLs
PACKAGECONFIG[acl] = "--enable-facl, --disable-facl"

#enable proftpd controls via ftpdct
PACKAGECONFIG[ctrls] = "--enable-ctrls, --disable-crtls"

#prevent proftpd from using its bundled getopt implementation.
PACKAGECONFIG[getopt] = "--with-getopt, --without-getopt"

#do not strip debugging symbols from installed code
PACKAGECONFIG[strip] = "--enable-strip, --disable-strip"

#enable SIA authentication support (Tru64)
PACKAGECONFIG[sia] = "--enable-sia, --disable-sia"
PACKAGECONFIG[sendfile] = "-enable-sendfile, --disable-sendfile"

#enable Native Language Support (NLS)
PACKAGECONFIG[nls] = "--enable-nls, --disable-nls"

#add mod_dso to core modules
PACKAGECONFIG[dso] = "--enable-dso, --disable-dso"
PACKAGECONFIG[largefile] = "--enable-largefile, --disable-largefile"

#omit mod_auth_file from core modules
PACKAGECONFIG[auth] = "--enable-auth-file, --disable-auth-file"


# proftpd uses libltdl which currently makes configuring using
# autotools.bbclass a pain...
do_configure () {
    oe_runconf
}

FTPUSER = "ftp"
FTPGROUP = "ftp"

do_install () {
    oe_runmake DESTDIR=${D} install
    rmdir ${D}${libdir}/proftpd ${D}${datadir}/locale
    [ -d ${D}${libexecdir} ] && rmdir ${D}${libexecdir}
    sed -i '/ *User[ \t]*/s/ftp/${FTPUSER}/' ${D}${sysconfdir}/proftpd.conf
    sed -i '/ *Group[ \t]*/s/ftp/${FTPGROUP}/' ${D}${sysconfdir}/proftpd.conf
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/proftpd-basic.init ${D}${sysconfdir}/init.d/proftpd
    sed -i 's!/usr/sbin/!${sbindir}/!g' ${D}${sysconfdir}/init.d/proftpd
    sed -i 's!/etc/!${sysconfdir}/!g' ${D}${sysconfdir}/init.d/proftpd
    sed -i 's!/var/!${localstatedir}/!g' ${D}${sysconfdir}/init.d/proftpd
    sed -i 's!^PATH=.*!PATH=${base_sbindir}:${base_bindir}:${sbindir}:${bindir}!' ${D}${sysconfdir}/init.d/proftpd

    install -d ${D}${sysconfdir}/default
    install -m 0755 ${WORKDIR}/default ${D}${sysconfdir}/default/proftpd
    install -m 644 ${WORKDIR}/ftpusers ${D}${sysconfdir}/ftpusers
    install -m 644 ${WORKDIR}/proftpd.conf ${D}${sysconfdir}/proftpd.conf
    install -m 644 ${WORKDIR}/proftpd_banner ${D}${sysconfdir}/welcome.msg
    install -d ${D}${sysconfdir}/pam.d/
    cp ${WORKDIR}/ftp.pam ${D}${sysconfdir}/pam.d/ftp
    sed -i "s:/lib/security:${base_libdir}/security:" ${D}${sysconfdir}/pam.d/ftp

    install -d ${D}/${systemd_unitdir}/system
    install -m 644 ${WORKDIR}/proftpd.service ${D}/${systemd_unitdir}/system
    sed -e 's,@BASE_SBINDIR@,${base_sbindir},g' \
        -e 's,@SYSCONFDIR@,${sysconfdir},g' \
        -e 's,@SBINDIR@,${sbindir},g' \
        -i ${D}${systemd_unitdir}/system/*.service
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "proftpd.service"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system ${FTPGROUP}"
USERADD_PARAM_${PN} = "--system -g ${FTPGROUP} --home-dir /var/lib/${FTPUSER} --no-create-home \
                       --shell /bin/false ${FTPUSER}"

FILES_${PN} += "/home/${FTPUSER}"

RDEPENDS_${PN} += "perl"
