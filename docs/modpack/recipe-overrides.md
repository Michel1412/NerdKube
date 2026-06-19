# Receitas alteradas — NerdKube



Substituições de craft de outros mods. Os JSON ficam no namespace do mod alvo; o **`RecipeOverrideHandler`** remove a receita vanilla (por ID ou por ingrediente antigo) e registra a nova versão após cada carga/sync do `RecipeManager`.



## Deploy no modpack



O JAR precisa estar na instância CurseForge:



```powershell

cd e:\Arquivos_Mods\NerdKube

.\gradlew deployToModpack

```



Confirme `G:\CurseForge\minecraft\Instances\Nerds Quadrados\mods\nerdkube-0.4.1.jar`. Reinicie o jogo após atualizar o mod (não confie só em `/reload` na primeira validação).



## Itens



| Item | Mod | Substituição |

|------|-----|--------------|

| Wind Generator | Mekanism Generators | 2× `mekanism:energy_tablet` → 2× `powah:battery_nitro` |

| Atomic Disassembler | Mekanism | 1× `mekanism:energy_tablet` → 1× `mekanism:ultimate_induction_cell` |

| Vengeance Pickaxe | EvilCraft | 3× diamante (`c:gems/diamond`) → 3× `malum:hallowed_gold_ingot` |



## Onde craftar (Mekanism)



Receitas com tipo **`mekanism:mek_data`** (Wind Generator e Atomic Disassembler) **não** usam a bancada vanilla comum.



Crafte em:



- **Aba Crafting do Mekanism** (grid 3×3 na interface do mod), ou

- **Formulaic Assemblicator** (`mekanism:formulaic_assemblicator`)



A Vengeance Pickaxe usa `minecraft:crafting_shaped` na **bancada vanilla**.



## Arquivos



| Receita | JSON | ID |

|---------|------|-----|

| Wind Generator | `data/mekanismgenerators/recipe/generator/wind.json` | `mekanismgenerators:generator/wind` |

| Atomic Disassembler | `data/mekanism/recipe/atomic_disassembler.json` | `mekanism:atomic_disassembler` |

| Vengeance Pickaxe | `data/evilcraft/recipe/crafting/vengeance_pickaxe.json` | `evilcraft:crafting/vengeance_pickaxe` |



## Código



- `RecipeOverrideHandler` — remove vanilla (energy_tablet / diamante) e aplica JSON do **JAR nerdkube** (`Class.getResourceAsStream`, não `ClassLoader`)

- `RecipeManagerMixin` — reaplica ao fim de `RecipeManager#apply`

- `ClientRecipeSyncMixin` — reaplica após sync de receitas do servidor ao cliente

- `RecipeOverrideReloadListener` / `RecipeOverrideEvents` — servidor + `RecipesUpdatedEvent` (cliente)



## Teste



1. `.\gradlew deployToModpack` e reiniciar o CurseForge

2. Log: `NerdKube: 3 receita(s) de override aplicada(s).` e ingredientes `(ok)` para as três receitas

3. JEI: Wind Generator com **Battery Nitro**; Atomic Disassembler com **Ultimate Induction Cell**; Vengeance Pickaxe com **Hallowed Gold Ingot**

4. Craft real com os ingredientes novos (Mekanism UI para Disassembler; bancada para Pickaxe)

5. `/reload` — receita nova deve permanecer


