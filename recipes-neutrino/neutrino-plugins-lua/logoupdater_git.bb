include neutrino-lua-plugins-target-pattern.inc

# Maintainer information for the package
MAINTAINER = "flk, content by fred_feuerstein"

# Source name and additional metadata
SRC_NAME = "logoupdater"

# Keep this recipe revision local to logoupdater changes.
PR = "r5"

# Track logo-source changes without packaging the full logo repository.
LOGO_REPO_NAME = "logos"
LOGO_REPO_BRANCH = "master"
LOGO_REPO_DIR = "${WORKDIR}/logo-repo"
LOGO_REPO_URI = "git://github.com/neutrino-images/ni-logo-stuff.git;protocol=https;branch=${LOGO_REPO_BRANCH};name=${LOGO_REPO_NAME};destsuffix=logo-repo"

# Use a dedicated source revision for logo tracking.
SRC_URI:append = " \
    ${LOGO_REPO_URI} \
"
SRCREV_logos = "${AUTOREV}"
SRCREV_FORMAT = "default_logos"

def logoupdater_logo_suffix(d):
    srcpv = d.getVar('SRCPV') or ''
    if '_' in srcpv:
        return srcpv.rsplit('_', 1)[-1][:10]
    return (srcpv or "unknown")[:10]

LOGO_TRACK_SUFFIX = "${@logoupdater_logo_suffix(d)}"

# Include both plugin and logo-source changes in package versioning.
PKGV = "${MIGIT_PKGV}+logo${LOGO_TRACK_SUFFIX}"

# Plugin splash image expected by logoupdater.lua.
SPLASH_PNG = "${SRC_NAME}_1.png"
SPLASH_FILE = "${LOGO_REPO_DIR}/logo-intro/lua-version/${SPLASH_PNG}"
SPLASH_FALLBACK = "${S}/${SRC_NAME}.png"

# Runtime tools used by the plugin for online updates.
RDEPENDS_${PN} += "curl rsync unzip ca-certificates"
RRECOMMENDS_${PN} += "git"

# only marker file, no channel logos are packaged by this recipe
LOGO_TRACK_FILE = "${SRC_NAME}.logosrc-rev"

do_install () {
    INSTALLDIR=${D}${N_LUAPLUGIN_DIR}
    install -d ${INSTALLDIR}

    if [ -f "${SPLASH_FILE}" ]; then
        install -m 644 "${SPLASH_FILE}" "${INSTALLDIR}/${SRC_NAME}.png"
    elif [ -f "${SPLASH_FALLBACK}" ]; then
        install -m 644 "${SPLASH_FALLBACK}" "${INSTALLDIR}/${SRC_NAME}.png"
        bbwarn "logoupdater: using splash fallback ${SPLASH_FALLBACK}"
    else
        bbwarn "logoupdater: splash missing, installing plugin without background image"
    fi

    logo_rev="$(git -C "${LOGO_REPO_DIR}" rev-parse --verify HEAD 2>/dev/null || true)"
    if [ -n "${logo_rev}" ]; then
        printf '%s\n' "${logo_rev}" > "${INSTALLDIR}/${LOGO_TRACK_FILE}"
    else
        bbwarn "logoupdater: unable to read logo repo revision, using ${SRCPV}"
        printf '%s\n' "${SRCPV}" > "${INSTALLDIR}/${LOGO_TRACK_FILE}"
    fi

    install -m 755 "${S}/${SRC_NAME}.lua" "${INSTALLDIR}/${SRC_NAME}.lua"
    install -m 644 "${S}/${SRC_NAME}.cfg" "${INSTALLDIR}/${SRC_NAME}.cfg"
}

# Define the files included in the package
FILES:${PN} = " \
    /share \
    ${N_LUAPLUGIN_DIR} \
"
