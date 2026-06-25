# JARs opcionais para desenvolvimento local

## Modpack completo no `runClient`

Por padrão, o Gradle carrega **todos os JARs** de:

```
G:\CurseForge\minecraft\Instances\NerdCube\mods\
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

O workflow `.github/workflows/deploy.yml` executa `scripts/ci-setup-libs.sh` antes do build e publica com [mc-publish](https://github.com/Kir-Antipov/mc-publish). O script baixa os JARs com os nomes exatos de `gradle.properties` (Modrinth + CurseForge CDN para Just Dire Things).

Atualize versões no script e em `gradle.properties` quando o modpack mudar.

**Token CurseForge:** o CI usa a [Upload API de autores](https://support.curseforge.com/support/solutions/articles/9000197321-curseforge-upload-api) (`X-Api-Token`). Gere em [authors.curseforge.com → API Tokens](https://authors.curseforge.com/#/api-tokens). A chave do [Studios console](https://console.curseforge.com/) (`$2a$10$…`, `x-api-key`) não serve para o `mc-publish`.

## Deploy no CurseForge (instância local)

```powershell
.\gradlew deployToModpack
```

Copia `build/libs/nerdkube-<versão>.jar` → `modpack_mods_dir` (mesmo nome gerado pelo Gradle, alinhado a `mod_version` / tag `v*`).

Os JARs em `libs/` estão no `.gitignore` — não versionar mods de terceiros.
