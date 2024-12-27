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

do_install () {
    # Define the installation directory based on the plugin path
    INSTALLDIR=${D}${N_LUAPLUGIN_DIR}
    install -d ${INSTALLDIR}  # Create the directory if it doesn't exist

    SPLASH_FILE="${S}/${SPLASH_PNG}"  # Define the local path to the splash image

    # Check if the splash image exists; download it if not
    # Note: This section is necessary because `devtool` does not transfer this specific file
    # (the splash image) into the workspace when modifying the target.
    # As a result, the splash image must be downloaded separately and automatically
    # when the target is built from the workspace.
    # This is not relevant for the normal build process, as the file is handled via SRC_URI.
    if [ ! -f "${SPLASH_FILE}" ]; then
        bbwarn "File ${SPLASH_PNG} is missing! Attempting to download..."
        wget -O ${SPLASH_FILE} ${SPLASH_URL}

        # If the download is successful, verify the checksum
        if [ -f "${SPLASH_FILE}" ]; then
            bbwarn "File ${SPLASH_PNG} downloaded successfully, verifying checksum..."
            echo "${SPLASH_CHECKSUM}  ${SPLASH_FILE}" | sha256sum -c - || \
                bbfatal "Checksum verification failed for ${SPLASH_FILE}!"
        else
            bbwarn "Failed to download ${SPLASH_PNG} into ${WORKDIR}. Please copy it manually."
        fi
    fi

    # If the splash image exists, install it to the designated location
    # Note: This section checks again if the splash image is available. If it is still missing,
    # an error is logged, but the build does not fail because the file is not strictly required.
    # It can also be manually added to the source directory in the workspace if necessary.
    # This check is only relevant when building with workspace sources using `devtool`.
    # In a normal build process, the splash image should already be fetched via SRC_URI,
    # and its absence here would indicate a larger issue causing the build to fail during the fetch stage.
    if [ -f "${SPLASH_FILE}" ]; then
        install -m 644 ${SPLASH_FILE} ${INSTALLDIR}/${SRC_NAME}.png
    fi

    # Install Lua script and configuration file for the plugin
    install -m 755 ${S}/${SRC_NAME}.lua ${INSTALLDIR}/${SRC_NAME}.lua
    install -m 644 ${S}/${SRC_NAME}.cfg ${INSTALLDIR}/${SRC_NAME}.cfg
}

# Define the files included in the package
FILES:${PN} = " \
    /share \
    ${N_LUAPLUGIN_DIR} \
"
