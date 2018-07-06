DESCRIPTION = "STB - Backup Plugin"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-2-Clause;md5=8bef8e6712b1be5aa76af1ebde9d6378"
HOMEPAGE = "https://github.com/neutrino-hd"
DEPENDS = "lua"
RDEPENDS_${PN} = "luaposix"
PV = "0.1"

SRC_URI = "file://stb_backup.tar.gz \
"

S = "${WORKDIR}/"

do_install () {
	install -d ${D}/usr/share/tuxbox/plugins
	install -m 644 ${S}/stb* ${D}/usr/share/tuxbox/plugins
}

FILES_${PN} = "/usr"