
include neutrino-lua-plugins.inc
## PN (package name = filename)
#PN = "neutrino-lua-plugins-shared-files"
SUMMARY = "Shared files and scripts for neutrino-lua-plugins"
PR = "r2"

MIGIT_ENABLED = "0"

SRC_URI = " \
	git://github.com/tuxbox-neutrino/plugin-scripts-lua.git;protocol=https;subpath=share;destsuffix=${SRC_DEST_SUFFIX};branch=master \
"

PV_LUA = "5.2"
S = "${WORKDIR}/${SRC_DEST_SUFFIX}"

SRC_SUBPATH = "lua/${PV_LUA}"

do_install () {
	install -d ${D}${datadir}/lua/${LUA_VER}
	set -- ${S}/${SRC_SUBPATH}/n_*.lua
	if [ -e "$1" ]; then
		cp -r "$@" ${D}${datadir}/lua/${LUA_VER}/
	else
		bbwarn "No n_*.lua files found in ${S}/${SRC_SUBPATH}; continuing"
	fi
##	json.lua not required, already provided by json package itself
# 	cp -r ${S}/${PV_LUA}/json.lua ${D}/usr/share/lua/${LUA_VER}/json.lua

# 	install -d ${D}/usr/share/lua/${PV_LUA}
# 	cp -r ${S}/${SRC_SUBPATH}/* ${D}/usr/share/lua/${PV_LUA}/

	rm -rf ${D}/usr/share/tuxbox
}

FILES:${PN} += "${datadir}/lua/${LUA_VER}"
