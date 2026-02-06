# for some common variables, e.g. plugin paths
include ../neutrino/neutrino-common-vars.inc
include ../neutrino/neutrino-build-config.inc

inherit neutrino-boxmap

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

## PLUGIN_NAME is defined by recipe name
PLUGIN_NAME = "${PN}"

## These variables should be defined in plugin recipe and will be override if no description was defined inside plugin recipe
SECTION = "neutrino-plugin"
DESCRIPTION ?= "Neutrino plugin: ${PLUGIN_NAME}"
MAINTAINER ?= "${FLAVOR}-developers"
HOMEPAGE ?= "https://github.com/tuxbox-neutrino"

## summary contains the plugin description
SUMMARY = "${DESCRIPTION}"

DEPENDS = "freetype ffmpeg zlib libxml2 virtual/libiconv openssl libpng curl giflib libjpeg-turbo"

PROVIDES = "virtual/neutrino-plugin-${PLUGIN_NAME}"
RPROVIDES_${PN} = "virtual/neutrino-plugin-${PLUGIN_NAME}"

# PV = "${SRCPV}"
PR = "r2"
SRCREV = "${AUTOREV}"

inherit gitpkgv
# We have more as one repository without tags, so we
# suppress warnings for that.
GITPKGVTAG_NO_WARN_ON_NO_TAG = "1"

# we need an independent file stack for the build, some templates for configure.ac and Makefile.am
# The plugin source and other required sources itself will be defined inside the file: neutrino-plugins-<provider>-env.inc
SRC_URI = " \
	file://acinclude.m4 \
	file://autogen.sh \
	file://COPYING \
	\
	file://configure.ac \
	file://Makefile.am \
"

S = "${WORKDIR}"

ALLOW_EMPTY_neutrino-plugins = "1"

inherit autotools pkgconfig gettext

EXTRA_OECONF = " \
	--enable-maintainer-mode \
	${NEUTRINO_OECONF_BASE} \
	--with-boxtype=${NEUTRINO_BOXTYPE} \
	--with-boxmodel=${NEUTRINO_BOXMODEL} \
	\
	${NEUTRINO_OECONF_DIRS_COMMON} \
	--with-datadir=${N_DATADIR} \
"

N_CFLAGS = "-Wall -W -Wshadow -g -O2 -funsigned-char -I${STAGING_INCDIR}/freetype2 -fcommon"
N_CXXFLAGS = "${N_CFLAGS}"
N_LDFLAGS += "-Wl,--hash-style=gnu -Wl,-rpath-link,${STAGING_DIR_HOST}${libdir},-lfreetype -lcrypto -lssl -lpng -lcurl -lz -lpthread"

do_compile () {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake CFLAGS="${N_CFLAGS}" CXXFLAGS="${N_CXXFLAGS}" LDFLAGS="${N_LDFLAGS}"
}

do_install:append() {
	rm -rf ${D}/${N_PLUGIN_DIR}/*.la
	rm -rf ${D}/${datadir}/tuxbox/neutrino/luaplugins
}

FILES:${PN} = " \
	${N_PREFIX} \
	${sysconfdir} \
"

FILES:${PN}-dbg += " \
	${N_PLUGIN_DIR}/.debug \
"
