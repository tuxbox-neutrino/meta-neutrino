
PROVIDES = "virtual/neutrino"
RPROVIDES_${PN} = "neutrino-mp virtual/neutrino"

RCONFLICTS_${PN} = "neutrino-mp"
RREPLACES_${PN} = "neutrino-mp"

CONFLICTS_${PN} = "neutrino-mp"
REPLACES_${PN} = "neutrino-mp"


inherit autotools pkgconfig systemd ccache gitpkgv neutrino-boxmap
include neutrino-build-config.inc

DEPENDS += " \
	curl \
	ffmpeg \
	flac \
	freetype \
	freetype-native \
	fribidi \
	gettext \
	giflib \
	glib-2.0 \
	libass \
	libbluray \
	libdvbsi++ \
	libid3tag \
	libjpeg-turbo \
	pugixml \
	libpng \
	libsigc++-2.0\
	virtual/lua \
	luaposix \
	openssl \
	openthreads \
	virtual/libstb-hal \
	virtual/libiconv \
	fontconfig \
	libpng \
	tremor \
	zlib \
"

RDEPENDS_${PN} = "streamripper virtual/libstb-hal virtual/lua luaposix lua-expat ffmpeg"
# `grab` is optional, but enables screenshot/GLCD capture features in Neutrino.
RRECOMMENDS:${PN} += "grab"

S = "${WORKDIR}/git"

# Use modern override syntax so systemd.bbclass can reliably generate
# package scripts/presets on kirkstone and newer bitbake versions.
SYSTEMD_SERVICE:${PN} = "neutrino.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

YT_DEV_KEY ?= "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

TMDB_DEV_KEY ?= "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

SHOUTCAST_DEV_KEY ?= "XXXXXXXXXXXXXXXX"

OMDB_API_KEY ?= "XXXXXXXX"

WEATHER_DEV_KEY ?= "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

# to find in neutrino-common-vars.inc
#N_PREFIX = "/usr" 

N_EXEC_PREFIX = "${N_PREFIX}/bin"

EXTRA_OECONF = " \
	--enable-maintainer-mode \
	--enable-silent-rules \
	--enable-upnp \
	--enable-mdev \
	--enable-fribidi \
	--enable-pugixml \
	--enable-giflib \
	--with-tremor \
	--enable-flac \
	--enable-ffmpegdec \
	--enable-lua \
	${NEUTRINO_OECONF_BASE} \
	--exec_prefix=${N_EXEC_PREFIX} \
	--includedir=${N_PREFIX}/include \
	\
	--with-stb-hal-includes=${STAGING_INCDIR}/libstb-hal \
	\
	${NEUTRINO_OECONF_DIRS_COMMON} \
	--with-datadir=${N_DATADIR} \
	\
	--with-tmdb-api-key="${TMDB_DEV_KEY}" \
	--with-omdb-api-key="${OMDB_API_KEY}" \
	--with-youtube-api-key="${YT_DEV_KEY}" \
	--with-shoutcast-dev-id="${SHOUTCAST_DEV_KEY}" \
	--with-weather-api-key="${WEATHER_DEV_KEY}" \
	--with-weather-api-version="${WEATHER_API_VERSION}" \
	\
	--enable-pip \
	--enable-lcd4linux \
"

EXTRA_OECONF:remove:qemux86 = " --enable-pip"
EXTRA_OECONF:remove:qemux86-64 = " --enable-pip"

# EXTRA_OECONF += " \
# 	--enable-dynamicdemux \
# "

EXTRA_OEMAKE += "'ARFLAGS=rc'"

# VERSION_FILE = "${N_PREFIX}/.version"

N_CFLAGS = "-Wall -Wextra -Wshadow -Wno-psabi ${DEBUG_FLAGS} ${FULL_OPTIMIZATION} -fno-strict-aliasing -rdynamic -DENABLE_FREESATEPG -DNEW_LIBCURL \
-I${S}/lib/libcoolstream2 -I${STAGING_INCDIR}/freetype2 -I${STAGING_INCDIR}/sigc++-2.0 -I${STAGING_INCDIR}/glib-2.0 -I${STAGING_LIBDIR}/glib-2.0/include -I${STAGING_INCDIR}/luajit-2.1 \
"

N_CXXFLAGS = "${N_CFLAGS}"

N_LDFLAGS += "-Wl,--hash-style=gnu -Wl,-rpath-link, ${STAGING_LIBDIR} -lcurl -ldl -lass"
N_CPPFLAGS = "-DDYNAMIC_LUAPOSIX -D__KERNEL_STRICT_NAMES -D__STDC_CONSTANT_MACROS"
CPPFLAGS = "${N_CPPFLAGS}"

do_configure:prepend() {
        ln -sf ${B}/src/gui/version.h ${S}/src/gui/
}


do_compile () {
	# unset CFLAGS CXXFLAGS LDFLAGS
	oe_runmake CFLAGS="${N_CFLAGS}" CXXFLAGS="${N_CXXFLAGS}" LDFLAGS="${N_LDFLAGS}"
}


do_install:prepend () {
	install -d ${D}${sysconfdir}/network
	install -d ${D}${systemd_unitdir}/system
	install -d ${D}${bindir}
	install -d ${D}${N_CONFIG_DIR}
	install -d ${D}${sysconfdir}/neutrino/plugins	
	install -m 0644 ${WORKDIR}/neutrino.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${WORKDIR}/neutrino-log.service  ${D}${systemd_unitdir}/system
	install -m 0755 ${WORKDIR}/neutrino.sh_tuxbox ${D}${bindir}/neutrino.sh   
	install -m 0755 ${WORKDIR}/streamripper.sh ${D}${bindir}/streamripper.sh
	install -m 755 ${WORKDIR}/pre-wlan0.sh ${D}${sysconfdir}/network/
	install -m 755 ${WORKDIR}/post-wlan0.sh ${D}${sysconfdir}/network/
	install -m 644 ${WORKDIR}/timezone.xml ${D}${sysconfdir}/timezone.xml
	install -m 755 ${WORKDIR}/tobackup.conf ${D}${sysconfdir}/neutrino/config
	install -d ${D}${localstatedir}/cache
	install -d ${D}/lib/mdev/fs
	install -m 755 ${WORKDIR}/mount.mdev ${D}/lib/mdev/fs/mount

	# why this ???
	touch ${D}/usr/bin/enigma2
}

do_install:append() {
	install -d ${D}${sysconfdir}/neutrino/bin ${D}${sysconfdir}/neutrino/plugins/luaplugins
	install -d ${D}${sysconfdir}/neutrino/plugins/webradio ${D}${sysconfdir}/neutrino/plugins/webtv

	# We don't install the update.urls file with the neutrino target.
	# The content is inserted during the creation of the image (See: neutrino-image-base.inc).
	# touch ${D}/etc/update.urls
}

FILES:${PN} += "\
	${sysconfdir} \
	${datadir} \
	/share \
	/lib \
"

systemd_prerm() {
}

systemd_postinst() {
OPTS=""

if [ -n "$D" ]; then
    OPTS="--root=$D"
fi

if type systemctl >/dev/null 2>/dev/null; then
	if [ -z "$D" ]; then
		systemctl daemon-reload
	fi

	systemctl $OPTS ${SYSTEMD_AUTO_ENABLE} ${SYSTEMD_SERVICE}
fi
}


pkg_postinst_ontarget_${PN} () {
	update-alternatives --install /bin/backup.sh backup.sh /usr/bin/backup.sh 100
	update-alternatives --install /bin/install.sh install.sh /usr/bin/install.sh 100
	update-alternatives --install /bin/restore.sh restore.sh /usr/bin/restore.sh 100
	# pic2m2v is only available on platforms that use "real" libstb-hal
	if which pic2m2v >/dev/null 2>&1; then
		# neutrino icon path
		I=/usr/share/tuxbox/neutrino/icons
		pic2m2v $I/mp3.jpg $I/radiomode.jpg $I/scan.jpg $I/shutdown.jpg $I/start.jpg
	fi
	touch /tmp/.restart_neutrino
}
