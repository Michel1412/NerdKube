package br.com.nerdskube.config;

import java.util.List;
import java.util.Set;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class CommandConflictConfigAccess {
    private static final boolean DEFAULT_ENABLED = true;
    private static final List<String> DEFAULT_LITERALS = List.of("regen");
    private static final List<String> DEFAULT_PRIORITY_MOD_IDS = List.of(
            "worldedit",
            "worldeditforge",
            "worldedit-neoforge",
            "worldeditcfc");
    private static final List<String> DEFAULT_SECONDARY_ALIASES = List.of("weregen", "regen2");

    private CommandConflictConfigAccess() {}

    public static boolean isEnabled() {
        return getOrDefault(NerdKubeServerConfig.COMMAND_CONFLICTS_ENABLED, DEFAULT_ENABLED);
    }

    public static Set<String> monitoredLiterals() {
        return Set.copyOf(getListOrDefault(NerdKubeServerConfig.CONFLICT_LITERALS, DEFAULT_LITERALS));
    }

    public static Set<String> priorityModIds() {
        return Set.copyOf(getListOrDefault(NerdKubeServerConfig.PRIORITY_MOD_IDS, DEFAULT_PRIORITY_MOD_IDS));
    }

    public static List<String> secondaryAliases() {
        return List.copyOf(getListOrDefault(NerdKubeServerConfig.SECONDARY_ALIASES, DEFAULT_SECONDARY_ALIASES));
    }

    public static boolean isConfigLoaded() {
        try {
            NerdKubeServerConfig.COMMAND_CONFLICTS_ENABLED.get();
            return true;
        } catch (IllegalStateException ex) {
            return false;
        }
    }

    private static <T> T getOrDefault(ModConfigSpec.ConfigValue<T> value, T defaultValue) {
        try {
            return value.get();
        } catch (IllegalStateException ex) {
            return defaultValue;
        }
    }

    private static List<String> getListOrDefault(
            ModConfigSpec.ConfigValue<List<? extends String>> value,
            List<String> defaultValue) {
        try {
            return List.copyOf(value.get());
        } catch (IllegalStateException ex) {
            return defaultValue;
        }
    }
}
