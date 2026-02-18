# Neutrino Runtime Dependency Matrix

Status snapshot: 2026-02-18

This matrix tracks runtime tools that are called by Neutrino core and bundled
plugins, and maps them to package integration in this stack.

## Core Paths

| Tool / helper | Code path | Package source | Integration |
| --- | --- | --- | --- |
| `opkg` | `src/gui/opkg_manager.cpp` | `opkg` | Required by base image and Neutrino package management UI |
| `ifup`, `ifdown` | `src/system/configure_network.cpp` | `ifupdown` | Included via network packagegroup |
| `mount`, `umount` | `src/neutrino.cpp`, `src/gui/hdd_menu.cpp` | `busybox`/`util-linux` | Included via base image |
| `service` | `src/system/setting_helpers.cpp` | `busybox`/init stack | Included via base image |
| `streamripper` | `src/gui/audioplayer.cpp` | `streamripper` | `RDEPENDS` in `neutrino.inc` |
| `streamripper.sh` | `src/gui/audioplayer.cpp` | `neutrino` package | Installed from layer-owned helper script |
| `grab` | `src/driver/screenshot.cpp`, `src/driver/glcd/glcd.cpp` | `grab` | `RRECOMMENDS` in `neutrino.inc` |
| `ofgwrite_caller` | `src/gui/update.cpp` | `ofgwrite` package | Installed by `meta-tuxbox` `ofgwrite.bbappend` |

## Optional / Feature-Driven Tools

| Tool | Code path | Behavior |
| --- | --- | --- |
| `ntpdate` / `ntpd` | `src/eitd/sectionsd.cpp` | Uses `ntpdate` first, falls back to `ntpd` |
| `fdisk` / `sfdisk` / `sgdisk` | `src/gui/hdd_menu.cpp` | Uses first available tool; fallback chain implemented |
| `hdparm`, `hd-idle` | `src/gui/hdd_menu.cpp` | Optional HDD tuning / standby features |
| `ether-wake` | `src/neutrino.cpp`, `src/gui/moviebrowser/mb.cpp` | Optional wake-on-LAN for recording paths |

## Plugin Script Paths (selected)

| Plugin | Script tool usage | Current package path |
| --- | --- | --- |
| `autoreboot`, `epgscan`, `pr-auto-timer` | `wget`, `dos2unix` | Provided by base image runtime |
| `imgbackup` | `mount`, `umount`, `tar` | Provided by base image runtime |

## Policy

- Avoid importing helper scripts from NI repositories as-is.
- Keep layer-owned helper scripts neutral (no external branding assumptions).
- Prefer strict runtime dependencies only where a feature is mandatory; use
  recommendations for optional UI features.
