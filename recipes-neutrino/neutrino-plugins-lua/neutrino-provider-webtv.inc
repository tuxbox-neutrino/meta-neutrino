include neutrino-provider-suffix.inc

PROVIDER_TARGET_DIR ?= "${N_WEBTV_DIR}"
PROVIDER_SCRIPT_GLOB ?= "*.lua"
PROVIDER_XML_GLOB ?= "*.xml"

do_install () {
	suffix="${PROVIDER_SUFFIX}"
	if [ -z "$suffix" ]; then
		bbfatal "provider suffix missing (PN should include -by-)"
	fi

	install -d ${D}${PROVIDER_TARGET_DIR}

	scripts_map=""
	for s in ${S}/${PROVIDER_SCRIPT_GLOB}; do
		[ -e "$s" ] || continue
		raw=$(basename "$s" .lua)
		if provider_should_skip "$raw"; then
			continue
		fi
		name=$(provider_normalize_name "$raw")
		[ -n "$name" ] || name="$raw"
		scripts_map="$scripts_map ${raw}:${name}-${suffix}"
	done

	for f in ${S}/${PROVIDER_SCRIPT_GLOB} ${S}/${PROVIDER_XML_GLOB}; do
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

		if [ "$ext" = "xml" ]; then
			tmp="${WORKDIR}/${new}"
			cp "$f" "$tmp"
			for pair in $scripts_map; do
				raw="${pair%%:*}"
				ren="${pair##*:}"
				sed -i "s/script=\"${raw}\\.lua\"/script=\"${ren}\\.lua\"/g" "$tmp"
			done
			install -m 644 "$tmp" "${D}${PROVIDER_TARGET_DIR}/${new}"
		else
			install -m 644 "$f" "${D}${PROVIDER_TARGET_DIR}/${new}"
		fi
	done
}
