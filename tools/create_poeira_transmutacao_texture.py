#!/usr/bin/env python3
"""Matriz da Poeira de Transmutação Proibida."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ITEM_ID = "poeira_transmutacao_proibida"
BASE = ROOT / "docs" / "textures" / "item" / ITEM_ID
OPTION = BASE / "options" / "a"

SPEC = {
    "display_name": "Poeira de Transmutação Proibida",
    "palette": {
        ".": [0, 0, 0, 0],
        "#": [40, 80, 180, 255],
        "X": [80, 200, 220, 255],
        "O": [240, 250, 255, 255],
        "+": [120, 50, 180, 255],
    },
    "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . # # . . . . . . . .
. . . . . # O O # . . . . . . .
. . . . # X O O X # . . . . . .
. . . # X X O O X X # . . . . .
. # # X X X + + X X X # # . . .
. # O O + + + + + + O O # . . .
. # O O + + + + + + O O # . . .
. # # X X X + + X X X # # . . .
. . . # X X O O X X # . . . . .
. . . . # X O O X # . . . . . .
. . . . . # O O # . . . . . . .
. . . . . . # # . . . . . . . .
""".strip(),
}


def main() -> None:
    OPTION.mkdir(parents=True, exist_ok=True)
    (BASE / "meta.json").write_text(
        json.dumps(
            {
                "id": f"nerdkube:{ITEM_ID}",
                "type": "item",
                "display_name": SPEC["display_name"],
                "module": "alchemy",
                "resolution": 16,
                "output_png": f"src/main/resources/assets/nerdkube/textures/item/{ITEM_ID}.png",
                "model_json": f"src/main/resources/assets/nerdkube/models/item/{ITEM_ID}.json",
                "active_option": "a",
            },
            indent=2,
            ensure_ascii=False,
        )
        + "\n",
        encoding="utf-8",
    )
    (OPTION / "option.json").write_text(
        json.dumps({"name": SPEC["display_name"], "theme": "alchemy"}, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )
    (OPTION / "palette.json").write_text(json.dumps(SPEC["palette"], indent=2) + "\n", encoding="utf-8")
    (OPTION / "matrix.txt").write_text(SPEC["matrix"] + "\n", encoding="utf-8")
    print(f"created {ITEM_ID}")


if __name__ == "__main__":
    main()
