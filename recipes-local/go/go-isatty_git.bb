SUMMARY = "go-isatty"
DESCRIPTION = "A command-line fuzzy finder"
HOMEPAGE = "https://github.com/mattn/go-isatty"
SECTION = "go"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

GO_IMPORT = "github.com/mattn/go-isatty"

SRC_URI = "git://${GO_IMPORT} \
"

SRCREV = "6ca4dbf54d38eea1a992b3c722a76a5d1c4cb25c"

inherit go

GO_INSTALL = "${GO_IMPORT}"

