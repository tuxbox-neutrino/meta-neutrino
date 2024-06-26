SUMMARY = "utilities to create, check, label and dump exFAT filesystem"
DESCRIPTION = "Utilities to manage extended file allocation table filesystem. \
This package provides tools to create, check and label the filesystem. It \
contains \
 - dumpexfat to dump properties of the filesystem \
 - exfatfsck / fsck.exfat to report errors found on a exFAT filesystem \
 - exfatlabel to label a exFAT filesystem \
 - mkexfatfs / mkfs.exfat to create a exFAT filesystem. \
"
HOMEPAGE = "http://code.google.com/p/exfat/"
SECTION = "universe/otherosfs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "https://github.com/relan/exfat/releases/download/v${PV}/${BP}.tar.gz"

DEPENDS = "virtual/libc"

inherit pkgconfig autotools

SRC_URI[md5sum] = "f8928571b152455e828ca0bd42af8b73"
SRC_URI[sha256sum] = "dfebd07a7b907e2d603d3a9626e6440bd43ec6c4e8c07ccfc57ce9502b724835"

do_install:append() {
	install -d ${D}/sbin
	ln -s /usr/sbin/mkfs.exfat ${D}/sbin/mkfs.exfat
        ln -s /usr/sbin/fsck.exfat ${D}/sbin/fsck.exfat
}
