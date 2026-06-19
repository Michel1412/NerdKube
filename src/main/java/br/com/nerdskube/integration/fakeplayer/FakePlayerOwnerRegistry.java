package br.com.nerdskube.integration.fakeplayer;

import java.util.List;
import java.util.Optional;

import net.minecraft.resources.ResourceLocation;

/**
 * Blocos de automação que simulam jogador (clique, quebra, ferramentas).
 * Apenas mods presentes no modpack Nerds Quadrados.
 */
public final class FakePlayerOwnerRegistry {
    public enum Handling {
        /** NerdKube injeta UUID via mixin + evento de colocação. */
        MANAGED,
        /** Mod já persiste UUID do colocador nativamente. */
        NATIVE
    }

    public record Entry(ResourceLocation blockId, Handling handling, String modId) {}

    public static final List<Entry> ENTRIES = List.of(
            // Oritech — UUID aleatório no FakePlayer; gerenciado pelo NerdKube
            entry("oritech", "laser_arm_block", Handling.MANAGED),
            entry("oritech", "destroyer_block", Handling.MANAGED),

            // Just Dire Things — placedByUUID nativo em BaseMachineBE
            entry("justdirethings", "clickert1", Handling.NATIVE),
            entry("justdirethings", "clickert2", Handling.NATIVE),
            entry("justdirethings", "blockbreakert1", Handling.NATIVE),
            entry("justdirethings", "blockbreakert2", Handling.NATIVE),
            entry("justdirethings", "blockplacert1", Handling.NATIVE),
            entry("justdirethings", "blockplacert2", Handling.NATIVE),
            entry("justdirethings", "fluidplacert1", Handling.NATIVE),
            entry("justdirethings", "fluidplacert2", Handling.NATIVE),
            entry("justdirethings", "fluidcollectort1", Handling.NATIVE),
            entry("justdirethings", "fluidcollectort2", Handling.NATIVE),

            // Modular Routers — setOwner(Player) em setPlacedBy
            entry("modularrouters", "modular_router", Handling.NATIVE),

            // Mob Grinding Utils — setPlacer(Player) na serra
            entry("mob_grinding_utils", "saw", Handling.NATIVE));

    private FakePlayerOwnerRegistry() {}

    public static Optional<Entry> find(ResourceLocation blockId) {
        return ENTRIES.stream().filter(e -> e.blockId().equals(blockId)).findFirst();
    }

    public static boolean isManaged(ResourceLocation blockId) {
        return find(blockId).map(e -> e.handling() == Handling.MANAGED).orElse(false);
    }

    private static Entry entry(String namespace, String path, Handling handling) {
        return new Entry(ResourceLocation.fromNamespaceAndPath(namespace, path), handling, namespace);
    }
}
