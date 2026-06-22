---
name: Auditoria e Otimizacao NerdKube
overview: Relatório de auditoria dos 5 eixos (estrutura, sobrecarga de recipes, trava FakePlayer x chunks via FTB Chunks API, modularização via config, e árvore do NerdCube) com plano de ação para reduzir redundância de execução, blindar performance e fechar lacunas da progressão.
todos:
  - id: recipes
    content: Consolidar gatilhos de override (manter RecipeManagerMixin + RecipesUpdatedEvent), adicionar guarda de idempotência por instância, logs limpos e toggle de config
    status: completed
  - id: chunklock
    content: Implementar trava FakePlayer x chunks claimed via API do FTB Chunks (soft-dep), com cache por ChunkPos/UUID e fallback ao comportamento atual
    status: completed
  - id: config
    content: Expandir NerdKubeServerConfig (progression, fakePlayer, recipeOverrides) e substituir constantes hardcoded por valores configuráveis
    status: completed
  - id: tree
    content: Classificar itens do NerdCube (Primário/Secundário/Suporte), propor receita do cube_maker e produtor de oferta_poco_sombria, e remover o BlockItem redundante agri_core_crop
    status: completed
  - id: validate
    content: Build, lint e verificação de logs (override único, sem spam) após as mudanças
    status: completed
isProject: false
---

# Auditoria e Otimização do NerdKube

> Diagnóstico + plano. Nada será alterado até sua aprovação.

## 1. Diagnóstico por eixo

### 1.1 Varredura estrutural e arquivos sobrando
- Estrutura geral segue o padrão NeoForge 1.21.1 (registries em `registry/`, mixins em `mixin/`, integrações em `integration/`). `data/nerdkube/recipe/` (singular) está **correto** para 1.21.1.
- Texturas PNG **não são versionadas** (geradas por `tools/generate_textures.py` a partir de `docs/textures/`) — intencional, não é "arquivo sobrando".
- Itens/recursos sem conexão clara (detalhe no eixo 5):
  - `cube_maker` — registrado e no creative tab, mas **sem receita de obtenção** (lang diz "Obtenção: ainda não implementada"); é peça central do ritual.
  - `oferta_poco_sombria` — **consumido** por `componente_magia_stage1_void_favor.json`, mas **sem produtor** registrado.
  - item `agri_core_crop` (BlockItem) — redundante com o fluxo Botany Pot (`runic_crystal_cake` -> `raizes_doces`).
  - `eternal_cube` — bloco admin fora da árvore (manter); modelo `models/block/eternal_cube.json` tem 2628 linhas (revisar peso, mas é usado).
  - `bolo_mistico_colocavel` — bloco sem BlockItem (intencional, world-only).

### 1.2 Sobrecarga de recipes (ponto crítico)
- Núcleo em [RecipeOverrideHandler.java](src/main/java/br/com/nerdskube/recipe/RecipeOverrideHandler.java): substitui **3 receitas** (Wind Generator, Atomic Disassembler, Vengeance Pickaxe). **Não há loop por tick** — guarda `ThreadLocal APPLYING` evita recursão.
- O problema real é **redundância de gatilhos** (6 pontos disparando o mesmo `apply`), gerando reprocessamento e spam de log a cada reload/sync — o que dá a sensação de "rodando continuamente":
  - `RecipeManagerMixin` (após `apply`)
  - `RecipeOverrideReloadListener` (AddReloadListener)
  - `ServerStartingEvent`
  - `OnDatapackSyncEvent`
  - `ClientRecipeSyncMixin` (2 métodos)
  - `RecipesUpdatedEvent` (cliente)
- JEI: o hook `RecipesUpdatedEvent` em `HIGHEST` já é o ponto correto do ciclo de vida do cliente (antes do JEI ler o `RecipeManager`). O risco de "JEI puxar receita antiga" vem de aplicar tarde demais ou de forma não idempotente em parte dos gatilhos.

### 1.3 Trava FakePlayer x chunks claimed (decisão: via FTB Chunks API)
- Hoje **não existe** trava por claim: o mod só repassa a identidade do dono ao FakePlayer e dispara `BlockEvent.BreakEvent` para o FTB Chunks (presente no pack: `ftb-chunks-neoforge-2101.1.18.jar`) decidir — ver [LaserArmBlockEntityMixin.java](src/main/java/br/com/nerdskube/mixin/oritech/LaserArmBlockEntityMixin.java) e [FakePlayerOwnerRegistry.java](src/main/java/br/com/nerdskube/integration/fakeplayer/FakePlayerOwnerRegistry.java).
- [FakePlayerProgressionGuard.java](src/main/java/br/com/nerdskube/integration/fakeplayer/FakePlayerProgressionGuard.java) só protege blocos/itens da progressão, **não** chunks de terceiros.
- O build usa padrão `compileOnly` por mod (ver [build.gradle](build.gradle)); dá pra integrar a API do FTB Chunks como soft-dep do mesmo jeito.

### 1.4 Modularidade e configs
- Já existem: [NerdKubeConfig.java](src/main/java/br/com/nerdskube/config/NerdKubeConfig.java) (texturas) e [NerdKubeServerConfig.java](src/main/java/br/com/nerdskube/config/NerdKubeServerConfig.java) (conflitos de comando).
- Valores **hardcoded** candidatos a config:
  - Drop loot exploração `CHANCE=0.015F` e `>=3` artifacts ([ExplorationChestLootModifier.java](src/main/java/br/com/nerdskube/loot/ExplorationChestLootModifier.java)).
  - Transmutação `XP_LEVEL_COST=30`, `SUCCESS_CHANCE=0.02F` ([ForbiddenTransmutationHandler.java](src/main/java/br/com/nerdskube/integration/alexsmobs/ForbiddenTransmutationHandler.java)).
  - Listas de blocos/itens protegidos e heurística de nome em `FakePlayerProgressionGuard`.
  - Toggle de overrides de receita e verbosidade de log.

### 1.5 Árvore do NerdCube
- Arco: 4 pilares (Tech/Norte, Magia/Sul, Exploração/Leste, Agricultura/Oeste) -> `nucleo_de_materia` / `coracao_arcano` / `reliquia_desbravador` / `semente_criacao` -> ritual no `cube_maker` -> `nerd_cube`.
- Lacunas: `cube_maker` sem receita; `oferta_poco_sombria` sem produtor; item `agri_core_crop` redundante.

## 2. Plano de ação

### Etapa A — Recipes (eixo 2)
- Consolidar gatilhos: manter **(servidor)** `RecipeManagerMixin` (cobre startup + `/reload`) e **(cliente)** `RecipesUpdatedEvent` em `HIGHEST` (correção JEI). Remover redundantes: `RecipeOverrideReloadListener`, `ServerStartingEvent`, `OnDatapackSyncEvent`, `ClientRecipeSyncMixin` — após validar que a dupla mantida cobre todos os casos (incluindo multiplayer sync).
- Adicionar **guarda de idempotência por instância de `RecipeManager`** (ex.: marca/hash) para pular reaplicação desnecessária.
- Logs limpos: 1 linha por override aplicado com sucesso (`item antigo -> item novo`), e resumo único `N override(s) aplicado(s)`.
- Toggle `recipeOverrides.enabled` + `recipeOverrides.verboseLog` na config.

### Etapa B — Trava FakePlayer x chunks via FTB Chunks (eixo 3)
- `build.gradle` + `gradle.properties`: adicionar `ftb-chunks`/`ftb-library` como `compileOnly` (padrão dos outros mods).
- Novo pacote `integration/ftbchunks/` (carregado só se `ModList.isLoaded("ftbchunks")`):
  - Handlers `@SubscribeEvent` em `BlockEvent.BreakEvent`, `BlockEvent.EntityPlaceEvent`, `PlayerInteractEvent.RightClickBlock` (prioridade alta) que: se ator é FakePlayer/automação **e** o chunk é claimed por team que **não inclui** o dono -> cancela.
  - **Performance**: cache `long(ChunkPos) -> ownerTeamId` com invalidação por evento de claim do FTB; checagem de pertencimento por UUID/team em O(1); zero I/O e zero trabalho por tick.
  - Soft fallback: sem FTB Chunks, mantém o comportamento atual (BreakEvent bridge).
- Toggle `fakePlayer.chunkLockEnabled`.

### Etapa C — Config / modularização (eixo 4)
- Expandir `NerdKubeServerConfig` com seções `progression` (chance/contagem de loot, custo/chance de transmutação), `fakePlayer` (guard + chunkLock), `recipeOverrides`.
- Trocar constantes hardcoded pelos valores da config nos arquivos citados em 1.4.

### Etapa D — Árvore do NerdCube (eixos 1 e 5)
- Classificar cada item como **Primário / Secundário / Suporte** (tabela no relatório final e/ou doc em `docs/modpack/`).
- Fechar lacunas:
  - Propor receita de `cube_maker` (ligada aos 4 pilares) e atualizar lang.
  - Propor produtor de `oferta_poco_sombria`.
- Remover redundância: o BlockItem `agri_core_crop` (manter bloco/crop, remover item do creative tab) — sob sua confirmação item a item.
- Manter `eternal_cube` como admin (documentar como fora da progressão).

### Etapa E — Validação
- `./gradlew build` e `ReadLints` nos arquivos tocados; revisar logs do `runClient` para confirmar override único e ausência de spam.

## 3. Itens que pedem sua confirmação antes de mexer
- Remoção do BlockItem `agri_core_crop`.
- Conteúdo exato das novas receitas (`cube_maker`, `oferta_poco_sombria`).
- Remoção dos gatilhos redundantes de recipe (manter mixin + RecipesUpdatedEvent).