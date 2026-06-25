#!/usr/bin/env bash
# Valida token CurseForge para mc-publish (Upload API + metadados).
set -euo pipefail

sanitize_token() {
  echo -n "$1" | tr -d '\r\n' | sed 's/^["'\'']//;s/["'\'']$//'
}

write_github_env() {
  local name="$1"
  local value="$2"
  if [[ -n "${GITHUB_ENV:-}" ]]; then
    {
      echo "${name}<<CURSEFORGE_ENV_EOF"
      printf '%s\n' "$value"
      echo "CURSEFORGE_ENV_EOF"
    } >> "$GITHUB_ENV"
  fi
}

TOKEN="$(sanitize_token "${CURSEFORGE_TOKEN:-}")"
PROJECT_ID="$(echo -n "${CURSEFORGE_PROJECT_ID:-1580917}" | tr -d '[:space:]')"

if [[ -z "$TOKEN" ]] || [[ -z "$PROJECT_ID" ]]; then
  echo "::error::Defina CURSEFORGE_TOKEN e CURSEFORGE_PROJECT_ID."
  exit 1
fi

if [[ ! "$PROJECT_ID" =~ ^[0-9]+$ ]]; then
  echo "::error::CURSEFORGE_PROJECT_ID deve ser numerico (ex.: 1580917)."
  exit 1
fi

echo "CurseForge project ID: $PROJECT_ID"
echo "Token length: ${#TOKEN} chars"

if [[ "$TOKEN" == \$2a\$10\$* ]]; then
  echo "::error::CURSEFORGE_TOKEN parece ser chave do CurseForge for Studios (console.curseforge.com, header x-api-key)."
  echo "::error::O mc-publish envia arquivos pela Upload API de autores (header X-Api-Token)."
  echo "::error::Gere um token em https://authors.curseforge.com/#/api-tokens e atualize o secret CURSEFORGE_TOKEN."
  exit 1
fi

upload_get() {
  local path="$1"
  local out="$2"
  local http
  http=$(curl -sS -o "$out" -w "%{http_code}" \
    -H "X-Api-Token: $TOKEN" \
    -H "Accept: application/json" \
    "https://minecraft.curseforge.com/api${path}")
  echo "$http"
}

HTTP=$(upload_get "/game/version-types?cache=true" /tmp/cf-upload-types.json)
echo "GET /api/game/version-types (Upload API) -> HTTP $HTTP"
if [[ "$HTTP" != "200" ]]; then
  cat /tmp/cf-upload-types.json || true
  if jq -e '.errorCode == 3' /tmp/cf-upload-types.json >/dev/null 2>&1; then
    echo "::error::Token invalido para upload (X-Api-Token). Use https://authors.curseforge.com/#/api-tokens — nao o console Studios."
  else
    echo "::error::Falha na autenticacao da Upload API (mesmo endpoint usado pelo mc-publish)."
  fi
  exit 1
fi

HTTP=$(upload_get "/game/versions?cache=true" /tmp/cf-upload-versions.json)
echo "GET /api/game/versions (Upload API) -> HTTP $HTTP"
if [[ "$HTTP" != "200" ]]; then
  cat /tmp/cf-upload-versions.json || true
  echo "::error::Falha ao listar versoes na Upload API."
  exit 1
fi

if ! jq -e '[.[].versions[]? | .name] | any(. == "1.21.1")' /tmp/cf-upload-versions.json >/dev/null; then
  echo "::error::Versao Minecraft 1.21.1 nao encontrada na Upload API."
  exit 1
fi
echo "Versao Minecraft 1.21.1: OK na Upload API"

if jq -e '[.[] | select((.slug // "") | test("modloader|loader"; "i")) | .versions[]? | .name] | any(. | ascii_downcase == "neoforge")' /tmp/cf-upload-versions.json >/dev/null; then
  echo "Mod loader NeoForge: OK na Upload API"
else
  echo "::warning::NeoForge nao encontrado em /api/game/versions; confira o projeto na CurseForge."
fi

write_github_env "CURSEFORGE_TOKEN" "$TOKEN"
write_github_env "CURSEFORGE_PROJECT_ID" "$PROJECT_ID"

echo "OK: token de upload e endpoints do mc-publish validados."
