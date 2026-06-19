#!/usr/bin/env python3
"""Matrizes de textura da progressão Exploração e Combate (NerdKube 0.6.0)."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TEXTURES = ROOT / "docs" / "textures"

ITEMS = {
    "fragmento_combate_stage1": {
        "display_name": "Amuleto de Sangue Petrificado",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [70, 70, 75, 255],
            "X": [120, 50, 160, 255],
            "O": [160, 30, 40, 255],
            "+": [200, 220, 255, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . . # . . . . . . . .
. . . . . . # # . . . . . . . .
. . . . . # X # . . . . . . . .
. . . . # X O X # . . . . . . .
. . . # X O O O X # . . . . . .
. . . # X O + O X # . . . . . .
. . . . # X O X # . . . . . . .
. . . . . # X # . . . . . . . .
. . . . . # # . . . . . . . . .
. . . . . # . . . . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "componente_combate_stage1_completo": {
        "display_name": "Núcleo de Alma do Vazio",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [50, 20, 70, 255],
            "X": [40, 180, 200, 255],
            "O": [15, 10, 20, 255],
            "+": [220, 60, 200, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . # . . . . # . . . . . .
. . . # # # . . # # # . . . . .
. . . # X # # # # X # . . . . .
. . # X O O O O O O X # . . . .
. . # X O + + + + O X # . . . .
. . # X O + + + + O X # . . . .
. . # X O O O O O O X # . . . .
. . . # X # # # # X # . . . . .
. . . # # # . . # # # . . . . .
. . . . # . . . . # . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "fragmento_combate_stage2": {
        "display_name": "Insígnia do Desbravador Perdido",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [180, 150, 60, 255],
            "X": [90, 85, 80, 255],
            "O": [60, 100, 180, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . # # # # # # # # # # . . . .
. . # X X X X X X X X # . . . .
. . # X O O X X O O X # . . . .
. . . # X O O O O X # . . . . .
. . . # X X O O X X # . . . . .
. . . # X X X X X X # . . . . .
. . . . # X X X X # . . . . . .
. . . . # X X X X # . . . . . .
. . . . . # X X # . . . . . . .
. . . . . . # # . . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "componente_combate_stage2_completo": {
        "display_name": "Olho da Forja Ancestral",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [30, 40, 90, 255],
            "X": [40, 120, 50, 255],
            "O": [200, 80, 120, 255],
            "+": [240, 240, 255, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . # # # # . . . . . . .
. . . . # X O O X # . . . . . .
. . . # X O + + O X # . . . . .
. . # X O + + + + O X # . . . .
. # X O + + + + + + O X # . . .
. # X O + + + + + + O X # . . .
. . # X O + + + + O X # . . . .
. . . # X O + + O X # . . . . .
. . . . # X O O X # . . . . . .
. . . . . # # # # . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "lamina_conquistador": {
        "display_name": "Punho da Lâmina Sacrificada",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [220, 170, 40, 255],
            "X": [30, 25, 30, 255],
            "O": [200, 40, 50, 255],
            "+": [255, 230, 120, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . . # . . . . . . . .
. . . . . . # # # . . . . . . .
. . . . . . # + # . . . . . . .
. . . . . . # + # . . . . . . .
. . . . . # # + # # . . . . . .
. . . . # # # # # # # . . . . .
. . . . . # O O O # . . . . . .
. . . . . . X X X . . . . . . .
. . . . . . X X X . . . . . . .
. . . . . . # # # . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "olho_desbravador_primal": {
        "display_name": "Visor das Doze Dimensões",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [40, 60, 140, 255],
            "X": [50, 140, 80, 255],
            "O": [160, 40, 50, 255],
            "+": [240, 240, 255, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . # # # # . . . . . . .
. . . . # X X O O # . . . . . .
. . . # X X X O O O # . . . . .
. . # X X X + + O O O # . . . .
. . # X X + + + + O O # . . . .
. . # X X + + + + O O # . . . .
. . # X X X + + O O O # . . . .
. . . # X X X O O O # . . . . .
. . . . # X X O O # . . . . . .
. . . . . # # # # . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
}


def write_item(item_id: str, spec: dict) -> None:
    base = TEXTURES / "item" / item_id
    option = base / "options" / "a"
    option.mkdir(parents=True, exist_ok=True)
    (base / "meta.json").write_text(
        json.dumps(
            {
                "id": f"nerdkube:{item_id}",
                "type": "item",
                "display_name": spec["display_name"],
                "module": "exploration",
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
        json.dumps({"name": spec["display_name"], "theme": "exploration"}, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )
    (option / "palette.json").write_text(json.dumps(spec["palette"], indent=2) + "\n", encoding="utf-8")
    (option / "matrix.txt").write_text(spec["matrix"] + "\n", encoding="utf-8")


def main() -> None:
    for item_id, spec in ITEMS.items():
        write_item(item_id, spec)
        print(f"created item {item_id}")


if __name__ == "__main__":
    main()
