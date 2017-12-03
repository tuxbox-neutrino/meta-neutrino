DESCRIPTION = "tuxbox plugins, ported to neutrino-hd"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://tuxcom/tuxcom.c;beginline=10;endline=24;md5=f08c0a8fadd6ea7b914992e4bd4b7685"


DEPENDS = "freetype ffmpeg zlib libxml2 virtual/libiconv openssl libpng curl giflib libjpeg-turbo"

SRCREV = "${AUTOREV}"
PV = "8"

SRC_URI = "git://github.com/neutrino-hd/neutrino-hd-plugins.git;branch=master;protocol=https \
"

S = "${WORKDIR}/git"

ALLOW_EMPTY_neutrino-plugins = "1"

inherit autotools pkgconfig


EXTRA_OECONF += " \
	--enable-maintainer-mode \
	--with-target=native \
	--with-plugindir=/var/tuxbox/plugins \
	--with-boxtype=armbox \
"

EXTRA_OECONF += "--with-configdir=/etc/neutrino/config"

N_CFLAGS = "-Wall -W -Wshadow -g -O2 -funsigned-char -I${STAGING_INCDIR}/freetype2"
N_CXXFLAGS = "${N_CFLAGS}"
N_LDFLAGS += "-Wl,--hash-style=gnu -Wl,-rpath-link,${STAGING_DIR_HOST}${libdir},-lfreetype -lcrypto -lssl -lpng -lcurl -lz"

do_compile () {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake CFLAGS="${N_CFLAGS}" CXXFLAGS="${N_CXXFLAGS}" LDFLAGS="${N_LDFLAGS}" SUBDIRS="${PLUGIN_INSTALL}"
}

do_install () {
	for i in ${PLUGIN_INSTALL}; do
		oe_runmake install SUBDIRS="$i" DESTDIR=${D}
	done
}			

do_install_append() {
	rm -f ${D}/var/tuxbox/plugins/*.la
}

FILES_${PN}-dbg += "/var/tuxbox/plugins/.debug"

SRC_URI[md5sum] = "f04cf2dddc22af9f12685f4d4dda0067"
SRC_URI[sha256sum] = "f3ad02f2e43afca3da474bfeccd70808ca9651858893eff0b90891067284b0b8"
