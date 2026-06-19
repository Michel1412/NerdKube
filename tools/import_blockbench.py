#!/usr/bin/env python3
"""
Importa modelos Blockbench + texturas para assets do mod.

Coloque na pasta de trabalho (padrão: docs/blockbench/):
  {nome}.json   — export Java Block/Item do Blockbench
  {nome}.png    — textura principal (e outras PNGs referenciadas no JSON)

Uso:
  python tools/import_blockbench.py
  python tools/import_blockbench.py pedestal_tech
  python tools/import_blockbench.py -s docs/blockbench pedestal_tech nucleo_de_materia
  python tools/import_blockbench.py --dry-run --all

O script:
  - corrige namespace das texturas para nerdkube:block/... ou nerdkube:item/...
  - remove rotações multi-eixo incompatíveis com Java (com aviso)
  - copia PNGs e JSON para src/main/resources/assets/nerdkube/
  - cria blockstates + model/item para blocos, se ainda não existirem
"""

from __future__ import annotations

import argparse
import json
import re
import shutil
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DEFAULT_SOURCE = ROOT / "docs" / "blockbench"
ASSETS = ROOT / "src" / "main" / "resources" / "assets" / "nerdkube"
MOD_ID = "nerdkube"

STRIP_MODEL_KEYS = frozenset({"format_version", "credit", "groups"})


def load_registered_ids(java_path: Path) -> set[str]:
    if not java_path.exists():
        return set()
    text = java_path.read_text(encoding="utf-8")
    return set(re.findall(r'register(?:SimpleItem)?\("([^"]+)"', text))


def detect_asset_type(name: str) -> str:
    blocks = load_registered_ids(ROOT / "src/main/java/br/com/nerdskube/registry/ModBlocks.java")
    if name in blocks:
        return "block"
    return "item"


def texture_basename(value: str) -> str:
    if value.startswith("#"):
        return value
    tail = value.split("/")[-1]
    if ":" in tail:
        tail = tail.split(":", 1)[1]
    return tail


def normalize_texture_path(value: str, category: str) -> str:
    if value.startswith("#"):
        return value
    return f"{MOD_ID}:{category}/{texture_basename(value)}"


def is_invalid_java_rotation(rotation: object) -> bool:
    return isinstance(rotation, dict) and (
        "x" in rotation or "y" in rotation or "z" in rotation
    )


def fix_blockbench_model(model: dict, name: str, category: str) -> tuple[dict, list[str]]:
    warnings: list[str] = []
    cleaned = {key: value for key, value in model.items() if key not in STRIP_MODEL_KEYS}

    textures = cleaned.get("textures")
    if isinstance(textures, dict):
        fixed_textures: dict[str, str] = {}
        for key, value in textures.items():
            if isinstance(value, str):
                fixed_textures[key] = normalize_texture_path(value, category)
            else:
                fixed_textures[key] = value
        cleaned["textures"] = fixed_textures

    elements = cleaned.get("elements")
    if isinstance(elements, list):
        for element in elements:
            if not isinstance(element, dict):
                continue
            rotation = element.get("rotation")
            if is_invalid_java_rotation(rotation):
                element_name = element.get("name", "?")
                warnings.append(
                    f"Removida rotação multi-eixo do elemento '{element_name}' "
                    f"(faça Bake Model no Blockbench para manter a inclinação)."
                )
                del element["rotation"]

    if "textures" not in cleaned and "parent" not in cleaned:
        warnings.append("Modelo sem 'textures' nem 'parent'; verifique o export do Blockbench.")

    if isinstance(cleaned.get("textures"), dict):
        has_primary = any(
            not str(path).startswith("#")
            for path in cleaned["textures"].values()
            if isinstance(path, str)
        )
        if has_primary:
            primary = f"{MOD_ID}:{category}/{name}"
            if cleaned["textures"].get("0") != primary and "layer0" not in cleaned["textures"]:
                cleaned["textures"].setdefault("0", primary)
            cleaned["textures"].setdefault("particle", primary)

    return cleaned, warnings


def referenced_texture_names(model: dict) -> set[str]:
    names: set[str] = set()
    textures = model.get("textures")
    if not isinstance(textures, dict):
        return names
    for value in textures.values():
        if isinstance(value, str) and not value.startswith("#"):
            names.add(texture_basename(value))
    return names


def write_json(path: Path, data: dict, dry_run: bool) -> None:
    text = json.dumps(data, indent=2, ensure_ascii=False) + "\n"
    if dry_run:
        print(f"  [dry-run] escreveria {path.relative_to(ROOT)}")
        return
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(text, encoding="utf-8")


def copy_file(src: Path, dest: Path, dry_run: bool) -> None:
    if dry_run:
        print(f"  [dry-run] copiaria {src.relative_to(ROOT)} -> {dest.relative_to(ROOT)}")
        return
    dest.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(src, dest)


def ensure_block_sidecars(name: str, dry_run: bool) -> None:
    blockstate_path = ASSETS / "blockstates" / f"{name}.json"
    item_model_path = ASSETS / "models" / "item" / f"{name}.json"

    if not blockstate_path.exists():
        write_json(
            blockstate_path,
            {"variants": {"": {"model": f"{MOD_ID}:block/{name}"}}},
            dry_run,
        )
        print(f"  + blockstate criado: {blockstate_path.relative_to(ROOT)}")

    if not item_model_path.exists():
        write_json(
            item_model_path,
            {"parent": f"{MOD_ID}:block/{name}"},
            dry_run,
        )
        print(f"  + model/item criado: {item_model_path.relative_to(ROOT)}")


def discover_names(source_dir: Path) -> list[str]:
    names = sorted(
        {
            path.stem
            for path in source_dir.glob("*.json")
            if path.is_file() and (source_dir / f"{path.stem}.png").exists()
        }
    )
    return names


def import_asset(name: str, source_dir: Path, category: str | None, dry_run: bool) -> bool:
    model_src = source_dir / f"{name}.json"
    texture_src = source_dir / f"{name}.png"

    if not model_src.exists():
        print(f"[erro] {name}: falta {model_src.relative_to(ROOT)}", file=sys.stderr)
        return False
    if not texture_src.exists():
        print(f"[erro] {name}: falta {texture_src.relative_to(ROOT)}", file=sys.stderr)
        return False

    asset_type = category or detect_asset_type(name)
    texture_category = asset_type

    with model_src.open(encoding="utf-8") as handle:
        model = json.load(handle)

    fixed_model, warnings = fix_blockbench_model(model, name, texture_category)
    for warning in warnings:
        print(f"  [aviso] {name}: {warning}")

    if asset_type == "block":
        model_dest = ASSETS / "models" / "block" / f"{name}.json"
        texture_dest = ASSETS / "textures" / "block" / f"{name}.png"
        ensure_block_sidecars(name, dry_run)
    else:
        model_dest = ASSETS / "models" / "item" / f"{name}.json"
        texture_dest = ASSETS / "textures" / "item" / f"{name}.png"

    write_json(model_dest, fixed_model, dry_run)
    copy_file(texture_src, texture_dest, dry_run)

    for tex_name in referenced_texture_names(fixed_model):
        if tex_name == name:
            continue
        extra_src = source_dir / f"{tex_name}.png"
        extra_dest = ASSETS / "textures" / texture_category / f"{tex_name}.png"
        if extra_src.exists():
            copy_file(extra_src, extra_dest, dry_run)
        else:
            print(
                f"  [aviso] {name}: textura referenciada '{tex_name}.png' não encontrada em "
                f"{source_dir.relative_to(ROOT)}"
            )

    print(
        f"[ok] {name} ({asset_type}) -> "
        f"{model_dest.relative_to(ROOT)}, {texture_dest.relative_to(ROOT)}"
    )
    return True


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Importa modelos Blockbench para assets do NerdKube.")
    parser.add_argument(
        "names",
        nargs="*",
        help="Nomes dos assets (sem extensão). Vazio = todos os pares .json+.png na pasta.",
    )
    parser.add_argument(
        "-s",
        "--source",
        type=Path,
        default=DEFAULT_SOURCE,
        help=f"Pasta com os arquivos Blockbench (padrão: {DEFAULT_SOURCE.relative_to(ROOT)})",
    )
    parser.add_argument(
        "-t",
        "--type",
        choices=("block", "item"),
        help="Força tipo do asset (padrão: detecta via ModBlocks.java)",
    )
    parser.add_argument(
        "--all",
        action="store_true",
        help="Importa todos os pares .json + .png da pasta (equivalente a não passar nomes).",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Mostra o que seria feito sem gravar arquivos.",
    )
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    source_dir = args.source.resolve()

    if not source_dir.exists():
        source_dir.mkdir(parents=True, exist_ok=True)
        print(f"Criada pasta de trabalho: {source_dir.relative_to(ROOT)}")
        print("Coloque lá seus pares {nome}.json + {nome}.png e rode o script de novo.")
        return 0

    if args.names:
        names = args.names
    else:
        names = discover_names(source_dir)
        if not names:
            print(f"Nenhum par .json + .png encontrado em {source_dir.relative_to(ROOT)}")
            print("Exemplo: docs/blockbench/pedestal_tech.json e pedestal_tech.png")
            return 1

    ok = True
    for name in names:
        if not import_asset(name, source_dir, args.type, args.dry_run):
            ok = False

    if ok and not args.dry_run:
        print("Pronto. Rode: .\\gradlew runClient")
    return 0 if ok else 1


if __name__ == "__main__":
    raise SystemExit(main())
