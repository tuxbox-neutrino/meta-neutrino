SUMMARY = "An HTTP and WebDAV client library with a C interface"
HOMEPAGE = "http://www.webdav.org/neon/"
SECTION = "libs"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://src/COPYING.LIB;md5=f30a9716ef3762e3467a2f62bf790f0a \
                    file://src/ne_utils.h;beginline=1;endline=20;md5=2caca609538eddaa6f6adf120a218037"

SRC_URI = "http://www.webdav.org/${BPN}/${BPN}-${PV}.tar.gz \
           file://pkgconfig.patch \
          "

SRC_URI[md5sum] = "e28d77bf14032d7f5046b3930704ef41"
SRC_URI[sha256sum] = "db0bd8cdec329b48f53a6f00199c92d5ba40b0f015b153718d1b15d3d967fbca"

inherit autotools binconfig-disabled lib_package pkgconfig

# Enable gnutls or openssl, not both
PACKAGECONFIG ?= "expat gnutls libproxy webdav zlib"
PACKAGECONFIG_class-native = "expat gnutls webdav zlib"

PACKAGECONFIG[expat] = "--with-expat,--without-expat,expat"
PACKAGECONFIG[gnutls] = "--with-ssl=gnutls,,gnutls"
PACKAGECONFIG[gssapi] = "--with-gssapi,--without-gssapi,krb5"
PACKAGECONFIG[libproxy] = "--with-libproxy,--without-libproxy,libproxy"
PACKAGECONFIG[libxml2] = "--with-libxml2,--without-libxml2,libxml2"
PACKAGECONFIG[openssl] = "--with-ssl=openssl,,openssl"
PACKAGECONFIG[webdav] = "--enable-webdav,--disable-webdav,"
PACKAGECONFIG[zlib] = "--with-zlib,--without-zlib,zlib"

EXTRA_OECONF += "--enable-shared"

do_compile:append() {
	oe_runmake -C test
}

BINCONFIG = "${bindir}/neon-config"

BBCLASSEXTEND = "native"
