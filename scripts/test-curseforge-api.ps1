# Testa token + project ID da CurseForge (mesmo check do CI).
# Uso:
#   $env:CURSEFORGE_TOKEN = "seu-token-do-console"
#   $env:CURSEFORGE_PROJECT_ID = "1580917"
#   .\scripts\test-curseforge-api.ps1

$ErrorActionPreference = "Stop"

$token = $env:CURSEFORGE_TOKEN?.Trim()
$projectId = if ($env:CURSEFORGE_PROJECT_ID) { $env:CURSEFORGE_PROJECT_ID.Trim() } else { "1580917" }

if ([string]::IsNullOrWhiteSpace($token)) {
    Write-Host "ERRO: defina CURSEFORGE_TOKEN (https://console.curseforge.com/ -> API Keys)." -ForegroundColor Red
    exit 1
}

Write-Host "Project ID: $projectId"
Write-Host "Token length: $($token.Length) chars (nao exibimos o valor)"

$headers = @{ "x-api-key" = $token; "Accept" = "application/json" }
$uri = "https://api.curseforge.com/v1/mods/$projectId"

try {
    $response = Invoke-WebRequest -Uri $uri -Headers $headers -UseBasicParsing
    Write-Host "GET $uri -> HTTP $($response.StatusCode)" -ForegroundColor Green
    $json = $response.Content | ConvertFrom-Json
    Write-Host "Projeto: $($json.data.name) (slug: $($json.data.slug), id: $($json.data.id))"
    Write-Host "OK: token tem acesso de leitura ao projeto. Upload exige ser autor/equipe com permissao de arquivo."
} catch {
    $status = $_.Exception.Response.StatusCode.value__
    Write-Host "GET $uri -> HTTP $status" -ForegroundColor Red
    if ($status -eq 403) {
        Write-Host @"

403 Forbidden — causas mais comuns:
  1. Token invalido, expirado ou copiado com aspas/espacos extras no GitHub Secret
  2. CURSEFORGE_PROJECT_ID errado no secret (use 1580917 para este mod)
  3. Conta do token nao e dona/membro do projeto na CurseForge
  4. App da API nao aprovada em https://console.curseforge.com/

GitHub: Settings -> Secrets -> Actions -> CURSEFORGE_TOKEN e CURSEFORGE_PROJECT_ID=1580917
"@ -ForegroundColor Yellow
    }
    exit 1
}
