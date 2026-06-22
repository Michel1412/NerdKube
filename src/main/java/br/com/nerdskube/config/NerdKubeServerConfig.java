package br.com.nerdskube.config;

import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class NerdKubeServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue COMMAND_CONFLICTS_ENABLED;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CONFLICT_LITERALS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> PRIORITY_MOD_IDS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SECONDARY_ALIASES;

    public static final ModConfigSpec.BooleanValue RECIPE_OVERRIDES_ENABLED;
    public static final ModConfigSpec.BooleanValue RECIPE_OVERRIDES_VERBOSE_LOG;

    public static final ModConfigSpec.BooleanValue FAKE_PLAYER_PROGRESSION_GUARD_ENABLED;
    public static final ModConfigSpec.BooleanValue FAKE_PLAYER_CHUNK_LOCK_ENABLED;

    public static final ModConfigSpec.DoubleValue EXPLORATION_LOOT_CHANCE;
    public static final ModConfigSpec.IntValue EXPLORATION_LOOT_MIN_ARTIFACTS;

    public static final ModConfigSpec.IntValue TRANSMUTATION_XP_LEVEL_COST;
    public static final ModConfigSpec.DoubleValue TRANSMUTATION_SUCCESS_CHANCE;

    public static final ModConfigSpec SPEC;

    static {
        BUILDER.comment("Resolve colisões de comandos Brigadier (ex.: /regen duplicado entre mods).");
        BUILDER.push("commandConflicts");

        COMMAND_CONFLICTS_ENABLED = BUILDER
                .comment("Ativa interceptação e re-registro de literais conflitantes.")
                .define("enabled", true);

        CONFLICT_LITERALS = BUILDER
                .comment("Literais raiz monitorados (ex.: regen).")
                .defineList("literals", List.of("regen"), o -> o instanceof String s && !s.isBlank());

        PRIORITY_MOD_IDS = BUILDER
                .comment("Mod IDs que mantêm o literal original (WorldEdit e variantes).")
                .defineList(
                        "priorityModIds",
                        List.of("worldedit", "worldeditforge", "worldedit-neoforge", "worldeditcfc"),
                        o -> o instanceof String s && !s.isBlank());

        SECONDARY_ALIASES = BUILDER
                .comment("Aliases para registrantes não prioritários, em ordem.")
                .defineList("secondaryAliases", List.of("weregen", "regen2"), o -> o instanceof String s && !s.isBlank());

        BUILDER.pop();

        BUILDER.comment("Substituição programática de receitas de outros mods (Mekanism, EvilCraft).");
        BUILDER.push("recipeOverrides");

        RECIPE_OVERRIDES_ENABLED = BUILDER
                .comment("Ativa overrides de receita após o RecipeManager carregar.")
                .define("enabled", true);

        RECIPE_OVERRIDES_VERBOSE_LOG = BUILDER
                .comment("Loga ingredientes de cada receita substituída (útil para debug).")
                .define("verboseLog", false);

        BUILDER.pop();

        BUILDER.comment("Proteção contra automações em rituais manuais e chunks de terceiros.");
        BUILDER.push("fakePlayer");

        FAKE_PLAYER_PROGRESSION_GUARD_ENABLED = BUILDER
                .comment("Bloqueia FakePlayers em blocos/itens de progressão NerdKube.")
                .define("progressionGuardEnabled", true);

        FAKE_PLAYER_CHUNK_LOCK_ENABLED = BUILDER
                .comment("Bloqueia automações em chunks claimed por outros jogadores (requer FTB Chunks).")
                .define("chunkLockEnabled", true);

        BUILDER.pop();

        BUILDER.comment("Taxas da progressão de exploração e alquimia proibida.");
        BUILDER.push("progression");

        EXPLORATION_LOOT_CHANCE = BUILDER
                .comment("Chance (0.0–1.0) de dropar fragmento_combate_stage2 em baús de exploração.")
                .defineInRange("explorationLootChance", 0.015D, 0.0D, 1.0D);

        EXPLORATION_LOOT_MIN_ARTIFACTS = BUILDER
                .comment("Mínimo de itens da tag artifacts:artifacts equipados para elegibilidade ao loot.")
                .defineInRange("explorationLootMinArtifacts", 3, 0, 64);

        TRANSMUTATION_XP_LEVEL_COST = BUILDER
                .comment("Níveis de XP consumidos na roleta da Mesa de Transmutação (Shift + Beacon).")
                .defineInRange("transmutationXpLevelCost", 30, 0, 1000);

        TRANSMUTATION_SUCCESS_CHANCE = BUILDER
                .comment("Chance (0.0–1.0) de obter poeira_transmutacao_proibida na roleta.")
                .defineInRange("transmutationSuccessChance", 0.02D, 0.0D, 1.0D);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    private NerdKubeServerConfig() {}
}
