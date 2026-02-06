include neutrino-lua-plugins.inc

# PN = often not the same as the target name, so it is defined in sub-recipes
SRC_NAME = "${PN}"

RDEPENDS_${PN} += "neutrino-lua-plugins-shared-files"

DEPENDS += "makeit-native"

# Defaults for makeit-driven installs
PLUGIN_SCRIPT_NAME ?= "${SRC_NAME}"
PLUGIN_SOURCE_DIR ?= "${S}"
PLUGIN_INSTALLDIR ?= "${N_LUAPLUGIN_DIR}"
PLUGIN_PREFIX ?= ""
PLUGIN_SUFFIX ?= ""
PLUGIN_TARGET_NAME ?= ""
PLUGIN_EXTRA_FILES ?= ""

# Compile is not required
do_compile[noexec] = "1"

do_install () {
	script_name="${PLUGIN_SCRIPT_NAME}"
	if [ -z "${script_name}" ] || [ ! -e "${PLUGIN_SOURCE_DIR}/${script_name}.lua" -a ! -e "${PLUGIN_SOURCE_DIR}/${script_name}.sh" ]; then
		for ext in lua sh; do
			candidate=$(ls "${PLUGIN_SOURCE_DIR}"/*.${ext} 2>/dev/null | head -n 1)
			if [ -n "${candidate}" ]; then
				script_name=$(basename "${candidate}" .${ext})
				break
			fi
		done
	fi

	if [ -z "${script_name}" ]; then
		bbfatal "makeit: no plugin script found in ${PLUGIN_SOURCE_DIR}"
	fi

	target_name_opt=""
	if [ -n "${PLUGIN_TARGET_NAME}" ]; then
		target_name_opt="TARGET_PROGRAM_NAME=${PLUGIN_TARGET_NAME}"
	fi

	makeit install \
		SCRIPT_NAME="${script_name}" \
		SOURCE_DIR="${PLUGIN_SOURCE_DIR}/" \
		INSTALLDIR="${D}${PLUGIN_INSTALLDIR}" \
		PROGRAM_PREFIX="${PLUGIN_PREFIX}" \
		PROGRAM_SUFFIX="${PLUGIN_SUFFIX}" \
		EXTRAFILES="${PLUGIN_EXTRA_FILES}" \
		${target_name_opt}

	# Install .conf files into config dir and provide symlinks in plugin dir
	conf_files=$(ls "${PLUGIN_SOURCE_DIR}"/*.conf 2>/dev/null || true)
	if [ -n "${conf_files}" ]; then
		install -d ${D}${N_CONFIG_DIR}
		for f in ${conf_files}; do
			base=$(basename "$f")
			install -m 644 "$f" "${D}${N_CONFIG_DIR}/$base"
			ln -sf "${N_CONFIG_DIR}/$base" "${D}${PLUGIN_INSTALLDIR}/$base"
		done
	fi
}
