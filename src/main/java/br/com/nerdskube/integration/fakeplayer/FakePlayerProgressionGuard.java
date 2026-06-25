package br.com.nerdskube.integration.fakeplayer;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.registry.ModBlocks;
import br.com.nerdskube.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.Locale;
import java.util.Set;

/**
 * Bloqueia automações em rituais manuais do NerdKube.
 * O Enderic Laser (Oritech) usa {@code FakeMachinePlayer} — subclasse de {@link ServerPlayer} com perfil do dono.
 */
public final class FakePlayerProgressionGuard {
    private static final ResourceLocation BAKER_STATION =
            ResourceLocation.fromNamespaceAndPath("bakery", "baker_station");

    private static final Set<Item> PROTECTED_PROGRESSION_ITEMS = Set.of(
            ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get(),
            ModItems.RUNIC_CRYSTAL_CAKE.get(),
            ModItems.OLHO_DESBRAVADOR_PRIMAL.get(),
            ModItems.RELIQUIA_DESBRAVADOR.get(),
            ModItems.MASSA_RUNICA_CRUA.get(),
            ModItems.FRAGMENTO_AGRI_STAGE1.get(),
            ModItems.COMPONENTE_AGRI_STAGE1_COMPLETO.get(),
            ModItems.RAIZES_DOCES_FRITAS.get(),
            ModItems.RAIZES_ANCESTRAIS_EM_CONSERVA.get());

    private FakePlayerProgressionGuard() {}

    public static boolean isAutomationPlayer(Player player) {
        if (player == null) {
            return false;
        }
        if (AutomationPlayerMark.isMarked(player)) {
            return true;
        }
        if (player instanceof FakePlayer) {
            return true;
        }
        if (isSyntheticServerPlayer(player)) {
            return true;
        }
        String name = player.getGameProfile().getName().toLowerCase(Locale.ROOT);
        return name.contains("oritech_laser")
                || name.contains("[justdiresfakeplayer]")
                || name.contains("fakeplayer");
    }

    private static boolean isSyntheticServerPlayer(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return false;
        }
        Class<?> type = player.getClass();
        if (type == ServerPlayer.class) {
            return false;
        }
        String typeName = type.getName().toLowerCase(Locale.ROOT);
        return typeName.contains("fakemachineplayer")
                || typeName.contains("fakeplayer")
                || typeName.contains("oritech");
    }

    public static boolean isProtectedInteractionBlock(Block block) {
        return isNerdKubeProgressionBlock(block) || isExternalProgressionBlock(block);
    }

    /** Blocos NerdKube que nunca devem ser quebrados/coletados por laser. */
    public static boolean isNerdKubeProgressionBlock(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (!NerdKube.MOD_ID.equals(id.getNamespace())) {
            return false;
        }
        return block == ModBlocks.MASSA_RUNICA_CRUA.get()
                || block == ModBlocks.BOLO_MISTICO_COLOCAVEL.get()
                || block == ModBlocks.CUBE_MAKER.get()
                || block == ModBlocks.PEDESTAL_TECH.get()
                || block == ModBlocks.PEDESTAL_MAGIA.get()
                || block == ModBlocks.PEDESTAL_EXPLORACAO.get()
                || block == ModBlocks.PEDESTAL_AGRICULTURA.get()
                || block == ModBlocks.PEDESTAL_BRUTO.get()
                || block == ModBlocks.NERD_CUBE.get()
                || block == ModBlocks.ETERNAL_CUBE.get()
                || block == ModBlocks.SCULK_GEAR_CRYSTAL.get()
                || block == ModBlocks.AGRI_CORE_CROP.get();
    }

    private static boolean isExternalProgressionBlock(Block block) {
        if (block == Blocks.ENCHANTING_TABLE) {
            return true;
        }
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        return id.equals(BAKER_STATION);
    }

    public static boolean isProtectedPickupItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return PROTECTED_PROGRESSION_ITEMS.contains(stack.getItem());
    }

    public static boolean isProtectedHeldItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return stack.is(ModBlocks.MASSA_RUNICA_CRUA.get().asItem())
                || stack.is(Items.BEACON)
                || stack.is(ModItems.OLHO_DESBRAVADOR_PRIMAL.get())
                || isProtectedPickupItem(stack);
    }
}
