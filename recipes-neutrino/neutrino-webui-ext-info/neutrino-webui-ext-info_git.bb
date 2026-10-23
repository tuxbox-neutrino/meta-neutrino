SUMMARY = "Info screen for Neutrino's web interface"
DESCRIPTION = "An extension of Neutrino's web interface that shows on a screen \
of its own what the box plays and what runs on it, for a tablet or a second \
screen beside the box. It also keeps the former address /info/ leading to it."
HOMEPAGE = "https://github.com/tuxbox-neutrino/neutrino-webui-ext-info"
SECTION = "network"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit gitpkgv allarch
PKGV = "${GITPKGVTAG}"

SRC_URI = "git://github.com/tuxbox-neutrino/neutrino-webui-ext-info.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

# for the path of the web interface (N_PRIVATE_HTTPDDIR)
include ../neutrino/neutrino-common-vars.inc

# its page loads the web interface's scripts, which neutrino brings
RDEPENDS:${PN} = "neutrino"

INHIBIT_DEFAULT_DEPS = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

# Into the web interface's folder at the root of the filesystem, where
# neutrino installs it and nhttpd serves it: the extension under
# extensions/info and the page that keeps /info/ leading to it.
do_install() {
	oe_runmake DESTDIR=${D} HTTPDDIR=${N_PRIVATE_HTTPDDIR} install
}

FILES:${PN} = " \
	${N_PRIVATE_HTTPDDIR}/extensions/info \
	${N_PRIVATE_HTTPDDIR}/info \
"

# neutrino carried these files itself until the info screen left its tree;
# this package may take them over from such a neutrino on an update
RREPLACES:${PN} = "neutrino"
