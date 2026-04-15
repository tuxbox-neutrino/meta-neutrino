# Neutrino Tabler Icon Mapping

This package is the first bootstrap step for modern Neutrino icons.

It does not try to replace the full historical icon tree in one shot. Instead,
it installs a curated subset of SVG icons from Tabler into
`/var/tuxbox/icons/` using Neutrino's existing legacy basenames such as
`btn_pause`, `settings`, or `hint_network`.

Why this shape:

- it is immediately usable with the current Neutrino runtime
- it avoids a risky mass rename of icon identifiers in C++ and plugin code
- any missing icon still falls back to Neutrino's packaged base assets

The mapping intentionally keeps the legacy names at the runtime boundary. The
modern naming only exists in the package-side manifest.

Future follow-up work can add a dedicated `icon_theme_name` setting in Neutrino
so icon sets can be selected independently from color themes.
