#!/usr/bin/env bash
# Testa token + project ID da CurseForge (mesmo check do CI).
# Uso:
#   export CURSEFORGE_TOKEN="seu-token"
#   export CURSEFORGE_PROJECT_ID="1580917"
#   bash scripts/test-curseforge-api.sh
set -euo pipefail

TOKEN="${CURSEFORGE_TOKEN:-}"
PROJECT_ID="${CURSEFORGE_PROJECT_ID:-1580917}"
PROJECT_ID="$(echo "$PROJECT_ID" | tr -d '[:space:]')"

if [[ -z "$TOKEN" ]]; then
  echo "ERRO: defina CURSEFORGE_TOKEN (https://console.curseforge.com/ -> API Keys)." >&2
  exit 1
fi

echo "Project ID: $PROJECT_ID"
echo "Token length: ${#TOKEN} chars"

HTTP=$(curl -sS -o /tmp/cf-test.json -w "%{http_code}" \
  -H "x-api-key: $TOKEN" \
  -H "Accept: application/json" \
  "https://api.curseforge.com/v1/mods/$PROJECT_ID")

echo "GET /v1/mods/$PROJECT_ID -> HTTP $HTTP"

if [[ "$HTTP" == "200" ]]; then
  jq -r '"Projeto: \(.data.name) (slug: \(.data.slug), id: \(.data.id))"' /tmp/cf-test.json
  echo "OK: token le o projeto. Upload exige ser autor/equipe com permissao de arquivo."
  exit 0
fi

cat /tmp/cf-test.json
echo ""
if [[ "$HTTP" == "403" ]]; then
  echo "403 Forbidden — verifique token, project ID 1580917 no secret, e se a conta e dona do projeto." >&2
fi
exit 1
