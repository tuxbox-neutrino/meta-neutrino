#!/bin/sh
set -eu

PIDFILE="/run/streamripper.pid"
STREAMRIPPER_BIN="${STREAMRIPPER_BIN:-streamripper}"
DEFAULT_OUTDIR="${STREAMRIPPER_DEFAULT_DIR:-/media/hdd/movie/streamripper}"
NEUTRINO_CONF="${NEUTRINO_CONFIG_FILE:-/etc/neutrino/config/neutrino.conf}"

usage() {
	echo "Usage: $0 {start <url> [output-dir]|stop|status}"
}

fail() {
	echo "ERROR: $*" >&2
	exit 1
}

read_neutrino_setting() {
	key="$1"
	[ -r "${NEUTRINO_CONF}" ] || return 1
	awk -F= -v k="${key}" '$1 == k {print $2; exit}' "${NEUTRINO_CONF}"
}

resolve_output_dir() {
	if [ "${#}" -ge 1 ] && [ -n "$1" ]; then
		echo "$1"
		return 0
	fi

	if [ -n "${STREAMRIPPER_OUTPUT_DIR:-}" ]; then
		echo "${STREAMRIPPER_OUTPUT_DIR}"
		return 0
	fi

	if [ -r /etc/init.d/globals ]; then
		# shellcheck disable=SC1091
		. /etc/init.d/globals
		if command -v get_setting >/dev/null 2>&1; then
			dir="$(get_setting network_nfs_streamripperdir 2>/dev/null || true)"
			if [ -n "${dir}" ]; then
				echo "${dir}"
				return 0
			fi
		fi
	fi

	dir="$(read_neutrino_setting network_nfs_streamripperdir 2>/dev/null || true)"
	if [ -n "${dir}" ]; then
		echo "${dir}"
		return 0
	fi

	echo "${DEFAULT_OUTDIR}"
}

is_running() {
	[ -s "${PIDFILE}" ] || return 1
	pid="$(cat "${PIDFILE}" 2>/dev/null || true)"
	[ -n "${pid}" ] || return 1
	kill -0 "${pid}" 2>/dev/null
}

start_ripper() {
	url="${1:-}"
	outdir="$(resolve_output_dir "${2:-}")"

	[ -n "${url}" ] || fail "missing stream URL"
	command -v "${STREAMRIPPER_BIN}" >/dev/null 2>&1 || fail "streamripper binary not found"

	if is_running; then
		fail "streamripper already running (pid $(cat "${PIDFILE}" 2>/dev/null))"
	fi

	mkdir -p "${outdir}" || fail "cannot create output directory: ${outdir}"

	"${STREAMRIPPER_BIN}" "${url}" -a -s -d "${outdir}" &
	pid="$!"
	echo "${pid}" > "${PIDFILE}"
	echo "streamripper started (pid ${pid}) -> ${outdir}"
}

stop_ripper() {
	if is_running; then
		pid="$(cat "${PIDFILE}" 2>/dev/null || true)"
		kill "${pid}" 2>/dev/null || true
		sleep 1
		kill -9 "${pid}" 2>/dev/null || true
		rm -f "${PIDFILE}"
		echo "streamripper stopped"
		return 0
	fi

	if command -v killall >/dev/null 2>&1; then
		killall "${STREAMRIPPER_BIN}" >/dev/null 2>&1 || true
	fi
	rm -f "${PIDFILE}"
	echo "streamripper not running"
}

status_ripper() {
	if is_running; then
		echo "running (pid $(cat "${PIDFILE}" 2>/dev/null))"
		return 0
	fi
	echo "stopped"
	return 1
}

cmd="${1:-}"
case "${cmd}" in
	start)
		shift
		start_ripper "${1:-}" "${2:-}"
		;;
	stop)
		stop_ripper
		;;
	status)
		status_ripper
		;;
	*)
		usage
		exit 1
		;;
esac
