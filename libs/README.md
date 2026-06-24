# JARs opcionais para desenvolvimento local

## Modpack completo no `runClient`

Por padrão, o Gradle carrega **todos os JARs** de:

```
G:\CurseForge\minecraft\Instances\Nerds Quadrados\mods\
```

(configurável em `gradle.properties` → `modpack_mods_dir`)

O mod NerdKube em dev vem do **sourceSet** do projeto; JARs `nerdkube-*.jar` na pasta do pack são **ignorados** para evitar duplicata.

## Pasta `libs/` (fallback)

Use `libs/` quando `modpack_mods_dir` não existir nesta máquina, ou para `compileOnly` quando o plugin precisa compilar contra um JAR específico:

| Mod | Uso |
|-----|-----|
| Jade | `compileOnly` — plugin Waila |
| JEI | `compileOnly` — plugin de receitas |
| Farmer's Delight | opcional (receitas datapack) |
| Mekanism + Generators | `compileOnly` — `RecipeOverrideHandler` |
| Oritech | `compileOnly` — mixins Laser Arm / Destroyer |
| Just Dire Things | `compileOnly` — mixin Goo Spread |
| FTB Library + FTB Chunks + Architectury | `compileOnly` — trava FakePlayer x chunks claimed |
| End Remastered | `compileOnly` — integração opcional |

## CI (GitHub Actions)

O workflow de release executa `scripts/ci-setup-libs.sh` antes do build. O script baixa os JARs com os nomes exatos de `gradle.properties` (Modrinth + CurseForge CDN para Just Dire Things).

Atualize versões no script e em `gradle.properties` quando o modpack mudar.

## Deploy no CurseForge (instância local)

```powershell
.\gradlew deployToModpack
```

Copia `build/libs/nerdkube-<versão>.jar` → `modpack_mods_dir` (mesmo nome gerado pelo Gradle, alinhado a `mod_version` / tag `v*`).

Os JARs em `libs/` estão no `.gitignore` — não versionar mods de terceiros.
