#!/usr/bin/env python3
"""Cria matrizes de textura dos 8 itens intermediários da progressão tech."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TEXTURES = ROOT / "docs" / "textures" / "item"

ITEMS = {
    "fragmento_matriz_dados": {
        "display_name": "Fragmento de Matriz de Dados",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [28, 72, 28, 255],
            "X": [184, 115, 51, 255],
            "O": [120, 50, 180, 255],
            "+": [20, 20, 20, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. # # # # # # # # # # # . . . .
. # X X X . . . . . O # . . . .
. # X + + X . . . O O # . . . .
. # X + + X . . O O O # . . . .
. # X X X . . O O O O # . . . .
. # . . . . O O O O O # . . . .
. # . . . O O O O O . # . . . .
. # . . O O O O O . . # . . . .
. # . O O O O O . . . # . . . .
. # O O O O O . . . . # . . . .
. # # # # # # # # # # # . . . .
""".strip(),
    },
    "processador_antimateria_insana": {
        "display_name": "Processador de Antimatéria Insana",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [90, 90, 95, 255],
            "X": [120, 40, 160, 255],
            "O": [60, 220, 230, 255],
            "+": [255, 255, 200, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . # # # # # . . . . . .
. . . . # X X X X X # . . . . .
. . . . # O O O O O # . . . . .
. . . . # + + + + + # . . . . .
. . . . # X X X X X # . . . . .
. . . . # O O O O O # . . . . .
. . . . # X X X X X # . . . . .
. . . . # # # # # # # . . . . .
. . . . . # . . . # . . . . . .
. . . . . # . . . # . . . . . .
. . . . . # . . . # . . . . . .
""".strip(),
    },
    "mecanismo_sombra_corrompido": {
        "display_name": "Mecanismo Corrompido por Sombra",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [25, 45, 90, 255],
            "X": [15, 15, 20, 255],
            "+": [40, 200, 180, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . # . . # . . . . . . .
. . . . # # # # # # . . . . . .
. . . # # X X X X # # . . . . .
. . # # X + . . + X # # . . . .
. # # X . . . . . . X # # . . .
. # # X . . . . . . X # # . . .
. . # # X + . . + X # # . . . .
. . . # # X X X X # # . . . . .
. . . . # # # # # # . . . . . .
. . . . . # . . # . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "nucleo_logico_infundido": {
        "display_name": "Núcleo Lógico Infundido",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [90, 90, 95, 255],
            "X": [40, 160, 60, 255],
            "O": [60, 200, 220, 255],
            "+": [200, 255, 255, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . # # # # . . . . . . .
. . . . # . . . . # . . . . . .
. . . # . . X X . . # . . . . .
. . # . . X O O X . . # . . . .
. . # . X O + + O X . # . . . .
. . # . X O + + O X . # . . . .
. . # . . X O O X . . # . . . .
. . . # . . X X . . # . . . . .
. . . . # . . . . # . . . . . .
. . . . . # # # # . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "matriz_modular_estabilizada": {
        "display_name": "Matriz Modular Estabilizada",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [200, 80, 140, 255],
            "X": [230, 240, 255, 255],
            "O": [80, 200, 240, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . . . # . . . . . . . .
. . . . . . # X # . . . . . . .
. . . . . # X O X # . . . . . .
. . . . . # X O X # . . . . . .
. . . . # X O O O X # . . . . .
. . . . # X O O O X # . . . . .
. . . . # X O O O X # . . . . .
. . . . . # X O X # . . . . . .
. . . . . # X X X # . . . . . .
. . . . . . # # # . . . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "chassi_cyber_flux": {
        "display_name": "Chassi Cyber-Flux",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [200, 40, 40, 255],
            "X": [70, 70, 75, 255],
            "O": [240, 200, 40, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. # . # . # . # . # . # . . . .
. # # # # # # # # # # # . . . .
. # X X X X X X X X X # . . . .
. # X X X X X X X X X # . . . .
. # X X X O O O X X X # . . . .
. # X X X O O O X X X # . . . .
. # X X X O O O X X X # . . . .
. # X X X X X X X X X # . . . .
. # X X X X X X X X X # . . . .
. # # # # # # # # # # # . . . .
. # . # . # . # . # . # . . . .
""".strip(),
    },
    "disco_materia_escura": {
        "display_name": "Disco de Matéria Escura Condensada",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [60, 20, 80, 255],
            "X": [15, 10, 20, 255],
            "O": [100, 100, 110, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . # # # # # # . . . . . .
. . . # # X X X X # # . . . . .
. . # # X O O O O X # # . . . .
. . # X O O O O O O X # . . . .
. . # X O O O O O O X # . . . .
. . # X O O O O O O X # . . . .
. . # # X O O O O X # # . . . .
. . . # # X X X X # X . . . . .
. . . . # # # # # # X . . . . .
. . . . . . . . . . X . . . . .
. . . . . . . . . . . . . . . .
""".strip(),
    },
    "singularidade_hipercarregada": {
        "display_name": "Singularidade Hipercarregada",
        "palette": {
            ".": [0, 0, 0, 0],
            "#": [40, 180, 60, 255],
            "X": [180, 200, 190, 255],
            "O": [5, 5, 8, 255],
        },
        "matrix": """
. . . . . . . . . . . . . . . .
. . . . . X X X X . . . . . . .
. . . . X X O O X X . . . . . .
. . # # # # O O # # # # . . . .
. # # X X O O O O X X # # . . .
# # X X O O O O O O X X # # . .
. # # X X O O O O X X # # . . .
. . # # # # O O # # # # . . . .
. . . . X X O O X X . . . . . .
. . . . . X X X X . . . . . . .
. . . . . . . . . . . . . . . .
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
                    "module": "tech",
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
            json.dumps({"name": spec["display_name"], "theme": "tech"}, indent=2, ensure_ascii=False) + "\n",
            encoding="utf-8",
        )
        (option / "palette.json").write_text(
            json.dumps(spec["palette"], indent=2) + "\n", encoding="utf-8"
        )
        (option / "matrix.txt").write_text(spec["matrix"] + "\n", encoding="utf-8")
        print(f"created {item_id}")


if __name__ == "__main__":
    main()
