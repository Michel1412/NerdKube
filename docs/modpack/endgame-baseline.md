# Baseline de endgame — NerdCube

Documento de referência para o mod **NerdKube** (customização de endgame). Última leitura da instância: **2026-06-23**.

## Visão geral da progressão

O pack usa **FTB Quests** em modo **linear** (`progression_mode: "linear"`). O acesso ao End não é vanilla: depende do mod **End Remastered** (`endrem`).

### Fluxo atual até o End

1. **Início** — Corpse, Sophisticated Backpacks, FTB Ultimine
2. **Exploração** — Waystones, Paraglider, Alex's Mobs, Artifacts, Explorer's Compass, Nature's Compass
3. **Os olhos do fim** — coletar **16 olhos** `endrem:*` para montar o portal
4. **End** — ilha reformulada por **YUNG's Better End Island**; dragão com loot especial (Simply Swords)

Não existe capítulo FTB Quests ativo de "Endgame 1–7" (só restos em `en_us.snbt`). O endgame pós-dragão é coberto pelo **NerdKube** — ver [ritual-reference.md](ritual-reference.md).

## Ritual NerdCube (NerdKube)

Após o End, jogadores podem montar o ritual em cruz:

1. `nerdkube:cube_maker` no centro com `minecraft:crafter`
2. Quatro pedestais temáticos a 3 blocos (N/S/L/O): `pedestal_tech`, `pedestal_magia`, `pedestal_exploracao`, `pedestal_agricultura`, cada um com o amuleto correspondente
3. Botão **Fabricar** na GUI — gera `nerdkube:nerd_cube` assinado
4. **1 troféu por jogador** (`nerdkube:has_crafted_trophy`)

## End Remastered (`endrem`)

| Config | Valor | Implicação |
|--------|-------|------------|
| `USE_EYE_OF_ENDER` | `false` | Olho de ender vanilla não localiza stronghold |
| `THROW_EYE_OF_ENDER` | `false` | Não arremessa olho |
| `FRAME_HAS_RANDOM_EYE` | `false` | Moldura não spawna olho aleatório |
| `EYE_BREAK_PROBABILITY` | `10` | 10% de quebrar ao inserir |
| `IS_CRYPTIC_EYE_OBTAINABLE` | `true` | Olho críptico disponível |
| `IS_EVIL_EYE_OBTAINABLE` | `true` | Olho maligno disponível |
| `CAN_REMOVE_EYE` | `true` | Pode remover olhos da moldura |

### 16 olhos exigidos nas quests

`old`, `nether`, `cold`, `rogue`, `black`, `magical`, `lost`, `corrupted`, `wither`, `guardian`, `witch`, `cursed`, `exotic`, `evil`, `undead`, `cryptic`

Texto da quest introdutória (PT-BR): *"Você consegue encontrar 12 dos 16 olhos?, talvez voce tenha que matar alguem para isso."*

## YUNG's Better End Island

- Torre central no primeiro spawn e ao re-summonar dragão
- Plataforma de obsidian e gateways **não** são vanilla
- Dragão re-summonado **não** dropa ovo (`Resummoned Dragon Drops Egg = false`)
- Sino toca antes do primeiro summon e re-summons

## Loot e combate pós-End

- **Simply Swords**: peso `50.0` para loot único no `minecraft:entities/ender_dragon`
- **Apothic Enchanting** e **Silent Gear** presentes — equipamento endgame tech/magia é viável
- **Lootr**: baús per-player em estruturas (multiplayer-friendly)

## Alex's Mobs vs NerdKube

- **Alex's Mobs** foi **removido do ambiente de teste** (`modpack_excluded_jars` em `gradle.properties`; JAR ausente em `mods/` da instância NerdCube).
- Capítulo FTB `alexs_mobs` ainda existe na instância — tratar como conteúdo legado até a equipe revisar as quests.
- **NerdKube 1.1.0+** não integra com Alex's Mobs — gates de progressão do mod usam Vanilla.

## Conteúdo fantasma (não confiar sem verificar JAR)

| Conteúdo | Status |
|----------|--------|
| Twilight Forest | Quests, **sem JAR** |
| Born In Chaos | Quests, **sem JAR** |
| Lightman's Currency | Configs + trades, **sem JAR** |

O NerdKube **não deve** depender desses mods até entrarem no pack.

## Diretrizes para o mod NerdKube

1. **Compatibilidade**: NeoForge `21.1.233`, MC `1.21.1`
2. **Integrar com `endrem`**, não substituir o gate sem alinhar com FTB Quests
3. **Respeitar Better End Island** — alterações no End devem coexistir com a torre/YUNG
4. **Multiplayer**: considerar Corpse, PlayerRevive (60s bleed), Lootr, FTB Teams
5. **Idioma**: pack em PT-BR; textos do mod devem ter `pt_br` (e idealmente `en_us`)
6. **Dependências opcionais**: usar `@Mod` optional deps para mods que podem sumir/voltar no pack

## Mods relevantes para endgame estendido

| Área | Mods |
|------|------|
| Tech pesada | AE2 (+ ExtendedAE, AdvancedAE), Mekanism (+ Unleashed, More Machine), Oritech, Pipez |
| Magia avançada | Ars Nouveau, Ars Elemental, Iron's Spells, Malum, Occultism, EvilCraft |
| Dimensões | Mining Dimension Repolished (sem Twilight Forest ativo) |
| Bosses/loot | Artifacts, Simply Swords/More (Alex's Mobs no pack, não no NerdKube) |
| Utilidade | Waystones, Sophisticated Storage, RFTools, Polymorph |
