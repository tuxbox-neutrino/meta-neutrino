# for some common variables, e.g. plugin paths
include ../neutrino/neutrino-common-vars.inc

SUMMARY = "Channellogos for Neutrino GUI"
DESCRIPTION = "${SUMMARY}"
SECTION = "neutrino-plugin"
MAINTAINER = "Fred Feuerstein"
HOMEPAGE = "https://github.com/neutrino-images/ni-logo-stuff"

# The provider of the logos has not defined a clear license. However, according to current knowledge,
# the provider insists that no one may package these graphics into their own packages and distribute them.
# Additionally, users are explicitly tracked through its integrated update tool.
# We will not adopt this practice. In order to offer reasonable packaging, we use only the Git revision of the GitHub repository.
# Should this change, as is usual, possible updates will be indicated by the package manager.
# This is sufficient to offer available updates in the usual way as a package. The only difference is that no installation files are provided.
# For the actual installation, we use our own logo updater, as usual, which can still be used as before but is utilized by the package manager
# for post-installation to install the logos. This is thus comparable to a virtual package that brings no files but only does what users
# would otherwise have to do manually to download and install the graphic files on their STBs.

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

PACKAGE_ARCH = "all"

RDEPENDS_${PN} = " \
	curl \
	logoupdater \
"

PROVIDES = "virtual/neutrino-logos"
RPROVIDES_${PN} = "virtual/neutrino-logos"

SRC_URI = " \
	git://github.com/neutrino-images/ni-logo-stuff.git;protocol=https \
"

PR = "r0"
SRCREV = "${AUTOREV}"
PV = "git-${SRCPV}"

S = "${WORKDIR}/git"

inherit gitpkgv

do_patch () {
	echo "${GITPKGV}" > "${S}/.rev";
}

do_configure[noexec] = "1"

do_compile[noexec] = "1"

do_install () {
	install -d ${D}/${N_LOGODIR}
	install -m 644 ${S}/.rev ${D}${N_LOGODIR}
}

FILES_${PN} = "\
	${N_LOGODIR}\
"

pkg_postinst_ontarget_${PN} () {
	touch /tmp/.keep_background
	echo -e "starting logo updater..."
	curl http://localhost/control/startplugin?name=logoupdater &>/dev/null
	curl http://localhost/control/rcem?KEY_GREEN &>/dev/null
}
