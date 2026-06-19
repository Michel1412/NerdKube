#!/usr/bin/env python3
"""Copia textura Blockbench do cristal; não sobrescreve blockstate com facing."""

from __future__ import annotations

import json
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "src/main/resources/assets/nerdkube"
BLOCKBENCH_CRYSTAL = ROOT / "docs/blockbench/gear_crystal.png"


def main() -> None:
    if BLOCKBENCH_CRYSTAL.exists():
        (ASSETS / "textures/block").mkdir(parents=True, exist_ok=True)
        Image.open(BLOCKBENCH_CRYSTAL).convert("RGBA").save(ASSETS / "textures/block/gear_crystal.png")

    block_model = ASSETS / "models/block/sculk_gear_crystal.json"
    if block_model.exists():
        (ASSETS / "models/item/sculk_gear_crystal.json").write_text(
            json.dumps({"parent": "nerdkube:block/sculk_gear_crystal"}, indent=2) + "\n",
            encoding="utf-8",
        )


if __name__ == "__main__":
    main()
