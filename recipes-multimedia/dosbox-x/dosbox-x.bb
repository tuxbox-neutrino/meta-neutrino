SUMMARY = "DOSBox-X fork of the DOSBox project"
HOMEPAGE = "https://dosbox-x.com/" 

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b5d36d7c1f35e9597b9843b9bedb6667" 

DEPENDS = " \
    libsdl2 \
    libsdl2-net \
    libpcap \
    libpng \
    alsa-lib \
    fluidsynth \
"

inherit autotools-brokensep pkgconfig dos2unix

SRC_URI = " \
    git://github.com/joncampbell123/dosbox-x.git \
    file://0001-use-pkgconfig-to-find-sdl2.patch \
    file://0002-Enable-unaligned-memory-based-on-recipe-s-suggestion.patch \
    file://0003-Treat-all-arm-hosts-as-armv7.patch \
    file://0004-Fix-build-with-Werror-format-security.patch \
"
SRCREV = "9e9d7acd934862208f20479cc8069404fa06fa03"
PV = "0.83.8"
S = "${WORKDIR}/git"

EXTRA_OECONF = " \
    --disable-sdl \
    --disable-sdltest \
    --enable-sdl2 \
    --disable-sdl2test \
    --disable-alsatest \
"

do_install:append() {
	rm -r ${D}${datadir}/icons
}

FILES_${PN} += "${datadir}/metainfo"
