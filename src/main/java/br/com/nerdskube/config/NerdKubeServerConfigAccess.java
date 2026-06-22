package br.com.nerdskube.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class NerdKubeServerConfigAccess {
    private NerdKubeServerConfigAccess() {}

    public static boolean recipeOverridesEnabled() {
        return getOrDefault(NerdKubeServerConfig.RECIPE_OVERRIDES_ENABLED, true);
    }

    public static boolean recipeOverridesVerboseLog() {
        return getOrDefault(NerdKubeServerConfig.RECIPE_OVERRIDES_VERBOSE_LOG, false);
    }

    public static boolean fakePlayerProgressionGuardEnabled() {
        return getOrDefault(NerdKubeServerConfig.FAKE_PLAYER_PROGRESSION_GUARD_ENABLED, true);
    }

    public static boolean fakePlayerChunkLockEnabled() {
        return getOrDefault(NerdKubeServerConfig.FAKE_PLAYER_CHUNK_LOCK_ENABLED, true);
    }

    public static float explorationLootChance() {
        return getOrDefault(NerdKubeServerConfig.EXPLORATION_LOOT_CHANCE, 0.015D).floatValue();
    }

    public static int explorationLootMinArtifacts() {
        return getOrDefault(NerdKubeServerConfig.EXPLORATION_LOOT_MIN_ARTIFACTS, 3);
    }

    public static int transmutationXpLevelCost() {
        return getOrDefault(NerdKubeServerConfig.TRANSMUTATION_XP_LEVEL_COST, 30);
    }

    public static float transmutationSuccessChance() {
        return getOrDefault(NerdKubeServerConfig.TRANSMUTATION_SUCCESS_CHANCE, 0.02D).floatValue();
    }

    private static <T> T getOrDefault(ModConfigSpec.ConfigValue<T> value, T defaultValue) {
        try {
            return value.get();
        } catch (IllegalStateException ex) {
            return defaultValue;
        }
    }
}
