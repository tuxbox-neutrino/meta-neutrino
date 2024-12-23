# for some common variables, e.g. plugin paths
include ../neutrino/neutrino-common-vars.inc

SUMMARY = "Channellogos for Neutrino GUI"
DESCRIPTION = "${SUMMARY}"
SECTION = "neutrino-plugin"
MAINTAINER = "Fred Feuerstein"
HOMEPAGE = "https://github.com/neutrino-images/ni-logo-stuff"

# The provider of the logos has not defined a clear license. However, based on current knowledge,
# the provider explicitly prohibits others from packaging these graphics into their own distributions
# for redistribution. Additionally, users are tracked through the integrated update tool provided by the supplier.
#
# We do not endorse this approach. To ensure a reasonable packaging solution, we rely solely on the
# Git revision of the GitHub repository to trigger updates. For updates, as is customary,
# the package manager will notify users of any available updates.
# This method is sufficient to provide updates in the standard way as a package, with the only exception being that no installation files are included.
#
# Therefore, manual installation is rarely required. However, for manual logo installations,
# users can still utilize our logo updater tool, as usual, to manage and install the logos manually.
# This approach is comparable to a virtual package that does not include any files but automates
# the steps users would otherwise need to perform manually to download and install the graphic files on their STBs.

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

PR = "r1"
SRCREV = "${AUTOREV}"
PV = "git-${SRCREV}"

S = "${WORKDIR}/git"

inherit gitpkgv

do_patch () {
	ref=$(git -C ${S} rev-list --count master)
	echo "${ref}" > "${S}/.rev";
}

do_configure[noexec] = "1"

do_compile[noexec] = "1"

do_install () {
	install -d ${D}/${N_LOGODIR}
	install -m 644 ${S}/.rev ${D}${N_LOGODIR}
}

FILES:${PN} = "\
	${N_LOGODIR}\
"

pkg_postinst_ontarget_${PN} () {
	touch /tmp/.keep_background
	echo -e "starting logo updater..."
	curl http://localhost/control/startplugin?name=logoupdater &>/dev/null
	curl http://localhost/control/rcem?KEY_GREEN &>/dev/null
}
