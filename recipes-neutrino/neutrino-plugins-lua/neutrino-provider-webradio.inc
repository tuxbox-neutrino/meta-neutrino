include neutrino-provider-suffix.inc

PROVIDER_TARGET_DIR ?= "${N_WEBRADIO_DIR}"
PROVIDER_XML_GLOB ?= "*.xml"

do_install () {
	suffix="${PROVIDER_SUFFIX}"
	if [ -z "$suffix" ]; then
		bbfatal "provider suffix missing (PN should include -by-)"
	fi

	install -d ${D}${PROVIDER_TARGET_DIR}

	for f in ${S}/${PROVIDER_XML_GLOB}; do
		[ -e "$f" ] || continue
		base=$(basename "$f")
		name="${base%.*}"
		ext="${base##*.}"

		if provider_should_skip "$name"; then
			continue
		fi

		norm=$(provider_normalize_name "$name")
		[ -n "$norm" ] || norm="$name"
		new="${norm}-${suffix}.${ext}"
		install -m 644 "$f" "${D}${PROVIDER_TARGET_DIR}/${new}"
	done
}
