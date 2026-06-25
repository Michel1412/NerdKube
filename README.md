# PROJECT MANIFESTO: NERDKUBE

> Mod ID: `nerdkube` · **NerdKube** extends post-End progression for the *Nerds Quadrados* modpack.

## DESCRIPTION

After the End Remastered gate (16 custom eyes, YUNG's Better End Island, tuned dragon loot), **NerdKube** opens a four-pillar endgame. Each pillar — **Technology**, **Magic**, **Exploration**, and **Agriculture** — produces a unique amulet. Place all four on themed pedestals around the **Cube Maker** (`nerdkube:cube_maker`), insert a vanilla **Crafter** in the center, and craft a signed trophy: **`nerdkube:nerd_cube`** (one per player).

Optional integrations: Alex's Mobs (Forbidden Transmutation Dust), Jade, JEI, Farmer's Delight, Bakery, Mystical Agriculture, and more — aligned with the closed CurseForge pack.

## SUMMARY OF PROGRESSION

| Section | Language |
|---------|----------|
| [Textual flowchart](#textual-evolution-flowchart) | English |
| [Real mechanics by module (PT-BR)](#detalhamento-da-progressão-real-por-módulo-pt-br) | Portuguese |

**Pillar amulets**

| Direction | Pedestal | Amulet |
|-----------|----------|--------|
| North | `pedestal_tech` | `nucleo_de_materia` |
| South | `pedestal_magia` | `coracao_arcano` |
| East | `pedestal_exploracao` | `reliquia_desbravador` |
| West | `pedestal_agricultura` | `semente_criacao` |

**Requirements:** Minecraft **1.21.1** · NeoForge **21.1.232** · JDK **21**

**Build**

```powershell
.\gradlew build
.\gradlew deployToModpack   # copy JAR to CurseForge instance (local)
```

**CI / CurseForge release:** push tag `v*` (e.g. `v1.0.0`) — workflow `.github/workflows/deploy.yml` builds and uploads via [mc-publish](https://github.com/Kir-Antipov/mc-publish). Requires secrets `CURSEFORGE_PROJECT_ID` and **`CURSEFORGE_TOKEN` from [authors API tokens](https://authors.curseforge.com/#/api-tokens)** (Upload API, `X-Api-Token` — not the Studios console key). Tags without `-alpha`/`-beta` publish as **release**; add `docs/changelogs/<version>.md` for a curated changelog.

**Importante:** o GitHub Actions usa o workflow **do commit da tag**. Depois de corrigir o CI, mova a tag para `main` atual (`git tag -f v1.0.0` + `git push origin v1.0.0 --force`) ou rode **workflow_dispatch** na branch `main`.

---

## TEXTUAL EVOLUTION FLOWCHART

### Pack prerequisite (FTB Quests + End Remastered)

```
Exploration → 16 endrem eyes → End portal → Dragon + Better End Island
        ➔ NERDKUBE BEGINS (post-End)
```

### Technology pillar

```
[Pressure Chamber 16 bar] ➔ pedestal_tech ➔ fragmento_matriz_dados
        ➔ processador_antimateria_insana
        ➔ JDT Goo T4 ➔ sculk_gear_crystal ➔ mecanismo_sombra_corrompido
        ➔ nucleo_logico_infundido ➔ matriz_modular_estabilizada
        ➔ chassi_cyber_flux + disco_materia_escura ➔ singularidade_hipercarregada
        ➔ ★ nucleo_de_materia ★
```

### Magic pillar

```
[Occultism craft_afrit] ➔ pedestal_magia ➔ oferta_poco_sombria
        ➔ [Malum well] ➔ componente_magia_stage1_completo
        ➔ fragmento_magia_stage2 ➔ [Spirit Altar] ➔ componente_magia_stage2_completo
        ➔ matriz_runica + essencia_vital_instavel
        ➔ ★ coracao_arcano ★
```

### Exploration pillar

```
[pedestal_exploracao craft] ➔ fragmento_combate_stage1
        ➔ [Kill Void Worm / Grizzly — 1:1] ➔ componente_combate_stage1_completo
        ➔ [Dungeon loot 1.5%] ➔ fragmento_combate_stage2 ➔ componente_combate_stage2_completo
        ➔ lamina_conquistador ➔ olho_desbravador_primal
        ➔ [Enchanting Table ritual + Forbidden Dust] ➔ ★ reliquia_desbravador ★
```

### Agriculture pillar

```
[pedestal_bruto → smithing] ➔ pedestal_agricultura ➔ fragmento_agri_stage1
        ➔ [Centrifuge] ➔ componente_agri_stage1_completo
        ➔ [Crafting Bowl] ➔ massa_runica_crua ➔ [Master Crystal] ➔ runic_crystal_cake
        ➔ [Ultra Botany Pot] ➔ raizes_doces
        ➔ [FD Skillet] ➔ raizes_doces_fritas ➔ raizes_ancestrais_em_conserva
        ➔ altar_revestimento_agri + massa_nutrientes_supremos
        ➔ [Cooking Pot] ➔ ★ semente_criacao ★
```

### Ritual convergence + forbidden alchemy

```
                    nucleo_de_materia (N)
                              │
semente_criacao (W) ──────────┼── cube_maker + minecraft:crafter
                              │
reliquia_desbravador (E) ─────┼── coracao_arcano (S)
                              ▼
                    ★ nerd_cube ★ (signed · 1 per player)

Parallel: alexsmobs:transmutation_table + Beacon gacha (2%) ➔ poeira_transmutacao_proibida
```

---

## DETALHAMENTO DA PROGRESSÃO REAL POR MÓDULO (PT-BR)

Mecânicas **exatas do código** — ver também [`docs/MANIFESTO.md`](docs/MANIFESTO.md) e `docs/modpack/*-progression.md`.

### Indústria (pilar Tecnologia)

| Etapa | Implementação |
|-------|----------------|
| Pedestal | `pedestal_tech_pressure.json` — PneumaticCraft 16 bar |
| Cadeia | Receitas em `data/nerdkube/recipe/tech/` + overrides Mekanism (`RecipeOverrideHandler`) |
| Goo Spread | `sculk_gear_goospread.json` + mixin `BaseMachineBEMixin` (JDT) |
| Laser / claim | Mixins Oritech + `FakePlayerProgressionGuard` (bloqueia `FakeMachinePlayer` mesmo com UUID do dono) |
| Amuleto | `nucleo_materia_pressure.json` → pedestal **norte** |

### Magia (pilar Magia)

| Etapa | Implementação |
|-------|----------------|
| Pedestal | `pedestal_magia_ritual.json` — Occultism |
| Poço | `componente_magia_stage1_void_favor.json` — Malum |
| Espíritos | `componente_magia_stage2_spirit_infusion.json` |
| Sangue | `essencia_vital_instavel_blood_infuser.json` — EvilCraft |
| Amuleto | `coracao_arcano_apparatus.json` — Ars Nouveau → pedestal **sul** |

### Combate / Exploração

| Etapa | Implementação |
|-------|----------------|
| Boss | `VoidWormSoulHandler` — 1 kill (`void_worm` ou `grizzly_bear`) = 1 `componente_combate_stage1_completo` |
| Masmorra | `ExplorationChestLootModifier` — 1,5% + 3× Artifacts |
| Lâmina | `lamina_conquistador_craft.json` — Simply Swords + Alex's Mobs |
| Visor | `olho_desbravador_primal_craft.json` — 8 olhos End Remastered |
| Ritual | `ExplorationAmuletCraftHandler` — Mesa de Encantamentos: visor na mão + lâmina + 3× núcleo alma + 3× olho forja + **1× poeira_transmutacao_proibida** |
| Amuleto | `reliquia_desbravador` → pedestal **leste** |

### Cozinha / Agricultura

| Etapa | Implementação |
|-------|----------------|
| Panificação | `RunicDoughBlock`, `MysticCakeBlock`, `BakerStationDoughHandler` — Cristal Mestre não consumido |
| Botany | `data/botanypots/recipe/nerdkube/crop/agri_core.json` + `BotanyPotGerminationHandler` |
| Fritura | `raizes_doces_fritas_skillet.json` — smelting na `farmersdelight:skillet` |
| Conserva | `raizes_ancestrais_em_conserva_craft.json` — jar Bakery + 3 fritas |
| Banquete | `massa_nutrientes_supremos_cooking.json` — FD Cooking Pot |
| Amuleto | `semente_criacao_cooking.json` → pedestal **oeste** |

### Alquimia proibida (Alex's Mobs)

| Mecânica | Classe / datapack |
|----------|-------------------|
| Gacha | `ForbiddenTransmutationHandler` — Shift+Beacon, 30 XP, 2% |
| Loot raro | Override `data/alexsmobs/loot_table/gameplay/transmutation_table_rare.json` |
| Uso | Consumida no ritual da **Relíquia do Desbravador** |

### Ritual final NerdCube

- `RitualService` / `RitualValidator` — cruz de pedestais a 3 blocos, Crafter no `cube_maker`
- `nerdkube:has_crafted_trophy` — limite 1 troféu por jogador
- Jade: `PedestalJadeProvider` — status do pedestal e oferta

---

## Desenvolvimento

```powershell
# CI local (Linux/macOS/WSL) — baixa JARs compileOnly
bash scripts/ci-setup-libs.sh

.\gradlew runClient    # requer instância CurseForge ou libs/
```

Documentação interna: [`AGENTS.md`](AGENTS.md) · snapshot do pack: [`docs/modpack/`](docs/modpack/)
