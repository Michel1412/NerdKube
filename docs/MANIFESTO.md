# MANIFESTO DO PROJETO: NERDKUBE

**NerdKube** é o mod de endgame do modpack *NerdCube*. Ele estende a progressão **após** o gate do End (End Remastered — 16 olhos, YUNG's Better End Island, loot do dragão via Simply Swords) com **quatro pilares de harmonia** que convergem no **Ritual do Fabricador de Cubo** (`nerdkube:cube_maker`), gerando o troféu assinado `nerdkube:nerd_cube`.

> **Versão do mod:** `1.1.0` · **Minecraft:** `1.21.1` · **Loader:** NeoForge `21.1.233`  
> **Idioma principal:** PT-BR · **Integrações opcionais:** Jade, JEI, Bakery, Farmer's Delight, entre outros.

---

## 1. OS QUATRO PILARES DA HARMONIA

| Módulo | Foco de gameplay | Mods principais (pack) | Pedestal ritual | Amuleto final (ID registrado) |
|--------|------------------|------------------------|-----------------|-------------------------------|
| **Tecnologia** | Cadeias industriais, energia extrema e corrupção mecânica | PneumaticCraft, Ender IO, Oritech, Mekanism / Mekanism More, Just Dire Things, Actually Additions, Powah, AE2 | `nerdkube:pedestal_tech` (Norte) | `nerdkube:nucleo_de_materia` — *Núcleo de Matéria* |
| **Magia** | Rituais, sangue, espíritos e aparato arcano | Occultism, Malum, EvilCraft, Ars Nouveau, Iron's Spells | `nerdkube:pedestal_magia` (Sul) | `nerdkube:coracao_arcano` — *Coração Arcano* |
| **Exploração** | Bosses, masmorras, dimensões e combate de elite | Artifacts, Simply Swords, End Remastered, Mob Grinding Utils, Lootr, YUNG's / Explorify | `nerdkube:pedestal_exploracao` (Leste) | `nerdkube:reliquia_desbravador` — *Relíquia do Desbravador* |
| **Agricultura** | Botânica expert, fermentação lenta e gastronomia ritual | Mystical Agriculture, Productive Bees, Botany Pots Tiers, Farm & Charm, Let's Do Bakery, Farmer's Delight, Macaw's / Refurbished Furniture | `nerdkube:pedestal_agricultura` (Oeste) | `nerdkube:semente_criacao` — *Semente da Criação* |

**Ritual final:** com os quatro amuletos nos pedestais corretos, `minecraft:crafter` no centro do `cube_maker` e botão **Fabricar**, o jogador obtém `nerdkube:nerd_cube` com assinatura `nerdkube:crafted_by`. **Limite: 1 troféu por jogador** (`nerdkube:has_crafted_trophy`).

---

## 2. DETALHAMENTO DA PROGRESSÃO REAL POR MÓDULO

### Pilar Tecnologia → `nerdkube:nucleo_de_materia`

1. **Pedestal de Tecnologia** — PneumaticCraft Pressure Chamber, 16 bar (`pedestal_tech_pressure.json`).
2. **Fragmento de Matriz de Dados** — Ender IO Alloy Smelter (`fragmento_matriz_dados_alloy.json`).
3. **Processador de Antimatéria Insana** — Oritech Atomic Forge (`processador_antimateria_insana_atomic.json`).
4. **Mecanismo Corrompido por Sombra** — Just Dire Things Goo Spread Tier 4: `mysticalagriculture:machine_frame` ao lado do goo → bloco `nerdkube:sculk_gear_crystal` → quebrar (`sculk_gear_goospread.json`).
5. **Núcleo Lógico Infundido** — craft 3×3 (`nucleo_logico_infundido_craft.json`).
6. **Matriz Modular Estabilizada** — Actually Additions Empowerer (`matriz_modular_estabilizada_empowerer.json`).
7. **Chassi Cyber-Flux** + **Disco de Matéria Escura** + **Singularidade Hipercarregada** (10B FE no Powah) — crafts em cruz / energização.
8. **Núcleo de Matéria** — Pressure Chamber 16 bar (`nucleo_materia_pressure.json`) → amuleto do pedestal **norte**.

---

### Pilar Magia → `nerdkube:coracao_arcano`

1. **Pedestal de Magia** — Occultism Ritual `craft_afrit` (`pedestal_magia_ritual.json`).
2. **Oferenda ao Abismo** (`oferta_poco_sombria`) — craft shapeless: Mesa de Encantamentos + bastão de relâmpago + nether star (`oferta_poco_sombria_craft.json`).
3. **Coração Sombrio Purificado** — lançar oferenda no poço sem fundo Malum (`componente_magia_stage1_void_favor.json`).
4. **Pergaminho de Geometria Proibida** — craft 3×3 (`fragmento_magia_stage2_craft.json`).
5. **Ídolo de Cinzas e Almas** — Malum Spirit Infusion (`componente_magia_stage2_spirit_infusion.json`).
6. **Anel de Ancoragem Celestial** — craft pentagrama (`matriz_runica_craft.json`).
7. **Plasma de Sangue Condenado** — EvilCraft Blood Infuser, 600k mB (`essencia_vital_instavel_blood_infuser.json`).
8. **Coração Arcano** — Ars Nouveau Enchanting Apparatus (`coracao_arcano_apparatus.json`) → amuleto do pedestal **sul**.

---

### Pilar Exploração → `nerdkube:reliquia_desbravador`

1. **Pedestal de Exploração** — craft 3×3 Silent Gear + Waystones + Pylons (`pedestal_exploracao_craft.json`).
2. **Amuleto de Sangue Petrificado** — Mob Grinding Utils Solidifier (`fragmento_combate_stage1`).
3. **Núcleo de Alma do Vazio** — evento `BossSoulInjectionHandler`: ao matar `minecraft:wither` **ou** `minecraft:ender_dragon` com o fragmento no inventário, **apenas 1 unidade** da pilha vira `componente_combate_stage1_completo` (não transforma a pilha inteira).
4. **Insígnia do Desbravador Perdido** — loot 1,5% em masmorras (Lootr / YUNG's / Explorify) + 3× Artifacts (`fragmento_combate_stage2`).
5. **Olho da Forja Ancestral** — craft 3×3 (`componente_combate_stage2_craft.json`).
6. **Punho da Lâmina Sacrificada** — craft 3×3 (`lamina_conquistador_craft.json`): 4× `simplyswords:netherfused_gem`, 4× `minecraft:echo_shard`, centro em tag `simplyswords:uniques` *(não usa mais bigorna)*.
7. **Visor das Doze Dimensões** — 8 olhos End Remastered + nether star (`olho_desbravador_primal_craft.json`).
8. **Relíquia do Desbravador** — ritual na Mesa de Encantamentos (`ExplorationAmuletCraftHandler`): visor na mão + 1× lâmina + 3× núcleo de alma + 3× olho da forja + **1× poeira de transmutação proibida** → amuleto do pedestal **leste**.

---

### Pilar Agricultura → `nerdkube:semente_criacao`

1. **Pilar Arquitetônico Inacabado** → **Pedestal de Agricultura** — smithing com Mesa de Carpinteiro Chipped (`pedestal_bruto_craft.json`, `pedestal_agricultura_smithing.json`).
2. **Semente de Matéria Cósmica** — Mystical Agriculture Infusion Altar (`fragmento_agri_stage1_infusion.json`).
3. **Pacote de Essências Agrícolas** → **Casulo de Mel Supremium** — Productive Bees Centrifuge (`pacote_essencias_agri_craft.json`, `componente_agri_stage1_centrifuge.json`).
4. **Massa Rúnica Crua** — Farm & Charm Crafting Bowl (`massa_runica_crua_crafting_bowl.json`) → bloco colocável `nerdkube:massa_runica_crua`.

   **Ritual de panificação (código Java):**
   - Colocar massa no chão **ou** sobre `bakery:baker_station`.
   - **Clique com Cristal Mestre de Infusão** (`mysticalagriculture:master_infusion_crystal`) — cristal **não é consumido** → bloco `bolo_mistico_colocavel`.
   - Mão vazia no bolo místico → item `nerdkube:runic_crystal_cake`.

5. **Raízes Doces** — Ultra Botany Pot + `mysticalagriculture:supremium_farmland`, `grow_time` 12.000 ticks (~10 min), colheita 1–2 unidades (`data/botanypots/.../agri_core.json` + fallback `BotanyPotGerminationHandler`).

6. **Raízes Doces Fritas** — Frigideira do Farmer's Delight (`raizes_doces_fritas_skillet.json`, receita `minecraft:smelting`):
   - **Entrada:** `nerdkube:raizes_doces` na `farmersdelight:skillet` com calor embaixo
   - **Saída:** `nerdkube:raizes_doces_fritas`

7. **Raízes Ancestrais em Conserva** — craft shapeless (`raizes_ancestrais_em_conserva_craft.json`):
   - **Craft:** `1× bakery:jar` + `3× raizes_doces_fritas` → `raizes_ancestrais_em_conserva`

8. **Módulo de Cozinha Imperial** — craft decoração 3×3 (`altar_revestimento_agri_craft.json`).
9. **Banquete da Consagração** — Farmer's Delight Cooking Pot (`massa_nutrientes_supremos_cooking.json`).
10. **Semente da Criação** — Cooking Pot final (`semente_criacao_cooking.json`):
   - 1× módulo de cozinha, 2× banquete, 2× casulo de mel, **1× raizes_ancestrais_em_conserva** → amuleto do pedestal **oeste**.

---

### Camada alquímica de endgame → `nerdkube:poeira_transmutacao_proibida`

- **Gacha alquímico** (`ForbiddenTransmutationHandler`):
  - **Agachado + clique direito** em `nerdkube:pedestal_exploracao` segurando `minecraft:beacon` na mão principal.
  - **Custo fixo:** 30 níveis de XP + 1 Beacon (sempre consumidos).
  - **Chance:** **2%** de sucesso → dropa `poeira_transmutacao_proibida`.
- **Loot alternativo:** `EndCityPoeiraLootModifier` — 2% em baús de `minecraft:chests/end_city`.
- **Status no ritual:** catalisador consumido na forja da **Relíquia do Desbravador** (pilar exploração).

---

## 3. DIAGRAMA TEXTUAL DE EVOLUÇÃO (FLOWCHART)

### Pré-requisito do pack (FTB Quests + End Remastered)

```
Exploração vanilla / estruturas
        ➔ 16 olhos endrem:*
        ➔ Portal do End (sem olho de ender vanilla)
        ➔ Dragão + Better End Island + loot Simply Swords
        ➔ INÍCIO DO NERDKUBE (pós-End)
```

---

### Pilar Tecnologia

```
[Pressure Chamber 16 bar]
        ➔ nerdkube:pedestal_tech
        ➔ nerdkube:fragmento_matriz_dados
        ➔ nerdkube:processador_antimateria_insana
        ➔ JDT Goo T4 ➔ nerdkube:sculk_gear_crystal ➔ nerdkube:mecanismo_sombra_corrompido
        ➔ nerdkube:nucleo_logico_infundido
        ➔ nerdkube:matriz_modular_estabilizada
        ➔ nerdkube:chassi_cyber_flux + disco_materia_escura ➔ singularidade_hipercarregada
        ➔ ★ nerdkube:nucleo_de_materia ★
```

---

### Pilar Magia

```
[Occultism craft_afrit]
        ➔ nerdkube:pedestal_magia
        ➔ nerdkube:oferta_poco_sombria (sem receita — JEI)
        ➔ [Poço Malum] ➔ nerdkube:componente_magia_stage1_completo
        ➔ nerdkube:fragmento_magia_stage2
        ➔ [Malum Spirit Altar] ➔ nerdkube:componente_magia_stage2_completo
        ➔ nerdkube:matriz_runica + nerdkube:essencia_vital_instavel
        ➔ ★ nerdkube:coracao_arcano ★
```

---

### Pilar Exploração

```
[Craft pedestal_exploracao]
        ➔ nerdkube:fragmento_combate_stage1
        ➔ [Matar Wither / Ender Dragon — 1 boss : 1 fragmento] ➔ componente_combate_stage1_completo
        ➔ [Loot masmorra 1,5%] ➔ fragmento_combate_stage2
        ➔ nerdkube:componente_combate_stage2_completo
        ➔ nerdkube:lamina_conquistador (craft Simply Swords + 4× Echo Shard)
        ➔ nerdkube:olho_desbravador_primal (8 olhos endrem + nether star)
        ➔ [Mesa de Encantamentos] ➔ ★ nerdkube:reliquia_desbravador ★
```

---

### Pilar Agricultura

```
[Pedestal bruto ➔ smithing] ➔ nerdkube:pedestal_agricultura
        ➔ nerdkube:fragmento_agri_stage1
        ➔ pacote_essencias_agri ➔ [Centrífuga] ➔ componente_agri_stage1_completo
        ➔ [Tigela F&C] ➔ massa_runica_crua (bloco)
        ➔ [Cristal Mestre — clique] ➔ bolo_mistico ➔ runic_crystal_cake
        ➔ [Ultra Botany Pot + Supremium — ~10 min] ➔ nerdkube:raizes_doces
        ➔ [FD Skillet — fritura] ➔ nerdkube:raizes_doces_fritas
        ➔ [craft jar+3 fritas] ➔ nerdkube:raizes_ancestrais_em_conserva
        ➔ altar_revestimento_agri + massa_nutrientes_supremos
        ➔ [Cooking Pot FD] ➔ ★ nerdkube:semente_criacao ★
```

---

### Convergência ritual + endgame alquímico

```
                    nerdkube:nucleo_de_materia (N)
                              │
nerdkube:semente_criacao (O) ─┼─ nerdkube:cube_maker + minecraft:crafter
                              │
nerdkube:reliquia_desbravador (L) ─┼─ nerdkube:coracao_arcano (S)
                              │
                              ▼
                    ★ nerdkube:nerd_cube ★
                    (assinado · 1× por jogador)

        ═══════════ camada paralela (poeira proibida) ═══════════

[Shift+Beacon no pedestal_exploracao — 30 níveis XP]
        ➔ 2% chance ➔ nerdkube:poeira_transmutacao_proibida
        (também 2% em baús de End City)
```

---

## Notas de implementação

| Tópico | Detalhe |
|--------|---------|
| Conserva ancestral | FD Skillet (smelting) + craft jar Bakery |
| Boss kill | 1 morte = 1 fragmento transformado |
| Lâmina | Craft shaped, não bigorna |
| Jade | Pedestais do ritual |
| JEI | Ingredient Info (R) em toda a cadeia + categoria Ritual NerdKube |
| ProjectE | Removido — poeira via pedestal de exploração + loot End City |

---

## Documentação relacionada

| Arquivo | Conteúdo |
|---------|----------|
| [`AGENTS.md`](../AGENTS.md) | Guia para agentes e desenvolvedores |
| [`docs/modpack/endgame-baseline.md`](modpack/endgame-baseline.md) | Baseline do pack até o End |
| [`docs/modpack/ritual-reference.md`](modpack/ritual-reference.md) | Layout do ritual NerdCube |
| [`docs/modpack/tech-progression.md`](modpack/tech-progression.md) | Cadeia técnica detalhada |
| [`docs/modpack/magic-progression.md`](modpack/magic-progression.md) | Cadeia magia detalhada |
| [`docs/modpack/exploration-progression.md`](modpack/exploration-progression.md) | Cadeia exploração detalhada |
| [`docs/modpack/agriculture-progression.md`](modpack/agriculture-progression.md) | Cadeia agricultura detalhada |

---

*Última consolidação: NerdKube v1.1.0 — fontes: `ModItems.java`, `PedestalDirection.java`, handlers Java, receitas `data/nerdkube/recipe/` e snapshot do modpack NerdCube.*
