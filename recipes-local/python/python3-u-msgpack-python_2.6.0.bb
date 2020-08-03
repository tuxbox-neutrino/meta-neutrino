DESCRIPTION = "A portable, lightweight MessagePack serializer and deserializer written in pure Python."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9eb4691bcb66360acee473a8cf75f594"

SRC_URI[sha256sum] = "754edb07eaee39a9686a99823892e3a1be4e0948d9cc5c717946750c27643c9c"

inherit pypi setuptools3 ptest

SRC_URI += " \
        file://run-ptest \
"

RDEPENDS_${PN}-ptest += " \
       ${PYTHON_PN}-pytest \
"

do_install_ptest() {
       cp -f ${S}/test_umsgpack.py ${D}${PTEST_PATH}/
}

RDEPENDS_${PN} += " \
    ${PYTHON_PN}-datetime \
"

BBCLASSEXTEND = "native nativesdk"
