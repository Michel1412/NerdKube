# Testa token + endpoints CurseForge (mesmo check do CI).
# Uso:
#   $env:CURSEFORGE_TOKEN = "sua-chave-do-console"
#   $env:CURSEFORGE_PROJECT_ID = "1580917"
#   .\scripts\test-curseforge-api.ps1

$ErrorActionPreference = "Stop"

$bash = Get-Command bash -ErrorAction SilentlyContinue
if ($bash) {
    & $bash.Source (Join-Path $PSScriptRoot "curseforge-validate.sh")
    exit $LASTEXITCODE
}

function Sanitize-Token([string]$Value) {
    if ([string]::IsNullOrEmpty($Value)) { return "" }
    return $Value.Trim().Trim('"').Trim("'") -replace "`r`n", ""
}

$token = Sanitize-Token $env:CURSEFORGE_TOKEN
$projectId = if ($env:CURSEFORGE_PROJECT_ID) { ($env:CURSEFORGE_PROJECT_ID -replace '\s', '') } else { "1580917" }

if ([string]::IsNullOrWhiteSpace($token)) {
    Write-Host "ERRO: defina CURSEFORGE_TOKEN (https://console.curseforge.com/)." -ForegroundColor Red
    exit 1
}

$headers = @{ "x-api-key" = $token; "Accept" = "application/json" }

function Test-Cf([string]$Path) {
    $uri = "https://api.curseforge.com/v1/$Path"
    try {
        $r = Invoke-WebRequest -Uri $uri -Headers $headers -UseBasicParsing
        return @{ Uri = $uri; Status = [int]$r.StatusCode; Json = ($r.Content | ConvertFrom-Json) }
    } catch {
        $status = [int]$_.Exception.Response.StatusCode
        return @{ Uri = $uri; Status = $status; Json = $null }
    }
}

Write-Host "Project ID: $projectId | Token length: $($token.Length) chars"

foreach ($path in @("games", "mods/$projectId", "games/432/versions", "minecraft/modloader")) {
    $r = Test-Cf $path
    Write-Host "GET $($r.Uri) -> HTTP $($r.Status)"
    if ($r.Status -ne 200) { exit 1 }
}

$versions = Test-Cf "games/432/versions"
$has1211 = $false
foreach ($group in $versions.Json.data) {
    foreach ($v in $group.versions) {
        if ($v -eq "1.21.1") { $has1211 = $true }
    }
}
if (-not $has1211) {
    Write-Host "ERRO: versao 1.21.1 nao encontrada na API." -ForegroundColor Red
    exit 1
}
Write-Host "Versao Minecraft 1.21.1: OK" -ForegroundColor Green
Write-Host "OK: autenticacao e endpoints validados." -ForegroundColor Green
