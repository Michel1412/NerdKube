#!/usr/bin/env python3
"""Cria matrizes de textura dos 5 itens intermediários da progressão magia."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TEXTURES = ROOT / "docs" / "textures" / "item"

ITEMS = {
    "oferta_poco_sombria": {
        "display_name": "Oferenda ao Abismo",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [90, 30, 120, 255],
            "X": [200, 50, 180, 255],
            "O": [180, 30, 40, 255],
            "+": [80, 20, 60, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . # # . . . . . . . .
. . . . . # X X # . . . . . . .
. . . . # X O O X # . . . . . .
. . . . # X O O X # . . . . . .
. . . # X X O O X X # . . . . .
. . . # X X + + X X # . . . . .
. . . . # X O O X # . . . . . .
. . . . # X O O X # . . . . . .
. . . . . # X X # . . . . . . .
. . . . . . # # . . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "componente_magia_stage1_completo": {
        "display_name": "Coração Sombrio Purificado",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [200, 170, 50, 255],
            "X": [120, 20, 30, 255],
            "O": [60, 20, 80, 255],
            "+": [220, 40, 50, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . # # . . # # . . . . . .
. . . # X X # # X X # . . . . .
. . # X O O X X O O X # . . . .
. . # X O + O O + O X # . . . .
. . . # X O + + O X # . . . . .
. . . # X X O O X X # . . . . .
. . . . # X X X X # . . . . . .
. . . . . # X X # . . . . . . .
. . . . . . # # . . . . . . . .
. . . . . . . # . . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "fragmento_magia_stage2": {
        "display_name": "Pergaminho de Geometria Proibida",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [100, 65, 35, 255],
            "X": [220, 200, 160, 255],
            "O": [90, 40, 120, 255],
            "+": [40, 20, 50, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . # # # # # # # # # # # . . .
. . # X X X X X X X X X # . . .
. . # X X X + + + X X X # . . .
. . . # X X + O + X X # . . . .
. . . # X X + + + X X # . . . .
. . . # X X X X X X X # . . . .
. . # X X X X X X X X X # . . .
. . # # # # # # # # # # # . . .
. . . . . . . . . . . . . . . .
. . . . . . . . . . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "componente_magia_stage2_completo": {
        "display_name": "Ídolo de Cinzas e Almas",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [90, 90, 95, 255],
            "X": [220, 120, 160, 255],
            "O": [60, 200, 180, 255],
            "+": [220, 120, 40, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . X X . . . . . . . .
. . . . . # O O # . . . . . . .
. . . . # # O O # # . . . . . .
. . . . # + # # + # . . . . . .
. . . # # + # # + # # . . . . .
. . . # # # # # # # # . . . . .
. . . # # O # # O # # . . . . .
. . . # # # # # # # # . . . . .
. . . # # # # # # # # . . . . .
. . # # # # # # # # # # . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "matriz_runica": {
        "display_name": "Anel de Ancoragem Celestial",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [20, 20, 25, 255],
            "X": [60, 30, 90, 255],
            "O": [240, 240, 255, 255],
            "+": [60, 120, 220, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . # # . . . . . . . .
. . . . # # X X # # . . . . . .
. . . # X X O O X X # . . . . .
. . # X O + . . + O X # . . . .
. # X O . . . . . . O X # . . .
. # X O . . . . . . O X # . . .
. . # X O + . . + O X # . . . .
. . . # X X O O X X # . . . . .
. . . . # # X X # # . . . . . .
. . . . . . # # . . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "essencia_vital_instavel": {
        "display_name": "Plasma de Sangue Condenado",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [120, 120, 130, 255],
            "X": [160, 30, 40, 255],
            "O": [240, 200, 50, 255],
            "+": [100, 60, 30, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . + + . . . . . . . .
. . . . . # # # # . . . . . . .
. . . . . # . . # . . . . . . .
. . . . # # # # # # . . . . . .
. . . # X X X X X X # . . . . .
. . . # X X O O X X # . . . . .
. . . # X O X X O X # . . . . .
. . . # X X X X X X # . . . . .
. . . # X X X X X X # . . . . .
. . . . # # # # # # . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
}


def main() -> None:
    for item_id, spec in ITEMS.items():
        base = TEXTURES / item_id
        option = base / "options" / "a"
        option.mkdir(parents=True, exist_ok=True)
        (base / "meta.json").write_text(
            json.dumps(
                {
                    "id": f"nerdkube:{item_id}",
                    "type": "item",
                    "display_name": spec["display_name"],
                    "module": "magia",
                    "resolution": 16,
                    "output_png": f"src/main/resources/assets/nerdkube/textures/item/{item_id}.png",
                    "model_json": f"src/main/resources/assets/nerdkube/models/item/{item_id}.json",
                    "active_option": "a",
                },
                indent=2,
                ensure_ascii=False,
            )
            + "\n",
            encoding="utf-8",
        )
        (option / "option.json").write_text(
            json.dumps({"name": spec["display_name"], "theme": "magia"}, indent=2, ensure_ascii=False) + "\n",
            encoding="utf-8",
        )
        (option / "palette.json").write_text(
            json.dumps(spec["palette"], indent=2) + "\n", encoding="utf-8"
        )
        (option / "matrix.txt").write_text(spec["matrix"] + "\n", encoding="utf-8")
        print(f"created {item_id}")


if __name__ == "__main__":
    main()
