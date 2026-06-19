# Referência do ritual NerdCube

Snapshot para desenvolvimento. A API BlocksItems é **somente referência** — não usada em runtime.

## Layout do ritual (vista superior)

```
        [N] pedestal_tech + nucleo_de_materia
                    |
[W] pedestal_agricultura — [CubeMaker] — [E] pedestal_exploracao
    + semente_criacao              + reliquia_desbravador
                    |
        [S] pedestal_magia + coracao_arcano
```

Distância centro-a-centro entre `cube_maker` e cada pedestal temático: **3 blocos**.

## Pedestais temáticos

| Direção | Bloco | Amuleto |
|---------|-------|---------|
| Norte | `nerdkube:pedestal_tech` | `nerdkube:nucleo_de_materia` |
| Sul | `nerdkube:pedestal_magia` | `nerdkube:coracao_arcano` |
| Leste | `nerdkube:pedestal_exploracao` | `nerdkube:reliquia_desbravador` |
| Oeste | `nerdkube:pedestal_agricultura` | `nerdkube:semente_criacao` |

Cada pedestal aceita **somente** o amuleto do seu tema. Colocar o pedestal errado na direção (ex.: `pedestal_magia` no norte) impede o craft.

Bases de textura (matrizes A/B, paletas, `side` + `top`): [`docs/textures/block/`](../textures/README.md). Regenerar PNGs: `python tools/generate_textures.py` (variante ativa em cada `meta.json`).

## Componentes nerdkube (amuletos)

| ID | Pedestal | Textura |
|----|----------|---------|
| `nerdkube:nucleo_de_materia` | Norte | Ver [`docs/textures/item/nucleo_de_materia/`](../textures/item/nucleo_de_materia/) |
| `nerdkube:coracao_arcano` | Sul | Ver [`docs/textures/item/coracao_arcano/`](../textures/item/coracao_arcano/) |
| `nerdkube:reliquia_desbravador` | Leste | Ver [`docs/textures/item/reliquia_desbravador/`](../textures/item/reliquia_desbravador/) |
| `nerdkube:semente_criacao` | Oeste | Ver [`docs/textures/item/semente_criacao/`](../textures/item/semente_criacao/) |

Centro do fabricador: `minecraft:crafter` (Fabricador vanilla 1.21)

O amuleto `nucleo_de_materia` é obtido pela cadeia técnica documentada em [`tech-progression.md`](tech-progression.md).

Ao concluir o craft com sucesso, os quatro pedestais são removidos (sem drop) e fogos de artifício coloridos são lançados em cada posição.

## Migração

O bloco genérico `nerdkube:ritual_pedestal` foi removido. Mundos de teste precisam recolocar os quatro pedestais temáticos nas direções corretas.

## Dados persistentes

| Chave | Tipo | Descrição |
|-------|------|-----------|
| `nerdkube:has_crafted_trophy` | Player attachment | Trava 1 craft por jogador |
| `nerdkube:crafted_by` | Data component | Assinatura no item/bloco troféu |

## API BlocksItems (consulta dev)

```
GET https://blocksitems.com/api/v1/items?mod_id=mekanism&rarity=epic
GET https://blocksitems.com/api/v1/items?mod_id=endrem
GET https://blocksitems.com/api/v1/blocks?mod_id=mysticalagriculture&search=infusion
```

Exemplos de IDs epic úteis para receitas futuras (fase 2):

- `mekanism:alloy_atomic`, `mekanism:meka_tool`
- `endrem:cryptic_eye`, `endrem:wither_eye`
- `irons_spellbooks:eldritch_manuscript`
- `oritech:promethium_pickaxe`
