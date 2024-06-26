require linux-libc-headers.inc

SRC_URI += " \
    file://fsxattr.patch \
    file://bpf_perf_event.h \
"

SRC_URI[md5sum] = "b5e7f6b9b2fe1b6cc7bc56a3a0bfc090"
SRC_URI[sha256sum] = "3c95d9f049bd085e5c346d2c77f063b8425f191460fcd3ae9fe7e94e0477dc4b"

do_install_armmultilib_prepend() {
	install -m 0644 ${WORKDIR}/bpf_perf_event.h ${D}${includedir}/asm
}
