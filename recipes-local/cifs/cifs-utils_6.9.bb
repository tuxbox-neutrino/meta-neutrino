DESCRIPTION = "A a package of utilities for doing and managing mounts of the Linux CIFS filesystem."
HOMEPAGE = "http://wiki.samba.org/index.php/LinuxCIFS_utils"
SECTION = "otherosfs"
LICENSE = "GPLv3 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "6.9"

SRCREV = "9a8c21ad9e4510a83a3a41f7a04f763a4fe9ec09"
SRC_URI = "git://git.samba.org/cifs-utils.git"

S = "${WORKDIR}/git"
DEPENDS += "libtalloc"

PACKAGECONFIG ??= ""
PACKAGECONFIG[cap] = "--with-libcap,--without-libcap,libcap"
# when enabled, it creates ${bindir}/cifscreds and --ignore-fail-on-non-empty in do_install:append is needed
PACKAGECONFIG[cifscreds] = "--enable-cifscreds,--disable-cifscreds,keyutils"
# when enabled, it creates ${sbindir}/cifs.upcall and --ignore-fail-on-non-empty in do_install:append is needed
PACKAGECONFIG[cifsupcall] = "--enable-cifsupcall,--disable-cifsupcall,krb5 libtalloc keyutils"
PACKAGECONFIG[cifsidmap] = "--enable-cifsidmap,--disable-cifsidmap,keyutils samba"
PACKAGECONFIG[cifsacl] = "--enable-cifsacl,--disable-cifsacl,samba"
PACKAGECONFIG[pam] = "--enable-pam --with-pamdir=${base_libdir}/security,--disable-pam,libpam keyutils"

inherit autotools pkgconfig

do_install:append() {
    # Remove empty /usr/bin and /usr/sbin directories since the mount helper
    # is installed to /sbin
    rmdir --ignore-fail-on-non-empty ${D}${bindir} ${D}${sbindir}
}

FILES_${PN} += "${base_libdir}/security"
FILES_${PN}-dbg += "${base_libdir}/security/.debug"
RRECOMMENDS_${PN} = "kernel-module-cifs"
