SUMMARY = "Supervisor: A Process Control System"
DESCRIPTION = "\
Supervisor is a client/server system that allows its users \
to monitorand control a number of processes on UNIX-like \
operating systems."
HOMEPAGE = "https://github.com/Supervisor/supervisor"
LICENSE = "BSD-4-Clause"
LIC_FILES_CHKSUM = "file://LICENSES.txt;md5=5b4e3a2172bba4c47cded5885e7e507e"

SRC_URI[md5sum] = "ecea94eedc70ba5127fdeb0665bcca0d"
SRC_URI[sha256sum] = "2dc86fe0476e945e61483d614ceb2cf4f93b95282eb243bdf792621994360383"

PYPI_PACKAGE = "supervisor"
inherit pypi systemd setuptools3
RDEPENDS_${PN} = "\
    ${PYTHON_PN}-meld3 \
"

SRC_URI += "file://supervisord.conf \
	    file://supervisor.service \
	"
SYSTEMD_SERVICE_${PN} = "supervisor.service"

do_install:append() {
	install -d ${D}${sysconfdir}/supervisor
	install -d ${D}${systemd_system_unitdir}

	install -m 0644 ${WORKDIR}/supervisord.conf ${D}${sysconfdir}/supervisor
	install -m 0644 ${WORKDIR}/supervisor.service ${D}${systemd_system_unitdir}
}
