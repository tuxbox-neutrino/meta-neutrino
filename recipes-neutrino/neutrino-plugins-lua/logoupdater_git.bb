include neutrino-lua-plugins-target-pattern.inc

# Maintainer information for the package
MAINTAINER = "flk, content by fred_feuerstein"

# Source name and additional metadata
SRC_NAME = "logoupdater"

# Splash image filename and its URL
SPLASH_PNG = "${SRC_NAME}_1.png"
SPLASH_URL = "https://raw.githubusercontent.com/neutrino-images/ni-logo-stuff/master/logo-intro/lua-version/${SPLASH_PNG}"

# SHA256 checksum for the splash image to ensure data integrity
SPLASH_CHECKSUM = "db5e3aa45b449f81af6ae265d7c607c70fd96eecb66bc0d9a3f6364054c3a245"

# Append the splash image URL to the source URI for download during the build process
SRC_URI:append = " \
    ${SPLASH_URL};name=splash \
"

# Checksum of the downloaded splash image
SRC_URI[splash.sha256sum] = "${SPLASH_CHECKSUM}"

# Use the fetched asset directly and avoid runtime downloads
SPLASH_FILE = "${WORKDIR}/${SPLASH_PNG}"

do_install () {
    INSTALLDIR=${D}${N_LUAPLUGIN_DIR}
    install -d ${INSTALLDIR}

    if [ -f "${SPLASH_FILE}" ]; then
        install -m 644 ${SPLASH_FILE} ${INSTALLDIR}/${SRC_NAME}.png
    else
        bbwarn "logoupdater: splash ${SPLASH_FILE} missing; skipping image install"
    fi

    install -m 755 ${S}/${SRC_NAME}.lua ${INSTALLDIR}/${SRC_NAME}.lua
    install -m 644 ${S}/${SRC_NAME}.cfg ${INSTALLDIR}/${SRC_NAME}.cfg
}

# Define the files included in the package
FILES:${PN} = " \
    /share \
    ${N_LUAPLUGIN_DIR} \
"
