DESCRIPTION = "Sortierte Senderliste SAT 19.2E-13.0E-23.5E von Annie"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-2-Clause;md5=8bef8e6712b1be5aa76af1ebde9d6378"
HOMEPAGE = "https://github.com/neutrino-hd/settings-annie"

S = "${WORKDIR}/git"

RREPLACES_${PN} = "settings-annie-19.2e-13.0e-23.5e-28.2e-26.0e-0.8w-5.0w settings-annie-19.2e-13.0e-23.5e-28.2e settings-annie-19.2e-13.0e settings-annie-19.2e settings-matze-astra settings-matze-astra+hb settings-pathauf"
RCONFLICTS_${PN} = "settings-annie-19.2e-13.0e-23.5e-28.2e-26.0e-0.8w-5.0w settings-annie-19.2e-13.0e-23.5e-28.2e settings-annie-19.2e-13.0e settings-annie-19.2e settings-matze-astra settings-matze-astra+hb settings-pathauf"

SRCREV = "${AUTOREV}"

SRC_URI = "git://github.com/neutrino-hd/settings-annie.git;protocol=https"

do_install () {
	install -d ${D}/etc/neutrino/config/zapit  
        install -m 644 ${S}/19.2E-13.0E-23.5E/zapit/*.xml ${D}/etc/neutrino/config/zapit
}

FILES_${PN} = "\
    /etc/neutrino/config/zapit \
"
