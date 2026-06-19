#!/usr/bin/env bash
# Baixa JARs compileOnly para CI (GitHub Actions).
# Versões fixas em gradle.properties — atualize junto com o modpack.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
LIBS="$ROOT/libs"
PROPS="$ROOT/gradle.properties"

mkdir -p "$LIBS"

get_prop() {
  local key="$1"
  grep "^${key}=" "$PROPS" | cut -d= -f2- | tr -d '\r'
}

download_modrinth() {
  local project="$1"
  local filename="$2"
  local dest="$LIBS/$filename"

  if [[ -f "$dest" ]]; then
    echo "OK (cached): $filename"
    return 0
  fi

  echo "Downloading $filename (Modrinth: $project)..."
  local api_url="https://api.modrinth.com/v2/project/${project}/version?game_versions=%5B%221.21.1%22%5D&loaders=%5B%22neoforge%22%5D"
  local file_url
  file_url=$(curl -fsSL "$api_url" | jq -r --arg f "$filename" '
    .[] | .files[] | select(.filename == $f) | .url' | head -1)

  if [[ -z "$file_url" || "$file_url" == "null" ]]; then
    echo "ERROR: $filename not found on Modrinth (project: $project)." >&2
    echo "Update scripts/ci-setup-libs.sh or gradle.properties jar names." >&2
    exit 1
  fi

  curl -fL "$file_url" -o "$dest"
  echo "Saved: $dest"
}

download_curseforge_cdn() {
  local file_id="$1"
  local filename="$2"
  local dest="$LIBS/$filename"
  local folder1=$((file_id / 1000))
  local folder2
  folder2=$(printf '%03d' $((file_id % 1000)))

  if [[ -f "$dest" ]]; then
    echo "OK (cached): $filename"
    return 0
  fi

  echo "Downloading $filename (CurseForge CDN file $file_id)..."
  local cdn_name
  for cdn_name in "$filename" "JustDireThings-1.5.7.jar"; do
    local url="https://edge.forgecdn.net/files/${folder1}/${folder2}/${cdn_name}"
    if curl -fsSL "$url" -o "$dest"; then
      echo "Saved: $dest (from $cdn_name)"
      return 0
    fi
  done

  echo "ERROR: CurseForge CDN download failed for $filename (file id $file_id)." >&2
  exit 1
}

JADE_JAR="$(get_prop jade_jar_name)"
FD_JAR="$(get_prop farmersdelight_jar_name)"
JEI_JAR="$(get_prop jei_jar_name)"
MEK_JAR="$(get_prop mekanism_jar_name)"
MEKGEN_JAR="$(get_prop mekanism_generators_jar_name)"
ORITECH_JAR="$(get_prop oritech_jar_name)"
JDT_JAR="$(get_prop justdirethings_jar_name)"

download_modrinth "jade" "$JADE_JAR"
download_modrinth "farmers-delight" "$FD_JAR"
download_modrinth "jei" "$JEI_JAR"
download_modrinth "mekanism" "$MEK_JAR"
download_modrinth "mekanism-generators" "$MEKGEN_JAR"
download_modrinth "oritech" "$ORITECH_JAR"
# Just Dire Things — CurseForge only (NeoForge 1.21.1, v1.5.7)
download_curseforge_cdn "7463040" "$JDT_JAR"

echo "All compile-only libs ready in $LIBS"
