# meta-neutrino

Neutrino payload layer for Tuxbox-OS (Yocto Kirkstone / OE-Alliance base).

This layer is not a full standalone distro layer. It contains the Neutrino
stack and related hardware abstraction, while distro/image infrastructure is in
`meta-tuxbox` and orchestrated by `build-environment`.

## Scope

This layer contains:

- Neutrino core recipe(s)
- `libstb-hal`
- Neutrino plugins and themes
- Neutrino packagegroup: `packagegroup-tuxbox-neutrino`
- Neutrino-specific classes: `neutrino-boxmap.bbclass`, `neutrino-migit.bbclass`

This layer does not contain:

- distro configuration (`DISTRO = "tuxbox"`)
- image orchestration and generic packagegroups
- most non-Neutrino support recipes (tooling, middleware, Qt/Kodi, etc.)

Those parts live in `meta-tuxbox`.

## Recommended Usage

Use this layer through the orchestrator repository:

- Build Environment (orchestrator):
  `https://github.com/tuxbox-neutrino/build-environment`

Minimal flow:

```bash
git clone --recurse-submodules https://github.com/tuxbox-neutrino/build-environment.git
cd build-environment
make init
make image MACHINE=hd51 MACHINEBUILD=mutant51
```

The orchestrator manages `bblayers.conf`, machine setup, and pinned submodules.

## Manual Integration (advanced only)

If you build without the orchestrator, add at least:

```conf
BBLAYERS += " \
  /path/to/meta-neutrino \
  /path/to/meta-tuxbox \
"
DISTRO = "tuxbox"
```

Then build a Tuxbox image (for example `tuxbox-image`).

## Neutrino Flavour and Fork Workflow

In-tree default is `tuxbox`. NI/Tango/fork variants are intentionally not kept
as permanent flavours here.

For fork work use one of these paths:

1. Temporary workspace via `devtool modify` (preferred for development).
2. Persistent private overlay layer (`meta-local`) with your own bbappend/recipe
   and `SRC_URI`.

See hardware/integration documentation for the detailed workflow.

## Documentation

- Runtime dependency matrix:
  `NEUTRINO_RUNTIME_DEPENDENCY_MATRIX.md`
- Orchestrator README:
  `https://github.com/tuxbox-neutrino/build-environment/blob/master/README.md`
- Quick start:
  `https://github.com/tuxbox-neutrino/build-environment/blob/master/docs/QUICKSTART.md`
- Hardware integration:
  `https://github.com/tuxbox-neutrino/build-environment/blob/master/docs/HARDWARE_INTEGRATION.md`
- `meta-tuxbox` layer README:
  `https://github.com/tuxbox-neutrino/meta-tuxbox/blob/master/README.md`

## Compatibility

- Yocto release: Kirkstone (4.0 LTS)
- Layer series: `kirkstone`

## License

Layer metadata and infrastructure in this repository are published under
`BSD-2-Clause`.

Recipe payload keeps its original upstream licensing. Check `LICENSE` and
`LIC_FILES_CHKSUM` in each recipe for package-level licensing details.

## Credits

- Tuxbox-Neutrino developers and maintainers
- OE-Alliance project for machine and distro build ecosystem
- Yocto Project / OpenEmbedded maintainers
