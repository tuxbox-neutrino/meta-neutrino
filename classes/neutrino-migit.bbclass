# Extract subdirectories from monorepos into standalone git trees using migit.
# Intended for Neutrino script/Lua plugin packaging.

MIGIT_ENABLED ?= "0"
MIGIT_SUBDIR ?= ""
MIGIT_REPO_NAME ?= ""
MIGIT_DEPLOY_DIR ?= "${WORKDIR}/migit"
MIGIT_OUTPUT_DIR ?= "${MIGIT_DEPLOY_DIR}/current"
MIGIT_WORK_REPO ?= "${WORKDIR}/git"
def migit_project_name(d):
    import os
    src = (d.getVar('MIGIT_SOURCE') or '').rstrip('/')
    if not src:
        return d.getVar('PN')
    base = os.path.basename(src)
    if base.endswith('.git'):
        base = base[:-4]
    return base or d.getVar('PN')

MIGIT_PROJECT ?= "${@migit_project_name(d)}"
MIGIT_SOURCE ?= "${WORKDIR}/git"
MIGIT_OPTS ?= "--no-backups -n"

MIGIT_PKGV_FILE ?= "${WORKDIR}/migit.pkgv"
MIGIT_PKGV_FALLBACK ?= "${SRCPV}"

def migit_repo_name(d):
    import os
    subdir = (d.getVar('MIGIT_SUBDIR') or '').rstrip('/')
    if not subdir:
        return ''
    repo_name = d.getVar('MIGIT_REPO_NAME') or ''
    if repo_name:
        return repo_name
    return os.path.basename(subdir)

def get_migit_pkgv(d):
    import os
    path = d.getVar('MIGIT_PKGV_FILE')
    if path and os.path.exists(path):
        with open(path, 'r', encoding='utf-8') as f:
            value = f.read().strip()
            if value:
                return value
    return d.getVar('MIGIT_PKGV_FALLBACK') or "0"

MIGIT_PKGV = "${@get_migit_pkgv(d)}"

MIGIT_REPO_NAME ?= "${@migit_repo_name(d)}"
MIGIT_REPO = "${MIGIT_OUTPUT_DIR}/${MIGIT_REPO_NAME}"
DEPENDS:append = "${@' migit-native' if d.getVar('MIGIT_ENABLED') == '1' else ''}"

# Extract a single subdir into a standalone repo in ${MIGIT_DEPLOY_DIR}.
do_migit_extract() {
    if [ "${MIGIT_ENABLED}" != "1" ]; then
        exit 0
    fi

    if [ -z "${MIGIT_SUBDIR}" ]; then
        bbfatal "migit: MIGIT_SUBDIR is empty"
    fi

    if [ ! -d "${MIGIT_SOURCE}/.git" ]; then
        bbfatal "migit: source repo missing at ${MIGIT_SOURCE}"
    fi

    # migit may generate a timestamped root folder name, so we do not assume
    # a static deploy subdirectory here.
    rm -rf "${MIGIT_DEPLOY_DIR}"
    install -d "${MIGIT_DEPLOY_DIR}"

    # migit uses $(pwd)/work internally. Run it from a recipe-local directory
    # to avoid cross-recipe collisions when tasks execute in parallel.
    (
        cd "${MIGIT_DEPLOY_DIR}"
        migit "${MIGIT_SOURCE}" \
            --subdir "${MIGIT_SUBDIR}" \
            --deploy-dir "${MIGIT_DEPLOY_DIR}" \
            --target-root-project-name "${MIGIT_PROJECT}" \
            ${MIGIT_OPTS}
    )

    src_repo=$(ls -td "${MIGIT_DEPLOY_DIR}"/*/"${MIGIT_REPO_NAME}" 2>/dev/null | head -n 1 || true)
    if [ -z "${src_repo}" ] || [ ! -d "${src_repo}/.git" ]; then
        bbfatal "migit: output repo missing at ${src_repo}"
    fi

    rm -rf "${MIGIT_OUTPUT_DIR}/${MIGIT_REPO_NAME}"
    install -d "${MIGIT_OUTPUT_DIR}"
    cp -a "${src_repo}" "${MIGIT_OUTPUT_DIR}/"

    if [ ! -d "${MIGIT_REPO}/.git" ]; then
        bbfatal "migit: output repo missing at ${MIGIT_REPO}"
    fi

    # Replace the original WORKDIR/git with the filtered repo for later tasks/devtool
    rm -rf "${MIGIT_WORK_REPO}"
    cp -a "${MIGIT_REPO}" "${MIGIT_WORK_REPO}"
}

do_unpack[postfuncs] += "do_migit_extract"
do_unpack[depends] += "${@' migit-native:do_populate_sysroot' if d.getVar('MIGIT_ENABLED') == '1' else ''}"

# Compute a plugin-specific version string based on the filtered repo.
do_migit_version() {
    if [ "${MIGIT_ENABLED}" != "1" ]; then
        exit 0
    fi

    count=$(git -C "${MIGIT_WORK_REPO}" rev-list --count HEAD)
    hash=$(git -C "${MIGIT_WORK_REPO}" rev-parse --short=10 HEAD)
    echo "${count}+g${hash}" > "${MIGIT_PKGV_FILE}"
}
addtask migit_version after do_unpack before do_patch
