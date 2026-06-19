# Bases de texturas — NerdKube

Documentação e matrizes-fonte das texturas do mod. Cada entrada tem um `meta.json` na raiz e as opções visuais em `options/a`, `options/b`, etc.

## Estrutura

```
docs/textures/block/cube_maker/
├── meta.json              # active_option: "a" | "b" | "c"
└── options/
    ├── a/
    │   ├── option.json
    │   ├── palette.json
    │   ├── side.matrix.txt
    │   └── top.matrix.txt
    ├── b/
    └── c/
```

Itens e pedestais usam o mesmo padrão (`a` / `b`; blocos centrais também têm `c`).

## Gerar PNGs

```powershell
python tools/generate_textures.py
```

Gera **somente** os PNGs canônicos (`cube_maker_side.png`, `nerd_cube.png`, etc.) da opção ativa.

Troca de opção: edite `active_option` em `meta.json` (ou `config/nerdkube-common.toml` para cube_maker/nerd_cube) e rode o gerador.

```toml
[textures]
  cubeMakerVariant = "a"
  nerdCubeVariant = "a"
```

## Bootstrap

```powershell
python tools/bootstrap_central_block_docs.py
python tools/bootstrap_pedestal_texture_docs.py
```
