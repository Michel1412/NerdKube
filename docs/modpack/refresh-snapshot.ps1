# Atualiza o snapshot do modpack NerdCube em docs/modpack/
# Uso: .\docs\modpack\refresh-snapshot.ps1
#      .\docs\modpack\refresh-snapshot.ps1 -InstancePath "D:\Outro\Caminho\NerdCube"

param(
    [string]$InstancePath = "G:\CurseForge\minecraft\Instances\NerdCube"
)

$ErrorActionPreference = "Stop"
$instance = $InstancePath
$outDir = Join-Path $PSScriptRoot "."

if (-not (Test-Path $instance)) {
    Write-Error "Instância não encontrada: $instance"
}

$mods = Get-ChildItem "$instance\mods\*.jar" | Sort-Object Name
$mods | ForEach-Object { $_.Name } | Out-File -Encoding utf8 (Join-Path $outDir "mods-list.txt")

$json = Get-Content "$instance\minecraftinstance.json" | ConvertFrom-Json
$addons = $json.installedAddons

@{
    snapshotDate = (Get-Date -Format "yyyy-MM-dd")
    minecraft = "1.21.1"
    neoforge = $json.baseModLoader.forgeVersion
    modJarCount = $mods.Count
    curseforgeAddonCount = $addons.Count
    instancePath = $instance
    addons = ($addons | Select-Object name, fileNameOnDisk, dateInstalled)
} | ConvertTo-Json -Depth 4 | Out-File -Encoding utf8 (Join-Path $outDir "curseforge-addons.json")

Write-Host "Snapshot atualizado: $($mods.Count) JARs, $($addons.Count) addons CurseForge."
Write-Host "Revise manualmente manifest.json e endgame-baseline.md se houver mudanças de design."
