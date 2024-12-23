
include neutrino-lua-plugins-target-pattern.inc

MAINTAINER = "flk, content by fred_feuerstein"
SRC_NAME = "logoupdater"

SRC_URI:append = " \
    https://raw.githubusercontent.com/neutrino-images/ni-logo-stuff/master/logo-intro/lua-version/${SRC_NAME}_1.png \
"

# only for logo checksum
SRC_URI[sha256sum] = "db5e3aa45b449f81af6ae265d7c607c70fd96eecb66bc0d9a3f6364054c3a245"

do_install () {
    export INSTALLDIR=${D}${N_LUAPLUGIN_DIR}
        install -d ${INSTALLDIR} && \
        install -m 644 ${WORKDIR}/${SRC_NAME}_1.png ${INSTALLDIR}/${SRC_NAME}.png && \
		install -m 755  ${S}/${SRC_NAME}.lua ${INSTALLDIR}/${SRC_NAME}.lua && \
        install -m 644 ${S}/${SRC_NAME}.cfg ${INSTALLDIR}/${SRC_NAME}.cfg
}

FILES:${PN} = " \
	/share \
	${N_LUAPLUGIN_DIR} \
"
