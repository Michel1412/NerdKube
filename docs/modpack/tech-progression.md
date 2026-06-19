# Progressão Técnica — NerdKube 0.3.0

Referência canônica da cadeia Expert do pedestal de tecnologia e do amuleto `nucleo_de_materia`.

## O que mudou na 0.3.0

- 8 itens intermediários da linha tech (nomes temáticos + lore em `ProgressionLoreItem`)
- 11 receitas datapack cross-mod em `data/nerdkube/recipe/tech/` (inclui Goo Spread JDT)
- `mecanismo_sombra_corrompido` via **Just Dire Things Goo Spread (Tier 4)** + `sculk_gear_crystal` (bloco com `facing`)
- Categoria JEI **Ritual NerdCube** + Ingredient Info nos itens
- Renderer 3D do amuleto nos pedestais

## Fluxo resumido

```
Pedestal Tech (Pressure Chamber 16 bar)
    ↓
Fragmento de Matriz de Dados (EnderIO Alloy Smelter)
    ↓
Processador de Antimatéria Insana (Oritech Atomic Forge)
    ↓
Mecanismo Corrompido por Sombra (JDT Goo Spread T4: machine_frame → sculk_gear_crystal → quebrar)
    ↓
Núcleo Lógico Infundido (craft 3×3)
    ↓
Matriz Modular Estabilizada (Actually Additions Empowerer)
    ↓
Chassi Cyber-Flux (craft em cruz)
Disco de Matéria Escura → Singularidade Hipercarregada (Powah 10B FE)
    ↓
Núcleo de Matéria (Pressure Chamber 16 bar)
```

## Receitas (arquivo → máquina)

| Etapa | Arquivo | Máquina | Saída |
|-------|---------|---------|-------|
| A | `pedestal_tech_pressure.json` | PneumaticCraft Pressure Chamber (16 bar, 5×5×5 físico) | `pedestal_tech` |
| B | `fragmento_matriz_dados_alloy.json` | EnderIO Alloy Smelter | `fragmento_matriz_dados` |
| C | `processador_antimateria_insana_atomic.json` | Oritech Atomic Forge (`oritech:atomic_forge_block`; tipo receita `oritech:atomic_forge`) | `processador_antimateria_insana` (2× fragmento + insanite) |
| D | `sculk_gear_goospread.json` | Just Dire Things Goo Spread (Tier 4) | `mecanismo_sombra_corrompido` (via `sculk_gear_crystal`) |
| E | `nucleo_logico_infundido_craft.json` | Bancada 3×3 | `nucleo_logico_infundido` |
| F | `matriz_modular_estabilizada_empowerer.json` | Actually Additions Empowerer | `matriz_modular_estabilizada` |
| G | `chassi_cyber_flux_craft.json` | Bancada em cruz | `chassi_cyber_flux` |
| G.1 | `disco_materia_escura_craft.json` | Bancada (bucket de escuridão) | `disco_materia_escura` |
| G.2 | `singularidade_hipercarregada_powah.json` | Powah Energizing Orb (10e9 FE) | `singularidade_hipercarregada` |
| H | `nucleo_materia_pressure.json` | PneumaticCraft Pressure Chamber (16 bar) | `nucleo_de_materia` |

## Mecanismo Corrompido por Sombra — Goo Spread (Just Dire Things)

1. Ative um **Goo Block Tier 4** (`justdirethings:gooblock_tier4`) vivo
2. Coloque `mysticalagriculture:machine_frame` **ao lado** do bloco de goo (cardinal, mesma altura)
3. O goo **consome** o machine frame e o bloco vira `nerdkube:sculk_gear_crystal` (com `facing` cardinal)
4. Quebre o cristal → `1× nerdkube:mecanismo_sombra_corrompido`

Receita datapack: `sculk_gear_goospread.json` — aparece na categoria **Goo Spread** do JEI do Just Dire Things.

Modelo/textura do cristal: export Blockbench em `docs/blockbench/gear_crystal.*` → assets `textures/block/gear_crystal.png`.

## IDs substituídos (pack real)

Alguns IDs da spec original não existem no pack; receitas usam equivalentes:

| Spec original | ID usado |
|---------------|----------|
| `oritech:ultimate_core` | `oritech:machine_core_7` |
| `mekanism:u_u_matter` | `mekmm:uu_matter` |
| `actuallyadditions:palis_crystal_block_empowered` | `actuallyadditions:empowered_palis_crystal_block` |
| `oritech:connective_processor` | `oritech:super_ai_chip` |
| `mekanism:synergy_matrix_addon` | `mekanism:module_energy_unit` |
| `oritech:etheletic_quartz` | `oritech:quartz_dust` |
| `oritech:insanite_block` | `bigreactors:insanite_block` |
| `mekanism:advanced_electrolysis_core` | `mekmm:advanced_electrolysis_core` |
| `oritech:dry_ice` | `powah:dry_ice` |
| `justdirethings:shadow_gem` | mecânica Goo Spread Tier 4 (sem item catalisador na mão) |
| Oritech Assembler / Reaction Chamber + quantum_infusion | **Atomic Forge Block** (`oritech:atomic_forge_block`) + receita tipo `oritech:atomic_forge` / Pressure Chamber 16 bar + `overcharged_crystal` |
| EnderIO Chemical Crystallizer | craft shapeless com `enderio:liquid_darkness_bucket` |

## JEI

- **Ingredient Info** (R nos itens) — resumo de uso e obtenção
- **Ritual NerdCube** — layout do craft final (sem receita do bloco `cube_maker` ainda)

## Cubo Eterno (admin)

- Bloco indestrutível `eternal_cube` — apenas OP nível 2+ pode colocar ou quebrar
- Obtenção: `/give @p nerdkube:eternal_cube` (não aparece na aba criativa)
- Textura VIP gerada em `docs/textures/block/eternal_cube/`

## Teste

1. `.\gradlew build`
2. `/reload` — verificar log sem erros de receita
3. JEI: `processador_antimateria_insana` → Atomic Forge (2× fragmento + insanite)
4. Goo Spread in-game: Tier 4 + MA machine_frame ao lado → goo consome → sculk_gear_crystal → quebrar → mecanismo_sombra_corrompido
5. Ritual CubeMaker com `/nerdkube ritual reset` para repetir
6. `/give` Cubo Eterno como OP e testar bloqueio para jogadores normais
