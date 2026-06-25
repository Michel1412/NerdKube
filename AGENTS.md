# NerdKube — Guia para agentes

Mod NeoForge para o modpack **NerdCube** (MC 1.21.1). Objetivo: **customizar o endgame** mantendo alinhamento com o pack fechado no CurseForge.

## Leitura obrigatória (ordem)

1. `docs/modpack/manifest.json` — snapshot estruturado do pack
2. `docs/modpack/endgame-baseline.md` — estado atual do endgame e diretrizes
3. `docs/modpack/tech-progression.md` / `docs/modpack/magic-progression.md` — cadeias Expert tech e magia (0.3.0)
4. `docs/modpack/recipe-overrides.md` — crafts de mods alterados pelo NerdKube
5. `docs/modpack/mods-by-category.md` — mods instalados por categoria
6. `docs/modpack/mods-list.txt` — lista exata de JARs (213)
7. `docs/textures/README.md` — matrizes e paletas de texturas

## Instância do modpack (fonte da verdade)

```
G:\CurseForge\minecraft\Instances\NerdCube\
```

| Item | Valor |
|------|-------|
| Minecraft | 1.21.1 |
| Loader | NeoForge 21.1.233 |
| Mod JARs | 213 |
| Addons CurseForge | 220 (inclui shaders + PT-BR) |
| Idioma | PT-BR (resource pack + FTB Quests traduzido) |
| Progressão | FTB Quests, modo linear |

**Não há KubeJS nem datapacks** na instância. Customizações vêm de `config/` e do mod NerdKube.

## O que o pack já define sobre endgame

- **Gate para o End**: End Remastered — 16 olhos customizados, sem olho de ender vanilla
- **Quests**: capítulo `os_olhos_do_fim` (grupo Exploração)
- **Dimensão End**: YUNG's Better End Island (torre central, gateways custom)
- **Loot dragão**: Simply Swords com peso alto no ender_dragon
- **Pós-dragão**: sem capítulo FTB ativo — **é aqui que o NerdKube atua**

## Conteúdo fantasma (cuidado)

Configs/quests existem, mas **JAR ausente** em `mods/`:

- Twilight Forest
- Born In Chaos
- Lightman's Currency

Não criar dependência hard nesses mods.

## Regras de desenvolvimento

### Alinhamento com o modpack

- Antes de mudar integrações, reler a instância ou rodar `docs/modpack/refresh-snapshot.ps1`
- Versões de compile: **MC 1.21.1**, **NeoForge 21.1.233**
- Mod IDs reais do pack (ex.: `endrem`, `ftbquests`, `waystones`) — conferir JEI/logs se duvidar
- Textos em **português (Brasil)** como idioma principal

### Escopo do mod

- Foco: **endgame** (pós-16-olhos / pós-dragão / re-summon / progressão estendida)
- Preferir: eventos, recipes, loot tables, advancements, integração FTB Quests (via API ou docs), bosses/mecânicas novas
- Evitar: duplicar o gate do End Remastered sem coordenar com quests existentes

### Código

- Minimizar escopo; seguir convenções NeoForge 1.21.1
- Dependências de outros mods como **optional** quando possível
- Não commitar secrets, credenciais ou cópia integral do modpack

## Atualizar snapshot do modpack

Quando o pack mudar (mods adicionados/removidos, configs de endgame):

```powershell
.\docs\modpack\refresh-snapshot.ps1
```

Depois revisar manualmente `manifest.json` e `endgame-baseline.md` se houver mudança de design.

## Paths úteis na instância

| Path | Conteúdo |
|------|----------|
| `mods/` | JARs instalados |
| `config/EndRemastered-NeoForge/endrem.json` | Regras dos olhos |
| `config/betterendisland-neoforge-1_21.toml` | End reformulado |
| `config/ftbquests/quests/` | Questlines (SNBT) |
| `config/simplyswords/loot.toml` | Loot do dragão |
| `config/playerrevive.json` | Revive multiplayer |
| `config/trades/` | Trades customizados |

## Estrutura do repositório

```
NerdKube/
├── AGENTS.md
├── build.gradle / gradle.properties
├── .cursor/rules/
├── docs/modpack/
├── docs/textures/            ← bases pixel art por item/bloco
├── tools/generate_textures.py
├── libs/                     ← JARs opcionais para dev (endrem, jade, jei)
└── src/main/
    ├── java/br/com/nerdskube/
    ├── resources/
    └── templates/META-INF/neoforge.mods.toml
```

## Build e execução

Requisitos: **JDK 21**.

```powershell
cd e:\Arquivos_Mods\NerdKube
.\gradlew build          # compilar
.\gradlew runClient      # cliente de desenvolvimento (carrega ~213 mods do pack)
.\gradlew runServer      # servidor de desenvolvimento
.\gradlew deployToModpack  # copia JAR para a instância CurseForge
```

### Ambiente de dev = modpack completo

O `runClient` / `runServer` carregam **todos os JARs** de `modpack_mods_dir` (padrão: instância **NerdCube** no CurseForge), exceto `nerdkube-*.jar` (o mod em dev vem do sourceSet) e JARs em `modpack_excluded_jars` (ex.: `alexsmobs-*.jar`).

Heap do jogo: **8G** (`jvmArgument` em `build.gradle`).

### Deploy no modpack (teste real)

Destino: `G:\CurseForge\minecraft\Instances\NerdCube\mods\nerdkube-1.1.0.jar`

```powershell
.\gradlew deployToModpack
```

### Compile-only (plugins / integrações)

O Gradle resolve em `libs/` ou `modpack_mods_dir` apenas para **compilar** plugins:

- **End Remastered** — integração opcional
- **Jade** — plugin Waila
- **JEI** — plugin de receitas

Mod ID: `nerdkube` | Pacote: `br.com.nerdskube` | NeoForge: `21.1.233`
