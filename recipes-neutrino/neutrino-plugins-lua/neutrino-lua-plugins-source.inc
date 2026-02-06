SRC_DEST_SUFFIX = "src"

SRC_NAME = "${BPN}"

SRC_URI = " \
	git://github.com/tuxbox-neutrino/plugin-scripts-lua.git;protocol=https;branch=master \
"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

inherit neutrino-migit

# Extract a single plugin from the monorepo via migit.
MIGIT_ENABLED = "1"
MIGIT_SUBDIR = "plugins/${SRC_NAME}"
MIGIT_REPO_NAME = "${SRC_NAME}"

SRCREV = "${AUTOREV}"
PKGV = "${MIGIT_PKGV}"
PR = "r2"

S = "${WORKDIR}/git"
