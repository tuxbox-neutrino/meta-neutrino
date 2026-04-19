
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DESCRIPTION = "Required data files and fonts for neutrino plugins."
MAINTAINER = "${FLAVOR}-developers"
HOMEPAGE = ""

SUMMARY = "${DESCRIPTION}"
PE = "1"
PR = "r2"
PV = "git"

SRCREV = "${AUTOREV}"

inherit gitpkgv

PKGV = "${GITPKGVTAG}"
GITPKGVTAG_NO_WARN_ON_NO_TAG = "1"

SRC_URI = " \
	git://github.com/neutrino-images/ni-neutrino-plugins.git;protocol=https;subpath=data;destsuffix=git/data;branch=master \
"

S = "${WORKDIR}/git"

do_install () {
	install -d ${D}${datadir}/fonts
	install -m 0644 ${S}/data/fonts/* ${D}${datadir}/fonts/
	rm -f ${D}${datadir}/fonts/Makefile*
}

FILES:${PN} = " \
	${datadir}/fonts \
"
