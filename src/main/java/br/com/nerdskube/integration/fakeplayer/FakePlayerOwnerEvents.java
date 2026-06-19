package br.com.nerdskube.integration.fakeplayer;

import br.com.nerdskube.NerdKube;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class FakePlayerOwnerEvents {
    private FakePlayerOwnerEvents() {}

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(event.getPlacedBlock().getBlock());
        FakePlayerOwnerRegistry.find(blockId).ifPresent(entry -> {
            if (entry.handling() != FakePlayerOwnerRegistry.Handling.MANAGED) {
                return;
            }
            if (!ModList.get().isLoaded(entry.modId())) {
                return;
            }
            if (!(event.getEntity() instanceof Player player)) {
                return;
            }

            Level level = (Level) event.getLevel();
            BlockEntity blockEntity = level.getBlockEntity(event.getPos());
            if (blockEntity instanceof FakePlayerOwnerAccess ownerAccess) {
                ownerAccess.nerdkube$setOwner(player.getUUID(), player.getGameProfile().getName());
            }
        });
    }
}
