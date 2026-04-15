# for some common variables, e.g. icon paths
include ../neutrino/neutrino-common-vars.inc

SUMMARY = "Curated Tabler icon override set for Neutrino"
DESCRIPTION = "Installs a curated subset of Tabler icons into /var/tuxbox/icons so Neutrino can use modern SVG icons with fallback to its packaged base icons for anything not overridden."
HOMEPAGE = "https://github.com/tabler/tabler-icons"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=773c12d38d1fb8e4b902b248c05ec06f"

SECTION = "themes"

PV = "3.41.1"
PR = "r0"

INSANE_SKIP += "src-uri-bad"
SRC_URI = " \
    https://github.com/tabler/tabler-icons/archive/refs/tags/v${PV}.tar.gz;downloadfilename=tabler-icons-v${PV}.tar.gz \
    file://icon-map.json \
    file://generate-icon-theme.py \
    file://README.mapping.md \
"
SRC_URI[sha256sum] = "d1b0cd8e033e8ea82ee13784c93a8b4dc70cf0a738045e3de1ee25133d48315a"

S = "${WORKDIR}/tabler-icons-${PV}"

inherit allarch python3native

RDEPENDS:${PN} = "neutrino"

do_install() {
    install -d ${D}${N_ICONS_DIR_VAR}
    ${STAGING_BINDIR_NATIVE}/python3-native/python3 ${WORKDIR}/generate-icon-theme.py \
        --map ${WORKDIR}/icon-map.json \
        --tabler-root ${S} \
        --output-dir ${D}${N_ICONS_DIR_VAR}
    
    rm -f ${D}${N_ICONS_DIR_VAR}/.tabler*
}

FILES:${PN}:append = " \
    ${N_ICONS_DIR_VAR} \
"
