
include neutrino-lua-plugins-target-pattern.inc

MAINTAINER = "flk, content by horsti58"
SRC_NAME = "settingsupdater"

SRC_URI:append = " \
    https://raw.githubusercontent.com/horsti58/lua-data/master/lua/settingupdater_1.png \
"

# only for logo
SRC_URI[sha256sum] = "f642d48bf0b2e98498ee0fa7cf1a5c3062f5eef601f87a815c8d7a4b65eced93"

do_install:append() {
    install -d ${D}${N_LUAPLUGIN_DIR}
    install -m 644 ${WORKDIR}/settingupdater_1.png ${D}${N_LUAPLUGIN_DIR}/${SRC_NAME}.png
}
