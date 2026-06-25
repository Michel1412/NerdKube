# Progressão Magia — NerdKube 0.4.0

Referência canônica da cadeia Expert do pedestal de magia (sul) e do amuleto `coracao_arcano`.

## O que mudou na 0.4.0

- Removido `fragmento_magia_stage1` (Runa de Sangue Inerente)
- `oferta_poco_sombria` (Oferenda ao Abismo) — craft shapeless (Mesa de Encantamentos + bastão de relâmpago + nether star)
- 5 itens intermediários da linha magia (lore em `ProgressionLoreItem`)

## Fluxo resumido

```
Pedestal Magia (Occultism Ritual craft_afrit)
    ↓
Oferenda ao Abismo (craft shapeless — minecraft:enchanting_table + lightning_rod + nether_star)
    ↓
Coração Sombrio Purificado (lançar oferenda num buraco sem fundo visível)
    ↓
Pergaminho de Geometria Proibida (craft — proxy Inscription Table)
    ↓
Ídolo de Cinzas e Almas (Malum Spirit Infusion)
    ↓
Anel de Ancoragem Celestial (craft pentagrama)
Plasma de Sangue Condenado (EvilCraft Blood Infuser + ruined_book, 600k mB sangue)
    ↓
Coração Arcano (Ars Nouveau Enchanting Apparatus)
```

## Receitas (arquivo → máquina)

| Etapa | Arquivo | Máquina | Saída |
|-------|---------|---------|-------|
| A | `pedestal_magia_ritual.json` | Occultism Ritual (`craft_afrit`) | `pedestal_magia` |
| B | `magia/oferta_poco_sombria_craft.json` | Bancada shapeless | `oferta_poco_sombria` |
| C | `componente_magia_stage1_void_favor.json` | Poço sem fundo (`malum:void_favor`) | `componente_magia_stage1_completo` |
| D | `fragmento_magia_stage2_craft.json` | Bancada 3×3 | `fragmento_magia_stage2` |
| E | `componente_magia_stage2_spirit_infusion.json` | Malum Spirit Altar | `componente_magia_stage2_completo` |
| F | `matriz_runica_craft.json` | Bancada pentagrama | `matriz_runica` |
| G | `essencia_vital_instavel_blood_infuser.json` | EvilCraft Blood Infuser (tier III, 600k mB) | `essencia_vital_instavel` |
| H | `coracao_arcano_apparatus.json` | Ars Nouveau Enchanting Apparatus | `coracao_arcano` |

## Notas de design

### Oferenda ao Abismo

- **Sem receita datapack** — obtenção mística via JEI: *"misture os relâmpagos no Tablet de uma criatura misteriosa"*
- Depois: lançar no buraco sem fundo visível (`malum:void_favor`) → Coração Sombrio Purificado

### Enchanting Apparatus (8 pedestais)

O Apparatus tem **8** pedestais arcanos. O craft final usa:
- 2× `essencia_vital_instavel`
- 1× `occultism:miner_marid_master`
- 3× `componente_magia_stage1_completo`
- 2× `componente_magia_stage2_completo` (terceiro comp. S2 só na cadeia intermediária)

## JEI

- **Ingredient Info** (R) — resumo de uso e obtenção por item
- **Ritual NerdCube** — layout do craft final

## Teste

1. `.\gradlew build`
2. `/reload` — verificar log sem erros de receita
3. JEI (R): `oferta_poco_sombria` — texto místico, sem receita
4. Poço: lançar oferenda → `componente_magia_stage1_completo`
5. Ritual CubeMaker: `coracao_arcano` no pedestal **sul**
