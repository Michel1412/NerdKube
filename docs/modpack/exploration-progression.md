# Progressão Exploração — NerdKube 0.6.0

Referência do pilar **Audácia e Conquista** (pedestal leste) e amuleto `reliquia_desbravador`.

## Fluxo resumido

```
Pedestal Exploração (bancada 3×3 — Silent Gear + Waystones)
    ↓
Amuleto de Sangue Petrificado (Mob Grinding Solidifier)
    ↓
Núcleo de Alma do Vazio (matar Verme do Vazio / Urso Grizzly)
    ↓
Insígnia do Desbravador (loot 1,5% em masmorras Lootr/YUNG's/Explorify)
    ↓
Olho da Forja Ancestral (craft 3×3)
    ↓
Punho da Lâmina Sacrificada (craft: 4 Netherfused Gem + 4 Sculk Boomer + 1 única Simply Swords)
    ↓
Visor das Doze Dimensões (8 olhos End Remastered + nether star)
    ↓
Relíquia do Desbravador (ritual Mesa de Encantamentos)
```

## Receitas

| Etapa | Arquivo | Máquina / trigger | Saída |
|-------|---------|-------------------|-------|
| P | `pedestal_exploracao_craft.json` | Bancada 3×3 | `pedestal_exploracao` |
| 1 | `fragmento_combate_stage1_solidify.json` | MGUtils Solidifier | `fragmento_combate_stage1` |
| 1b | — (`VoidWormSoulHandler`) | Matar `alexsmobs:void_worm` ou `grizzly_bear` (1 boss → 1 fragmento) | `componente_combate_stage1_completo` |
| 2 | — (`ExplorationChestLootModifier`) | Loot 1,5% + 3× Artifacts | `fragmento_combate_stage2` |
| 2b | `componente_combate_stage2_craft.json` | Bancada 3×3 | `componente_combate_stage2_completo` |
| 3 | `lamina_conquistador_craft.json` | Bancada 3×3 | `lamina_conquistador` |
| 4 | `olho_desbravador_primal_craft.json` | Bancada 3×3 | `olho_desbravador_primal` |
| H | — (`ExplorationAmuletCraftHandler`) | Mesa de Encantamentos | `reliquia_desbravador` |

## IDs corrigidos (spec vs pack)

| Spec | Pack |
|------|------|
| `silentgems:corrupted_essence` | `silentgems:chaos_essence` |
| `pedestal_exploration` | `pedestal_exploracao` |
| `reliquia_do_desbravador` | `reliquia_desbravador` |

## Ritual final

Clique na **Mesa de Encantamentos** com `olho_desbravador_primal` na mão principal, tendo no inventário:

- 1× `lamina_conquistador`
- 3× `componente_combate_stage1_completo`
- 3× `componente_combate_stage2_completo`
- 1× `poeira_transmutacao_proibida`

Documentado no JEI: Eterna 50, Quanta 80, Arcana 95 (estantes Apothic).

## Teste

1. `.\gradlew build deployToModpack`
2. `/reload` — sem erros de receita
3. JEI (R) em cada etapa
4. Ritual: `reliquia_desbravador` no pedestal **leste**
