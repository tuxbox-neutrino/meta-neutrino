include neutrino-lua-plugins.inc
include neutrino-provider-webtv.inc

SUMMARY = "WebTV content from ni"
DESCRIPTION = "${SUMMARY}"
MAINTAINER = "ni"
SECTION = "optional"
HOMEPAGE = "https://github.com/neutrino-images/ni-neutrino-plugins"

SRC_NAME = "webtv"
MIGIT_SUBDIR = "scripts-lua/plugins/${SRC_NAME}"
MIGIT_REPO_NAME = "${SRC_NAME}"

SRC_URI = " \
	git://github.com/neutrino-images/ni-neutrino-plugins.git;protocol=https;branch=master \
"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PR = "r2"

do_compile[noexec] = "1"

PROVIDER_SKIP_BASENAMES = "filmon"

FILES:${PN} += " \
	${N_WEBTV_DIR} \
"
