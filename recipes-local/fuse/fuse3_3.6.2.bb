SUMMARY = "Implementation of a fully functional filesystem in a userspace program"
DESCRIPTION = "FUSE (Filesystem in Userspace) is a simple interface for userspace \
               programs to export a virtual filesystem to the Linux kernel. FUSE \
               also aims to provide a secure method for non privileged users to \
               create and mount their own filesystem implementations. \
              "
HOMEPAGE = "https://github.com/libfuse/libfuse"
SECTION = "libs"
LICENSE = "GPLv2 & LGPLv2"
LIC_FILES_CHKSUM = "file://GPL2.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://LGPL2.txt;md5=4fbd65380cdd255951079008b364516c \
                    file://LICENSE;md5=a55c12a2d7d742ecb41ca9ae0a6ddc66"

SRC_URI = "https://github.com/libfuse/libfuse/releases/download/fuse-${PV}/fuse-${PV}.tar.xz \
"
SRC_URI[md5sum] = "1798945aa69434286723b9999a141c7a"
SRC_URI[sha256sum] = "f45869427575e1e59ab743a67deb57addbf2cb8f9ce431199dbd40ddab71f281"

S = "${WORKDIR}/fuse-${PV}"

UPSTREAM_CHECK_URI = "https://github.com/libfuse/libfuse/releases"
UPSTREAM_CHECK_REGEX = "fuse\-(?P<pver>3(\.\d+)+).tar.xz"

inherit meson pkgconfig

DEPENDS = "udev"

PACKAGES =+ "fuse3-utils"

RPROVIDES_${PN}-dbg += "fuse3-utils-dbg"

RRECOMMENDS_${PN}_class-target = "kernel-module-fuse fuse3-utils"

FILES_${PN} += "${libdir}/libfuse3.so.*"
FILES_${PN}-dev += "${libdir}/libfuse3*.la"

EXTRA_OEMESON += " \
     -Dexamples=false \
"

# Forbid auto-renaming to libfuse3-utils
FILES_fuse3-utils = "${bindir} ${base_sbindir}"
DEBIAN_NOAUTONAME_fuse3-utils = "1"
DEBIAN_NOAUTONAME_${PN}-dbg = "1"

do_install:append() {
    rm -rf ${D}${base_prefix}/dev
}
