#!/usr/bin/env bash
# Valida token CurseForge e endpoints usados pelo mc-publish no upload.
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

cf_get() {
  local path="$1"
  local out="$2"
  local http
  http=$(curl -sS -o "$out" -w "%{http_code}" \
    -H "x-api-key: $TOKEN" \
    -H "Accept: application/json" \
    "https://api.curseforge.com/v1${path}")
  echo "$http"
}

HTTP=$(cf_get "/games" /tmp/cf-games.json)
echo "GET /v1/games -> HTTP $HTTP"
if [[ "$HTTP" != "200" ]]; then
  cat /tmp/cf-games.json || true
  exit 1
fi

HTTP=$(cf_get "/mods/$PROJECT_ID" /tmp/cf-mod.json)
echo "GET /v1/mods/$PROJECT_ID -> HTTP $HTTP"
if [[ "$HTTP" != "200" ]]; then
  cat /tmp/cf-mod.json || true
  exit 1
fi
jq -r '"Projeto: \(.data.name) (\(.data.slug)) id=\(.data.id)"' /tmp/cf-mod.json

HTTP=$(cf_get "/games/432/versions" /tmp/cf-mc-versions.json)
echo "GET /v1/games/432/versions -> HTTP $HTTP"
if [[ "$HTTP" != "200" ]]; then
  cat /tmp/cf-mc-versions.json || true
  echo "::error::Falha ao listar versoes do Minecraft — o upload precisa deste endpoint."
  exit 1
fi

# v1 retorna { data: [ { type, versions: ["1.21.1", ...] } ] } — strings, nao objetos
if ! jq -e 'any(.data[]?.versions[]?; . == "1.21.1")' /tmp/cf-mc-versions.json >/dev/null; then
  echo "::error::Versao 1.21.1 nao encontrada em GET /v1/games/432/versions."
  jq -r '.data[]? | "type=\(.type) sample=\(.versions[0] // "n/a")"' /tmp/cf-mc-versions.json | head -5 || true
  exit 1
fi
echo "Versao Minecraft 1.21.1: OK na API"

HTTP=$(cf_get "/minecraft/modloader" /tmp/cf-loaders.json)
echo "GET /v1/minecraft/modloader -> HTTP $HTTP"
if [[ "$HTTP" != "200" ]]; then
  cat /tmp/cf-loaders.json || true
  echo "::error::Falha ao listar mod loaders — o upload precisa deste endpoint."
  exit 1
fi

if jq -e 'any(.data[]?; ((.name // "") | ascii_downcase) == "neoforge")' /tmp/cf-loaders.json >/dev/null; then
  echo "Mod loader NeoForge: OK na API"
else
  echo "::warning::NeoForge nao encontrado em /v1/minecraft/modloader; confira o projeto na CurseForge."
fi

write_github_env "CURSEFORGE_TOKEN" "$TOKEN"
write_github_env "CURSEFORGE_PROJECT_ID" "$PROJECT_ID"

echo "OK: autenticacao e endpoints de upload validados."
