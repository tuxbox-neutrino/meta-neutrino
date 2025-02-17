SUMMARY = "Web-based administration interface"
HOMEPAGE = "http://www.webmin.com"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENCE;md5=0a6446108c96d0819d21e40b48109507"

# for some common variables, e.g. plugin paths
include ../../recipes-neutrino/neutrino/neutrino-common-vars.inc

PR = "r1"

RM_WORK_EXCLUDE += "${PN}"

WEBMIN_THEME ?= "authentic-theme"

FILESEXTRAPATHS_prepend := "${THISDIR}/${WEBMIN_THEME}:"

# webmin
SRC_URI = " \
		${SOURCEFORGE_MIRROR}/webadmin/webmin-${PV}.tar.gz \
\
		file://0001-add-webmin-recipe.patch \
		file://0002-net-generic.patch \
		file://0003-add-webmin-recipe.patch \
		file://0004-add-webmin-recipe.patch \
		file://0005-nfs-export-remove-nfsd-check.patch \
		file://0006-Add-excludefs-config-option-to-mount-module.patch;apply=no \
		file://0007-Upstream-status-Inappropriate-configuration.patch \
		file://0008-Upstream-status-Inappropriate-configuration.patch \
		file://0009-add-webmin-recipe.patch \
		file://0010-original-patch-media-tomb.patch.patch \
		file://0011-ajaxterm-ajaxterm-qweb.py-fix-hardcode-of-python2.3.patch;apply=no \
		file://0012-Adjust-Mysql-config-defaults.patch \
		file://0013-webmin-use-correct-path-path-to-opkg.patch \
\
		file://exports_config \
		file://proftpd_config \
		file://samba_config \
		file://setup.sh \
		file://smart_config \
		file://webmin \
		file://webmin.service \
"

# authentic theme
SRC_URI += " \
		file://background_content.png  \
		file://logo.png  \
		file://logo_welcome.png  \
"

# only for context)
SRC_URI += " \
		file://disable-version-check.patch;apply=no \
"

RDEPENDS-${PN}-module += "perl"

SRC_URI[sha256sum] = "4f1c467aeda0ded546e69f332c768665b6a0537d6a69e609221f9465121f775e"

inherit perlnative systemd

do_configure() {
    # Remove binaries and plugins for other platforms
    rm -rf acl/Authen-SolarisRBAC-0.1*
    rm -rf format bsdexports hpuxexports sgiexports
    rm -rf zones rbac smf ipfw ipfilter dfsadmin
    rm -f mount/freebsd-mounts* mount/netbsd-mounts*
    rm -f mount/openbsd-mounts* mount/macos-mounts*

    # Remove some plugins for the moment
    rm -rf lilo frox wuftpd telnet pserver cpan shorewall webalizer cfengine fsdump pap
    rm -rf majordomo fetchmail sendmail mailboxes procmail filter mailcap dovecot exim spam qmailadmin postfix
    rm -rf stunnel squid sarg pptp-client pptp-server jabber openslp sentry cluster-* vgetty burner heartbeat

    # Adjust configs
    [ -f init/config-debian-linux ] && mv init/config-debian-linux init/config-generic-linux
    sed -i "s/shutdown_command=.*/shutdown_command=poweroff/" init/config-generic-linux
    echo "exclude=bootmisc.sh,single,halt,reboot,hostname.sh,modutils.sh,mountall.sh,mountnfs.sh,networking,populate-volatile.sh,rmnologin.sh,save-rtc.sh,umountfs,umountnfs.sh,hwclock.sh,checkroot.sh,banner.sh,udev,udev-cache,devpts.sh,psplash.sh,sendsigs,fbsetup,bootlogd,stop-bootlogd,sysfs.sh,syslog,syslog.busybox,urandom,webmin,functions.initscripts,read-only-rootfs-hook.sh" >> init/config-generic-linux
    echo "excludefs=devpts,devtmpfs,usbdevfs,proc,tmpfs,sysfs,debugfs" >> mount/config-generic-linux

    [ -f exports/config-debian-linux ] && mv exports/config-debian-linux exports/config-generic-linux
    sed -i "s/killall -HUP rpc.nfsd && //" exports/config-generic-linux
    sed -i "s/netstd_nfs/nfsserver/g" exports/config-generic-linux
    sed -i "s/os_support=.*/os_support=generic-linux/" net/module.info

    # Fix insane naming that causes problems at packaging time (must be done before deleting below)
    find . -name "*\**" | while read from
    do
        to=`echo "$from" | sed "s/*/ALL/"`
        mv "$from" "$to"
    done

    # Remove some other files we don't need
    find . -name "config-*" -a \! -name "config-generic-linux" -a \! -name "config-ALL-linux" -a \! -name "*.pl" -delete
    find . -regextype posix-extended -regex ".*/(openserver|aix|osf1|osf|openbsd|netbsd|freebsd|unixware|solaris|macos|irix|hpux|cygwin|windows)-lib\.pl" -delete
    rm -f webmin-gentoo-init webmin-caldera-init webmin-debian-pam webmin-pam

    # Don't need these at runtime (and we have our own setup script)
    rm -f setup.sh
    rm -f setup.pl

    # Use pidof for finding PIDs
    sed -i "s/find_pid_command=.*/find_pid_command=pidof NAME/" config-generic-linux
}

WEBMIN_LOGIN ?= "root"
WEBMIN_PASSWORD ?= "root"

do_install() {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/webmin
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/webmin.service ${D}${systemd_unitdir}/system
    sed -i -e 's,@SYSCONFDIR@,${sysconfdir},g' \
           ${D}${systemd_unitdir}/system/webmin.service

    install -d ${D}${localstatedir}
    install -d ${D}${localstatedir}/webmin

    install -d ${D}${libexecdir}/webmin
    cd ${S} || exit 1
    tar --no-same-owner --exclude='./patches' --exclude='./.pc' -cpf - . \
        | tar --no-same-owner -xpf - -C ${D}${libexecdir}/webmin

    rm -f ${D}${libexecdir}/webmin/webmin-init
    rm -f ${D}${libexecdir}/webmin/ajaxterm/ajaxterm/configure.initd.gentoo
    rm -rf ${D}${libexecdir}/webmin/patches

    # Run setup script
    export perl=perl
    export perl_runtime=${bindir}/perl
    export prefix=${D}
    export tempdir=${S}/install_tmp
    export wadir=${libexecdir}/webmin
    export config_dir=${sysconfdir}/webmin
    export var_dir=${localstatedir}/webmin
    export os_type=generic-linux
    export os_version=0
    export real_os_type="${DISTRO_NAME}"
    export real_os_version="${DISTRO_VERSION}"
    export port=10000
    export login=${WEBMIN_LOGIN}
    export password=${WEBMIN_PASSWORD}
    export ssl=0
    export atboot=1
    export no_pam=0
    mkdir -p $tempdir
    ${S}/../setup.sh

    # Ensure correct PERLLIB path
    sed -i -e 's#${D}##g' ${D}${sysconfdir}/webmin/start
}

do_install:append() {
	install -d ${D}${sysconfdir}/pam.d
	install -m 644 ${WORKDIR}/samba_config ${D}${sysconfdir}/webmin/samba/config
        install -m 644 ${WORKDIR}/exports_config ${D}${sysconfdir}/webmin/exports/config
        install -m 644 ${WORKDIR}/smart_config ${D}${sysconfdir}/webmin/smart-status/config
        install -m 644 ${WORKDIR}/proftpd_config ${D}${sysconfdir}/webmin/proftpd/config
        install -m 644 ${WORKDIR}/webmin ${D}${sysconfdir}/pam.d
	rm -rf ${D}/usr/libexec/webmin/shellinabox/cgi-bin # remove precompiled x86 binaries
	echo "theme_root=${WEBMIN_THEME}" >> ${D}${sysconfdir}/webmin/config
        echo "theme=${WEBMIN_THEME}" >> ${D}${sysconfdir}/webmin/config
        echo "preroot=${WEBMIN_THEME}" >> ${D}${sysconfdir}/webmin/miniserv.conf
	echo "preroot_root=${WEBMIN_THEME}" >> ${D}${sysconfdir}/webmin/miniserv.conf
        echo "lang=de" >> ${D}${sysconfdir}/webmin/config
        echo "show=*" > ${D}${sysconfdir}/webmin/system-status/root.acl
        echo "nowebminup=1" >> ${D}${sysconfdir}/webmin/config

        # presets for filemin
        install -d ${D}${sysconfdir}/webmin/filemin
	echo "config_portable_module_filemanager_records_for_server_pagination=250" >> ${D}${sysconfdir}/webmin/filemin/config
        echo "per_page=30" >> ${D}${sysconfdir}/webmin/filemin/config

        # presets for theme
        install -d ${D}${sysconfdir}/webmin/${WEBMIN_THEME}
        ln -sf ${N_ICONS_DIR}/start.jpg ${D}${sysconfdir}/webmin/${WEBMIN_THEME}/background_content.png
        install -m 644 ${WORKDIR}/logo.png ${D}${sysconfdir}/webmin/${WEBMIN_THEME}/logo.png
        install -m 644 ${WORKDIR}/logo_welcome.png ${D}${sysconfdir}/webmin/${WEBMIN_THEME}/logo_welcome.png
}

SYSTEMD_SERVICE_${PN} = "webmin.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

DEPENDS += "perl smartmontools procps mdadm"

# FIXME: some of this should be figured out automatically
RDEPENDS_${PN} += "perl perl-module-socket perl-module-exporter perl-module-exporter-heavy perl-module-carp perl-module-strict webmin-theme-${WEBMIN_THEME}"
RDEPENDS_${PN} += "perl-module-warnings perl-module-xsloader perl-module-posix perl-module-autoloader perl-module-digest-md5"
RDEPENDS_${PN} += "perl-module-fcntl perl-module-tie-hash perl-module-vars perl-module-time-local perl-module-config perl-module-constant perl-module-overloading"
RDEPENDS_${PN} += "perl-module-file-glob perl-module-file-copy perl-module-sdbm-file perl-module-feature smartmontools perl-module-encode-encoding perl-module-base"

PACKAGES_DYNAMIC += "webmin-module-* webmin-theme-*"
RDEPENDS_${PN} += "webmin-module-system-status libnet-ssleay-perl perl-module-file-path webmin-module-mount gnupg webmin-module-samba \
webmin-module-change-user webmin-module-net webmin-module-pam webmin-module-shell webmin-module-smart-status webmin-module-sshd webmin-module-status webmin-module-time \
webmin-module-system-status webmin-module-webmin webmin-module-webminlog webmin-module-updown webmin-module-acl webmin-module-servers webmin-module-filemin \
webmin-module-fdisk webmin-module-exports webmin-module-useradmin webmin-module-passwd webmin-module-proc webmin-module-proftpd webmin-module-webmincron \
webmin-module-software perl-module-json-pp shared-mime-info webmin-module-init \
"

RRECOMMENDS_${PN}-module-proc = "procps"
RRECOMMENDS_${PN}-module-raid = "mdadm"
RRECOMMENDS_${PN}-module-filemin = "perl-module-perlio perl-module-perlio-encoding"
RRECOMMENDS_${PN}-module-exports = "perl-module-file-basename perl-module-file-path perl-module-cwd perl-module-file-spec perl-module-file-spec-unix"
RRECOMMENDS_${PN}-module-fdisk = "parted"
RRECOMMENDS_${PN}-module-lvm = "lvm2"

python populate_packages_prepend() {
    import os, os.path

    wadir = bb.data.expand('${libexecdir}/webmin', d)
    wadir_image = bb.data.expand('${D}', d) + wadir
    modules = []
    themes = []
    for mod in os.listdir(wadir_image):
        modinfo = os.path.join(wadir_image, mod, "module.info")
        themeinfo = os.path.join(wadir_image, mod, "theme.info")
        if os.path.exists(modinfo):
            modules.append(mod)
        elif os.path.exists(themeinfo):
            themes.append(mod)

    do_split_packages(d, wadir, '^(%s)$' % "|".join(modules), 'webmin-module-%s', 'Webmin module for %s', allow_dirs=True, prepend=True)
    do_split_packages(d, wadir, '^(%s)$' % "|".join(themes), 'webmin-theme-%s', 'Webmin theme for %s', allow_dirs=True, prepend=True)
}

# Time-savers
package_do_pkgconfig() {
    :
}

INSANE_SKIP_${PN}-module-shellinabox += "file-rdeps already-stripped"

INSANE_SKIP += "file-rdeps src-uri-bad"
