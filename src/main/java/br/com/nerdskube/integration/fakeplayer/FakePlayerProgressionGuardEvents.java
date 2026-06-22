package br.com.nerdskube.integration.fakeplayer;

import br.com.nerdskube.config.NerdKubeServerConfigAccess;
import br.com.nerdskube.NerdKube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Impede lasers/automações de burlar rituais manuais — inclusive quando o fake player usa o UUID do dono.
 */
@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class FakePlayerProgressionGuardEvents {
    private FakePlayerProgressionGuardEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!NerdKubeServerConfigAccess.fakePlayerProgressionGuardEnabled()) {
            return;
        }
        if (event.getLevel().isClientSide()) {
            return;
        }
        Player player = event.getPlayer();
        if (!FakePlayerProgressionGuard.isAutomationPlayer(player)) {
            return;
        }
        BlockState state = event.getState();
        if (FakePlayerProgressionGuard.isProtectedInteractionBlock(state.getBlock())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!NerdKubeServerConfigAccess.fakePlayerProgressionGuardEnabled()) {
            return;
        }
        if (event.getLevel().isClientSide()) {
            return;
        }

        Player player = event.getEntity();
        if (!FakePlayerProgressionGuard.isAutomationPlayer(player)) {
            return;
        }

        Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
        if (FakePlayerProgressionGuard.isProtectedInteractionBlock(block)
                || FakePlayerProgressionGuard.isProtectedHeldItem(event.getItemStack())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemPickup(ItemEntityPickupEvent.Pre event) {
        if (!NerdKubeServerConfigAccess.fakePlayerProgressionGuardEnabled()) {
            return;
        }
        if (!FakePlayerProgressionGuard.isAutomationPlayer(event.getPlayer())) {
            return;
        }
        if (FakePlayerProgressionGuard.isProtectedPickupItem(event.getItemEntity().getItem())) {
            event.setCanPickup(TriState.FALSE);
        }
    }
}
