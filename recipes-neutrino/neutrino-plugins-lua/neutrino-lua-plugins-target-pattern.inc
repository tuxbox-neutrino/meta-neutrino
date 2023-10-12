include neutrino-lua-plugins.inc

# PN = often not the same as the target name, so it is defined in sub-recipes
SRC_NAME = "${PN}"

RDEPENDS_${PN} += "neutrino-lua-plugins-shared-files"

#SRC_SUBPATH = "plugins/${SRC_NAME}"

# Compile is not required
do_compile[noexec] = "1"

do_install () {
	# Create the destination directory for Lua plugins
	install -d ${D}${N_LUAPLUGIN_DIR}

	# Retrieve the list of files in the source directory
	file_list=$(ls -l "${S}" | awk '{print $9}')

	for f in $file_list; do
		if [ -f "${S}/$f" ]; then
			if [[ "$f" == *.conf ]]; then
				# If it's a .conf file:
				# Create the destination directory for configuration files
				install -d ${D}${N_CONFIG_DIR}

				# Copy the .conf file to the destination directory for configurations
				install -m 644 "${S}/$f" "${D}${N_CONFIG_DIR}/$f"

				# Create a symlink from Lua plugin to the .conf file in the destination directory for Lua plugins
				ln -s "${N_CONFIG_DIR}/$f" "${D}${N_LUAPLUGIN_DIR}/$f"
			else
				# If it's not a .conf file, simply install it to the destination directory for Lua plugins
				install -m 644 "${S}/$f" "${D}${N_LUAPLUGIN_DIR}/$f"
			fi
		fi
	done

	# Ensure removing of unwanted Makefiles from the destination directory for Lua plugins
	rm -f ${D}${N_LUAPLUGIN_DIR}/Makefile.*
}
