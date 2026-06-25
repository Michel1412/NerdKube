# Testa token + endpoints CurseForge (mesmo check do CI).
# Uso:
#   $env:CURSEFORGE_TOKEN = "seu-token-de-autor"
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
    Write-Host "ERRO: defina CURSEFORGE_TOKEN (https://authors.curseforge.com/#/api-tokens)." -ForegroundColor Red
    exit 1
}

if ($token.StartsWith('$2a$10$')) {
    Write-Host "ERRO: token do Studios console (x-api-key) nao serve para upload (X-Api-Token)." -ForegroundColor Red
    Write-Host "Gere um token em https://authors.curseforge.com/#/api-tokens" -ForegroundColor Yellow
    exit 1
}

$headers = @{ "X-Api-Token" = $token; "Accept" = "application/json" }

function Test-UploadApi([string]$Path) {
    $uri = "https://minecraft.curseforge.com/api/$Path"
    try {
        $r = Invoke-WebRequest -Uri $uri -Headers $headers -UseBasicParsing
        return @{ Uri = $uri; Status = [int]$r.StatusCode; Json = ($r.Content | ConvertFrom-Json) }
    } catch {
        $status = [int]$_.Exception.Response.StatusCode
        return @{ Uri = $uri; Status = $status; Json = $null }
    }
}

Write-Host "Project ID: $projectId | Token length: $($token.Length) chars"

foreach ($path in @("game/version-types?cache=true", "game/versions?cache=true")) {
    $r = Test-UploadApi $path
    Write-Host "GET $($r.Uri) -> HTTP $($r.Status)"
    if ($r.Status -ne 200) { exit 1 }
}

$versions = Test-UploadApi "game/versions?cache=true"
$has1211 = $false
foreach ($group in $versions.Json) {
    foreach ($v in $group.versions) {
        if ($v.name -eq "1.21.1") { $has1211 = $true }
    }
}
if (-not $has1211) {
    Write-Host "ERRO: versao 1.21.1 nao encontrada na Upload API." -ForegroundColor Red
    exit 1
}
Write-Host "Versao Minecraft 1.21.1: OK" -ForegroundColor Green
Write-Host "OK: token de upload validado." -ForegroundColor Green
