DESCRIPTION = "A small C library that is supposed to make it easy to run an HTTP server as part of another application"
HOMEPAGE = "http://www.gnu.org/software/libmicrohttpd/"
LICENSE = "LGPL-2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=57d09285feac8a64efa878e692b14f36"
SECTION = "net"

DEPENDS = "file"

SRC_URI = "${GNU_MIRROR}/libmicrohttpd/${BPN}-${PV}.tar.gz \
"
SRC_URI[md5sum] = "c7e230e9b899282b43d9a9faa41e893d"
SRC_URI[sha256sum] = "e8f445e85faf727b89e9f9590daea4473ae00ead38b237cf1eda55172b89b182"

inherit autotools lib_package pkgconfig gettext

CFLAGS += "-pthread -D_REENTRANT"

EXTRA_OECONF += "--disable-static --with-gnutls=${STAGING_LIBDIR}/../"

PACKAGECONFIG ?= "curl https"
PACKAGECONFIG:append_class-target = "\
        ${@bb.utils.filter('DISTRO_FEATURES', 'largefile', d)} \
"
PACKAGECONFIG[largefile] = "--enable-largefile,--disable-largefile,,"
PACKAGECONFIG[curl] = "--enable-curl,--disable-curl,curl,"
PACKAGECONFIG[https] = "--enable-https,--disable-https,libgcrypt gnutls,"

do_compile:append() {
    sed -i s:-L${STAGING_LIBDIR}::g libmicrohttpd.pc
}
