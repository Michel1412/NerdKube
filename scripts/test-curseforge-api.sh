#!/usr/bin/env bash
# Testa token + endpoints CurseForge (mesmo check do CI).
# Uso:
#   export CURSEFORGE_TOKEN="sua-chave"
#   export CURSEFORGE_PROJECT_ID="1580917"
#   bash scripts/test-curseforge-api.sh
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
exec bash "$ROOT/scripts/curseforge-validate.sh"
