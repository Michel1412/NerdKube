# Progressão Agricultura — NerdKube 0.6.0

Referência canônica da cadeia Expert do pedestal de agricultura (oeste) e do amuleto `semente_criacao`.

> **Breaking change 0.6.0:** saves em 0.5.0 com `fragmento_agri_stage2` ou `componente_agri_core_completo` precisam refazer as etapas D–F.

## Fluxo resumido

```
Pedestal bruto (bancada 3×3)
    ↓
Pedestal Agricultura (smithing + Mesa de Carpinteiro)
    ↓
Semente de Matéria Cósmica (MA Infusion Altar)
    ↓
Pacote de Essências → Casulo de Mel Supremium (Productive Bees Centrifuge)
    ↓
Massa Rúnica Crua (Tigela) → ritual no chão / Estação Caking → Bolo Místico → Bolo Rúnico de Cristal
    ↓
Raízes Doces (Ultra Botany Pot + solo Supremium, ~10 min, 1–2 unidades)
    ↓
Raízes Doces Fritas (Farmer's Delight Skillet — receita smelting)
    ↓
Raízes Ancestrais em Conserva (craft: jar Bakery + 3 fritas)
    ↓
Módulo de Cozinha Imperial (bancada decoração)
Banquete da Consagração (FD Cooking Pot)
    ↓
Semente da Criação (FD Cooking Pot final — 1× conserva)
```

## Receitas (arquivo → máquina)

| Etapa | Arquivo | Máquina | Saída |
|-------|---------|---------|-------|
| A | `pedestal_bruto_craft.json` | Bancada 3×3 | `pedestal_bruto` |
| A2 | `pedestal_agricultura_smithing.json` | Smithing (Mesa de Carpinteiro) | `pedestal_agricultura` |
| B | `fragmento_agri_stage1_infusion.json` | Mystical Agriculture Infusion Altar | `fragmento_agri_stage1` |
| C1 | `pacote_essencias_agri_craft.json` | Bancada shapeless | `pacote_essencias_agri` |
| C2 | `componente_agri_stage1_centrifuge.json` | Productive Bees Centrifuge | `componente_agri_stage1_completo` |
| D1 | `massa_runica_crua_crafting_bowl.json` | Farm & Charm Crafting Bowl | `massa_runica_crua` (bloco) |
| D2 | — (ritual Java) | Chão ou `bakery:baker_station` + Cristal Mestre | `runic_crystal_cake` |
| E | `data/botanypots/recipe/nerdkube/crop/agri_core.json` | Ultra Botany Pot + `supremium_farmland` | `raizes_doces` (1–2) |
| E2 | `raizes_doces_fritas_skillet.json` | Farmer's Delight Skillet (smelting) | `raizes_doces_fritas` |
| E3 | `raizes_ancestrais_em_conserva_craft.json` | Bancada shapeless | `raizes_ancestrais_em_conserva` |
| F | `altar_revestimento_agri_craft.json` | Bancada 3×3 | `altar_revestimento_agri` |
| G | `massa_nutrientes_supremos_cooking.json` | Farmer's Delight Cooking Pot | `massa_nutrientes_supremos` |
| H | `semente_criacao_cooking.json` | Farmer's Delight Cooking Pot | `semente_criacao` |

## IDs corrigidos (spec vs pack)

| Spec original | ID no pack NerdCube |
|---------------|---------------------------|
| `macawsroofs:` / `macawsfurniture:` / `macawsfences:` / `macawspaths:` | `mcwroofs:`, `mcwfurnitures:`, `mcwfences:`, `mcwpaths:` |
| `rechiseled:chisled_quartz_block` | `rechiseled:quartz_block_bordered` |
| `refurbished_furniture:fridge` | `refurbished_furniture:light_fridge` |
| `betterblockz:smooth_stone_stairs` | `minecraft:stone_stairs` (vanilla não tem escada de pedra lisa) |
| `macawspaths:stone_path` | `mcwpaths:stone_flagstone_path` |
| `betterblockz:mossy_stone_brick_stairs` | `minecraft:mossy_stone_brick_stairs` |
| `productivebees:comb_supremium` | `productivebees:configurable_honeycomb` + bee type Supremium |
| `farmandcharm:artisanal_cheese` | `farm_and_charm:butter` |
| `botanypotstiers:netherite_botany_pot` | `botanypotstiers:ultra_*_botany_pot` |
| `mysticalagradditions:nether_star_essence` | `mysticalagriculture:nether_star_essence` |

## Notas de design

### Centrífuga mono-ingrediente

A centrífuga do Productive Bees aceita **um** ingrediente por receita. O `pacote_essencias_agri` condensa fragmentos + favo + mel em um único item processável.

### Botany Pots

- Receita datapack: `grow_time` 12000 ticks (~10 min)
- Exige **Ultra Botany Pot** (`botanypotstiers:ultra_oak_botany_pot` ou variante)
- Solo: `mysticalagriculture:supremium_farmland`
- Semente: `runic_crystal_cake`
- Colheita madura: `raizes_doces` (1–2 unidades)
- Fallback Java: `BotanyPotGerminationHandler` se o crop datapack não carregar

### Ritual de panificação (0.6.0)

1. **Tigela de Mistura** (`farm_and_charm:crafting_bowl`) — massa rúnica cruá (bloco colocável)
2. Colocar massa no chão ou em cima da **Estação Caking** (`bakery:baker_station`)
3. Clicar na massa com **Cristal Mestre de Infusão** (não consumido) → bloco `bolo_mistico_colocavel`
4. Mão vazia no bolo místico → item `runic_crystal_cake`

Handlers: `RunicDoughBlock`, `MysticCakeBlock`, `BakerStationDoughHandler`.

### Fritura e conserva (Farmer's Delight)

- **Skillet:** a frigideira do FD usa receitas `minecraft:smelting` — `raizes_doces` → `raizes_doces_fritas` (200 ticks, como ovo frito)
- **Conserva:** craft shapeless `1× bakery:jar` + `3× raizes_doces_fritas` → `raizes_ancestrais_em_conserva`

### Craft final

A Cooking Pot da Semente da Criação usa **2×** casulo de mel, **1×** `raizes_ancestrais_em_conserva`, **2×** banquete e **1×** módulo de cozinha.

### Módulo de Cozinha Imperial (bancada 3×3)

Receita em `altar_revestimento_agri_craft.json` — **craft vanilla** na bancada de trabalho (não usa categoria JEI custom):

| | Col 1 | Col 2 | Col 3 |
|---|-------|-------|-------|
| **Linha 1** | `mcwfurnitures:oak_wardrobe` | `refurbished_furniture:light_fridge` | `mcwfurnitures:oak_bookshelf` |
| **Linha 2** | `mcwfences:oak_picket_fence` | `minecraft:stone_stairs` | `mcwfences:oak_picket_fence` |
| **Linha 3** | `mcwpaths:stone_flagstone_path` | `mcwpaths:stone_flagstone_path` | `mcwpaths:stone_flagstone_path` |

O módulo é **ingrediente físico** na Cooking Pot final (junto com conserva, banquetes e casulos de mel).

### Chipped / Smithing

A transformação `pedestal_bruto → pedestal_agricultura` usa `minecraft:smithing_transform` com `chipped:carpenters_table` (fallback documentado quando Chipped não aceita datapack de item).

## JEI

- **Ingredient Info** (R) — resumo de uso e obtenção por item
- **Ritual NerdCube** — layout do craft final
- Frigideira Farmer's Delight — receita de fritura das raízes

## Teste

1. `.\gradlew build deployToModpack`
2. `/reload` — verificar log sem erros de receita
3. JEI (R) em cada etapa A–H
4. Ritual: massa → cristal → bolo → plantio → fritura → conserva → `semente_criacao` no pedestal **oeste**
