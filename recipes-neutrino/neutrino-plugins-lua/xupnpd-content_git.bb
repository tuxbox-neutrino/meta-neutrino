include neutrino-lua-plugins-target-pattern.inc

SRC_NAME = "xupnpd"
SRC_SUBPATH = "${SRC_NAME}"
SUMMARY = "Content for xupnpd"
PR = "r1"

PROVIDES="virtual/neutrino-lua-plugin-xupnpd"
RPROVIDES_${PN} = "virtual/neutrino-lua-plugin-xupnpd"

MIGIT_ENABLED = "0"

SRC_URI = " \
    git://github.com/tuxbox-neutrino/plugin-scripts-lua.git;protocol=https;subpath=${SRC_SUBPATH};destsuffix=${SRC_DEST_SUFFIX};branch=master \
"

do_install () {
	# install content
	install -d ${D}/usr/share/xupnpd
	set -- ${S}/xupnpd*
	if [ -e "$1" ]; then
		install -m 644 "$@" ${D}/usr/share/xupnpd/
	else
		bbwarn "No xupnpd* content files found in ${S}; continuing"
	fi

	# clean up, not required for content
	rm -rf ${D}/share
	rm -rf ${D}/usr/share/tuxbox
}
