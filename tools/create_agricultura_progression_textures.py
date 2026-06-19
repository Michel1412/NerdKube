#!/usr/bin/env python3
"""Cria matrizes de textura dos itens intermediários da progressão agricultura."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TEXTURES = ROOT / "docs" / "textures"

ITEMS = {
    "fragmento_agri_stage1": {
        "display_name": "Semente de Matéria Cósmica",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [90, 30, 120, 255],
            "X": [40, 20, 50, 255],
            "O": [240, 240, 255, 255],
            "+": [220, 180, 80, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . # . . . . . . . . .
. . . . . # X # . . . . . . . .
. . . . # X O X # . . . . . . .
. . . . # X O X # . . . . . . .
. . . # X O + O X # . . . . . .
. . . # X O + O X # . . . . . .
. . # X X O + O X X # . . . . .
. . # X X X O X X X # . . . . .
. . . # X X X X X # . . . . . .
. . . . # # # # # . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "pacote_essencias_agri": {
        "display_name": "Pacote de Essências Agrícolas",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [120, 80, 40, 255],
            "X": [180, 140, 60, 255],
            "O": [200, 50, 50, 255],
            "+": [240, 200, 80, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . # # # # # # . . . . . .
. . . # X X X X X X # . . . . .
. . # X O O O O O O X # . . . .
. . # X O + + + + O X # . . . .
. . # X O + O O + O X # . . . .
. . # X O + + + + O X # . . . .
. . # X O O O O O O X # . . . .
. . . # X X X X X X # . . . . .
. . . . # # # # # # . . . . . .
. . . . . . . . . . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "componente_agri_stage1_completo": {
        "display_name": "Casulo de Mel Supremium",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [200, 40, 40, 255],
            "X": [220, 140, 40, 255],
            "O": [240, 210, 60, 255],
            "+": [255, 180, 30, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . # # # # . . . . . . .
. . . . # X O O X # . . . . . .
. . . # X O O O O X # . . . . .
. . # X O O X X O O X # . . . .
. . # X O X # # X O X # . . . .
. . # X O X # # X O X # . . . .
. . # X O O X X O O X # . . . .
. . . # X O O O O X # . . . . .
. . . . # X O O X # + . . . . .
. . . . . # # # # . + . . . . .
. . . . . . . . . . + . . . . .
""".strip(),
    },
    "massa_runica_crua": {
        "display_name": "Massa Rúnica Crua",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [140, 95, 55, 255],
            "X": [100, 65, 40, 255],
            "O": [248, 238, 215, 255],
            "+": [170, 130, 210, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . . . . . . . . . . .
. . . . # # # # # # . . . . . .
. . . # X X X X X X # . . . . .
. . # X O O O O O O X # . . . .
. . # X O O + O O O X # . . . .
. . # X O O O O O O X # . . . .
. . . # X X X X X X # . . . . .
. . . . # # # # # # . . . . . .
. . . . . . . . . . . . . . . .
. . . . . . . . . . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "runic_crystal_cake": {
        "display_name": "Bolo Rúnico de Cristal",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [100, 65, 35, 255],
            "X": [220, 60, 60, 255],
            "O": [160, 30, 40, 255],
            "+": [255, 120, 140, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . + X + X . . . . . . .
. . . . # X X X X # . . . . . .
. . . # X X O O X X # . . . . .
. . # X X O O O O X X # . . . .
. . # O O O O O O O O # . . . .
. . # # # # # # # # # # . . . .
. . # # # # # # # # # # . . . .
. . # # # # # # # # # # . . . .
. . # # # # # # # # # # . . . .
. . . # # # # # # # # . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "raizes_doces": {
        "display_name": "Raízes Doces",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [110, 75, 45, 255],
            "X": [230, 140, 50, 255],
            "O": [60, 140, 50, 255],
        },
        "matrix": """
. . . . . . O . . . . . . . . .
. . . . . O O . . . . . . . . .
. . . . . . # . . . . . . . . .
. . . . . # # # . . . . . . . .
. . . . # # X # # . . . . . . .
. . . . # X X X # . . . . . . .
. . . # # X X X # # . . . . . .
. . . # # # X # # # . . . . . .
. . . . # # # # # . . . . . . .
. . . . . # # # . . . . . . . .
. . . . . . # . . . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "raiz_em_conserva": {
        "display_name": "Raiz em Conserva",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [140, 145, 150, 255],
            "X": [110, 75, 45, 255],
            "O": [220, 180, 60, 255],
            "+": [180, 40, 40, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . + + + + . . . . . . .
. . . . . # # # # . . . . . . .
. . . . # # . . # # . . . . . .
. . . # # # # # # # # . . . . .
. . . # O O O O O O # . . . . .
. . . # O X X X X O # . . . . .
. . . # O X X X X O # . . . . .
. . . # O X X X X O # . . . . .
. . . # O O O O O O # . . . . .
. . . . # # # # # # . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "altar_revestimento_agri": {
        "display_name": "Módulo de Cozinha Imperial",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [100, 65, 35, 255],
            "X": [140, 145, 150, 255],
            "O": [30, 30, 35, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. # # # # # # # # # # # . . . .
. # X X X X X X X X X # . . . .
. # X X X X X X X X X # . . . .
. # # # # # # # # # # # . . . .
. # # O O # # # O O # # . . . .
. # # O O # # # O O # # . . . .
. # # # # # # # # # # # . . . .
. # # # # # # # # # # # . . . .
. # # # # # # # # # # # . . . .
. . # # . . . . . # # . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "massa_nutrientes_supremos": {
        "display_name": "Banquete da Consagração",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [200, 160, 40, 255],
            "X": [220, 120, 50, 255],
            "O": [180, 40, 40, 255],
            "+": [240, 240, 240, 255],
        },
        "matrix": """
. . . . . . + . + . . . . . . .
. . . . . . + + + . . . . . . .
. . . . . O O O O O . . . . . .
. . . . X X X X X X X . . . . .
. . . X X X X X X X X X . . . .
. . # # # # # # # # # # # . . .
. . # # # # # # # # # # # . . .
. . # # # # # # # # # # # . . .
. . . # # # # # # # # # . . . .
. . . . # # # # # # # . . . . .
. . . . . # # # # # . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
}

BLOCKS = {
    "massa_runica_crua": {
        "display_name": "Massa Rúnica Crua (bloco)",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [120, 85, 50, 255],
            "X": [235, 220, 185, 255],
            "O": [250, 245, 235, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . . . . . . . . . . .
. . . . . . # # # . . . . . . .
. . . . . # X X X # . . . . . .
. . . . # X X X O X # . . . . .
. . . # X X O O X X X # . . . .
. . . # X X X X X X X # . . . .
. . # X X X X X O O X X # . . .
. . # X O O X X X X X X # . . .
. . . # X X X X X X X # . . . .
. . . . # # # # # # # . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "bolo_mistico_colocavel": {
        "display_name": "Bolo Místico (bloco)",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [100, 65, 35, 255],
            "X": [220, 60, 60, 255],
            "O": [160, 30, 40, 255],
            "+": [255, 120, 140, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . + X + X . . . . . . .
. . . . # X X X X # . . . . . .
. . . # X X O O X X # . . . . .
. . # X X O O O O X X # . . . .
. . # O O O O O O O O # . . . .
. . # # # # # # # # # # . . . .
. . # # # # # # # # # # . . . .
. . # # # # # # # # # # . . . .
. . # # # # # # # # # # . . . .
. . . # # # # # # # # . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
}

PEDESTAL_BRUTO = {
    "display_name": "Pilar Arquitetônico Inacabado",
    "palette": {
        ".": [0, 0, 0, 0],
        "#": [110, 110, 115, 255],
        "X": [230, 230, 225, 255],
        "O": [70, 100, 55, 255],
    },
    "matrix": """
. . . . . . . . . . . . . . . .
. . . # # # # # # # # . . . . .
. . . # X X X X X X # . . . . .
. . . . # X X X X # . . . . . .
. . . . # O O O O # . . . . . .
. . . . # O O O O # . . . . . .
. . . . # O O O O # . . . . . .
. . . . # O O O O # . . . . . .
. . . . # X X X X # . . . . . .
. . . # X X X X X X # . . . . .
. . . # # # # # # # # . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
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
                "module": "agriculture",
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
        json.dumps({"name": spec["display_name"], "theme": "agriculture"}, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )
    (option / "palette.json").write_text(json.dumps(spec["palette"], indent=2) + "\n", encoding="utf-8")
    (option / "matrix.txt").write_text(spec["matrix"] + "\n", encoding="utf-8")


def write_block(block_id: str, spec: dict) -> None:
    base = TEXTURES / "block" / block_id
    option = base / "options" / "a"
    option.mkdir(parents=True, exist_ok=True)
    (base / "meta.json").write_text(
        json.dumps(
            {
                "id": f"nerdkube:{block_id}",
                "type": "block",
                "display_name": spec["display_name"],
                "module": "agriculture",
                "face_layout": "cube_all",
                "resolution": 16,
                "output_png": f"src/main/resources/assets/nerdkube/textures/block/{block_id}.png",
                "active_option": "a",
            },
            indent=2,
            ensure_ascii=False,
        )
        + "\n",
        encoding="utf-8",
    )
    (option / "option.json").write_text(
        json.dumps({"name": spec["display_name"], "theme": "agriculture"}, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )
    (option / "palette.json").write_text(json.dumps(spec["palette"], indent=2) + "\n", encoding="utf-8")
    (option / "matrix.txt").write_text(spec["matrix"] + "\n", encoding="utf-8")


def write_pedestal_bruto() -> None:
    write_block("pedestal_bruto", PEDESTAL_BRUTO)


def main() -> None:
    for item_id, spec in ITEMS.items():
        write_item(item_id, spec)
        print(f"created item {item_id}")
    for block_id, spec in BLOCKS.items():
        write_block(block_id, spec)
        print(f"created block {block_id}")
    write_pedestal_bruto()
    print("created block pedestal_bruto")


if __name__ == "__main__":
    main()
