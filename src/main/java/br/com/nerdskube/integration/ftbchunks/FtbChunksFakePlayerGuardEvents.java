package br.com.nerdskube.integration.ftbchunks;

import br.com.nerdskube.NerdKube;
import dev.ftb.mods.ftbchunks.api.Protection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Bloqueia FakePlayers/automações em chunks claimed por outros jogadores (FTB Chunks).
 */
@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class FtbChunksFakePlayerGuardEvents {
    private FtbChunksFakePlayerGuardEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        Player player = event.getPlayer();
        if (FtbChunksFakePlayerGuard.shouldBlockInteraction(
                serverLevel, player, event.getPos(), Protection.EDIT_BLOCK)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (FtbChunksFakePlayerGuard.shouldBlockInteraction(
                serverLevel, player, event.getPos(), Protection.EDIT_BLOCK)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (FtbChunksFakePlayerGuard.shouldBlockInteraction(
                serverLevel, event.getEntity(), event.getPos(), Protection.INTERACT_BLOCK)) {
            event.setCanceled(true);
        }
    }
}
