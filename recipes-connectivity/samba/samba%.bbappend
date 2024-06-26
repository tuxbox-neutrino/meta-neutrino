FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN}:append += "glibc-gconv-ibm850"

SRC_URI:append += "file://samba \
"

do_install:append() {
	install -d ${D}${sysconfdir}/pam.d
	install -m 0644 ${WORKDIR}/samba ${D}${sysconfdir}/pam.d
	sed -i "s|\/var\/run|\/run|" ${D}/etc/tmpfiles.d/samba.conf
}

pkg_postinst_ontarget_${PN}() {
#!/bin/sh

pdbedit -L | grep root >> /dev/null || smbpasswd -n -a root
}
