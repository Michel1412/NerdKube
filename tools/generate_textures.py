#!/usr/bin/env python3
"""
Gera PNGs 16x16 a partir das bases em docs/textures/.

Estrutura por textura:
  docs/textures/<item|block>/<nome>/
    meta.json              # active_option escolhe a pasta em options/
    options/
      a/ | b/ | c/
        option.json
        palette.json
        matrix.txt         # item ou bloco single
        side.matrix.txt    # bloco side_top
        top.matrix.txt

Gera apenas os PNGs canônicos definidos em meta.json output_png.

Uso:
  python tools/generate_textures.py
"""

from __future__ import annotations

import json
import re
from dataclasses import dataclass
from pathlib import Path

try:
    from PIL import Image
except ImportError:
    raise SystemExit("Instale Pillow: pip install Pillow")

ROOT = Path(__file__).resolve().parents[1]
DOCS_TEXTURES = ROOT / "docs" / "textures"
CONFIG_PATH = ROOT / "config" / "nerdkube-common.toml"
SIZE = 16

CONFIG_KEY_BY_TEXTURE = {
    "cube_maker": "cubeMakerVariant",
    "nerd_cube": "nerdCubeVariant",
}


@dataclass
class TextureOption:
    key: str
    name: str
    dir: Path
    palette: dict[str, tuple[int, int, int, int]]
    face_layout: str  # "single" | "side_top"


def load_json(path: Path) -> dict:
    with path.open(encoding="utf-8") as f:
        return json.load(f)


def load_matrix(path: Path) -> list[str]:
    lines = path.read_text(encoding="utf-8").splitlines()
    return [line for line in lines if line.strip()]


def load_palette(path: Path) -> dict[str, tuple[int, int, int, int]]:
    raw = load_json(path)
    return {key: tuple(value) for key, value in raw.items()}


def matrix_to_image(rows: list[str], palette: dict[str, tuple[int, int, int, int]]) -> Image.Image:
    grid = [row.split() for row in rows]
    height = len(grid)
    width = max(len(r) for r in grid)

    img = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    offset_y = (SIZE - height) // 2
    offset_x = (SIZE - width) // 2

    for y, row in enumerate(grid):
        row_offset_x = offset_x + (width - len(row)) // 2
        for x, ch in enumerate(row):
            color = palette.get(ch, (255, 0, 255, 255))
            px, py = row_offset_x + x, offset_y + y
            if 0 <= px < SIZE and 0 <= py < SIZE:
                img.putpixel((px, py), color)

    return img


def save_image(image: Image.Image, path: Path) -> Path:
    path.parent.mkdir(parents=True, exist_ok=True)
    image.save(path)
    return path


def read_config_options() -> dict[str, str]:
    if not CONFIG_PATH.exists():
        return {}
    text = CONFIG_PATH.read_text(encoding="utf-8")
    result: dict[str, str] = {}
    for texture_name, config_key in CONFIG_KEY_BY_TEXTURE.items():
        match = re.search(rf"{re.escape(config_key)}\s*=\s*\"([^\"]+)\"", text)
        if match:
            result[texture_name] = match.group(1)
    return result


def detect_face_layout(option_dir: Path, meta: dict) -> str:
    layout = meta.get("face_layout", "single")
    if layout in ("cube_all", "single"):
        return "single"
    if layout == "side_top" or (option_dir / "side.matrix.txt").exists():
        return "side_top"
    return "single"


def list_option_dirs(tex_dir: Path) -> list[Path]:
    options_root = tex_dir / "options"
    if not options_root.is_dir():
        return []
    return sorted(
        path for path in options_root.iterdir()
        if path.is_dir() and (path / "palette.json").exists()
    )


def load_texture_option(option_dir: Path, meta: dict) -> TextureOption:
    option_meta_path = option_dir / "option.json"
    option_meta = load_json(option_meta_path) if option_meta_path.exists() else {}
    return TextureOption(
        key=option_dir.name,
        name=option_meta.get("name", option_dir.name),
        dir=option_dir,
        palette=load_palette(option_dir / "palette.json"),
        face_layout=detect_face_layout(option_dir, meta),
    )


def resolve_active_option(tex_dir: Path, meta: dict, config_options: dict[str, str]) -> str:
    texture_name = tex_dir.name
    if texture_name in config_options:
        chosen = config_options[texture_name]
        if (tex_dir / "options" / chosen).is_dir():
            return chosen
        print(f"Aviso: {CONFIG_KEY_BY_TEXTURE[texture_name]}={chosen} não existe em {texture_name}/options/")

    active = meta.get("active_option") or meta.get("active_variant")
    if active and (tex_dir / "options" / active).is_dir():
        return active

    option_dirs = list_option_dirs(tex_dir)
    if not option_dirs:
        raise SystemExit(f"Sem opções em {tex_dir.relative_to(ROOT)}/options/")
    return option_dirs[0].name


def render_option(option: TextureOption) -> dict[str, Image.Image]:
    if option.face_layout == "side_top":
        return {
            "side": matrix_to_image(load_matrix(option.dir / "side.matrix.txt"), option.palette),
            "top": matrix_to_image(load_matrix(option.dir / "top.matrix.txt"), option.palette),
        }
    matrix_file = option.dir / "matrix.txt"
    if not matrix_file.exists():
        raise FileNotFoundError(f"matrix.txt ausente em {option.dir}")
    return {
        "single": matrix_to_image(load_matrix(matrix_file), option.palette),
    }


def export_textures(tex_dir: Path, config_options: dict[str, str]) -> None:
    meta_path = tex_dir / "meta.json"
    if not meta_path.exists():
        return

    meta = load_json(meta_path)
    if not list_option_dirs(tex_dir):
        return

    active_key = resolve_active_option(tex_dir, meta, config_options)
    option = load_texture_option(tex_dir / "options" / active_key, meta)
    rendered = render_option(option)

    outputs: list[Path] = []
    if option.face_layout == "side_top":
        for face in ("side", "top"):
            outputs.append(save_image(rendered[face], ROOT / meta["output_png"][face]))
    else:
        outputs.append(save_image(rendered["single"], ROOT / meta["output_png"]))

    texture_id = meta.get("id", tex_dir.name)
    print(
        f"{texture_id}: opção {active_key} ({option.name}) -> "
        + ", ".join(str(p.relative_to(ROOT)) for p in outputs)
    )


def main() -> None:
    config_options = read_config_options()
    if config_options:
        print(f"Config: {config_options}")

    for category in ("item", "block"):
        category_root = DOCS_TEXTURES / category
        if not category_root.is_dir():
            continue
        for tex_dir in sorted(category_root.iterdir()):
            if tex_dir.is_dir():
                export_textures(tex_dir, config_options)

    print(f"\nBases em: {DOCS_TEXTURES.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
