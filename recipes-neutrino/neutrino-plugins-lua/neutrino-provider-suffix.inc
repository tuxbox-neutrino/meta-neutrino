PROVIDER_SUFFIX ?= "${@('by-' + (d.getVar('PN') or '').split('-by-')[-1]) if '-by-' in (d.getVar('PN') or '') else ''}"
PROVIDER_PREFIX ?= "${@((d.getVar('PN') or '').split('-by-')[-1]) if '-by-' in (d.getVar('PN') or '') else ''}"

PROVIDER_SKIP_BASENAMES ?= ""

provider_normalize_name() {
	local name="$1"

	if [ -n "${PROVIDER_PREFIX}" ]; then
		case "$name" in
			${PROVIDER_PREFIX}-*) name="${name#${PROVIDER_PREFIX}-}" ;;
		esac
	fi

	case "$name" in
		*-by-tuxbox) name="${name%-by-tuxbox}" ;;
		*-by-ni) name="${name%-by-ni}" ;;
	esac

	echo "$name"
}

provider_should_skip() {
	local name="$1"

	for skip in ${PROVIDER_SKIP_BASENAMES}; do
		if [ "$name" = "$skip" ]; then
			return 0
		fi
	done

	return 1
}
