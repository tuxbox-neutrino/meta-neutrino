#!/usr/bin/env python3

import argparse
import json
import shutil
import sys
from pathlib import Path


def parse_args():
    parser = argparse.ArgumentParser(
        description="Generate a curated Neutrino icon override set from Tabler."
    )
    parser.add_argument("--map", required=True, dest="map_file")
    parser.add_argument("--tabler-root", required=True)
    parser.add_argument("--output-dir", required=True)
    return parser.parse_args()


def load_mapping(path: Path):
    with path.open("r", encoding="utf-8") as handle:
        data = json.load(handle)
    icons = data.get("icons", [])
    if not isinstance(icons, list) or not icons:
        raise ValueError("icon map must contain a non-empty 'icons' list")
    return data


def source_path(tabler_root: Path, item):
    style = item["style"]
    tabler_name = item["tabler_name"]
    return tabler_root / "icons" / style / f"{tabler_name}.svg"


def validate_item(item):
    required = ("target_name", "tabler_name", "style")
    missing = [field for field in required if not item.get(field)]
    if missing:
        raise ValueError(f"mapping entry missing required fields: {', '.join(missing)}")
    if item["style"] not in ("outline", "filled"):
        raise ValueError(
            f"unsupported style '{item['style']}' for target '{item['target_name']}'"
        )


def main():
    args = parse_args()
    tabler_root = Path(args.tabler_root)
    output_dir = Path(args.output_dir)
    mapping = load_mapping(Path(args.map_file))

    output_dir.mkdir(parents=True, exist_ok=True)
    manifest_lines = [
        "# Generated from Tabler icons",
        f"# Source tag: {mapping.get('metadata', {}).get('tabler_tag', 'unknown')}",
    ]

    seen_targets = set()
    for item in mapping["icons"]:
        validate_item(item)
        target_name = item["target_name"]
        if target_name in seen_targets:
            raise ValueError(f"duplicate target_name '{target_name}' in mapping")
        seen_targets.add(target_name)

        src = source_path(tabler_root, item)
        if not src.exists():
            raise FileNotFoundError(f"missing Tabler source icon: {src}")

        dest = output_dir / f"{target_name}.svg"
        shutil.copyfile(src, dest)
        manifest_lines.append(
            f"{target_name}.svg <- {item['style']}/{item['tabler_name']}.svg"
        )

    with (output_dir / ".tabler-manifest.txt").open("w", encoding="utf-8") as handle:
        handle.write("\n".join(manifest_lines) + "\n")

    return 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except Exception as exc:
        print(f"error: {exc}", file=sys.stderr)
        sys.exit(1)
