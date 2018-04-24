FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append += "file://exports \
" 

RDEPENDS_${PN} = "${PN}-client"

do_install_append() {
	install -m 644 ${WORKDIR}/exports ${D}${sysconfdir}
}
        
INSANE_SKIP_${PN} = "file-rdeps"

