package br.com.nerdskube.integration.agriculture;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Permite colocar {@code massa_runica_crua} sobre a estação Caking do Bakery,
 * espelhando o comportamento do {@code bakery:cake_dough}.
 */
@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class BakerStationDoughHandler {
    private static final ResourceLocation BAKER_STATION =
            ResourceLocation.fromNamespaceAndPath("bakery", "baker_station");

    private BakerStationDoughHandler() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!ModList.get().isLoaded("bakery") || event.getLevel().isClientSide()) {
            return;
        }
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (!state.is(BuiltInRegistries.BLOCK.get(BAKER_STATION))) {
            return;
        }

        ItemStack held = event.getItemStack();
        if (!held.is(ModBlocks.MASSA_RUNICA_CRUA.get().asItem())) {
            return;
        }

        Level level = event.getLevel();
        BlockPos above = event.getPos().above();
        if (!level.isEmptyBlock(above)) {
            return;
        }

        Player player = event.getEntity();
        level.setBlock(above, ModBlocks.MASSA_RUNICA_CRUA.get().defaultBlockState(), Block.UPDATE_ALL);
        level.playSound(null, above, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.9F, 1.0F);
        level.levelEvent(2001, above, Block.getId(ModBlocks.MASSA_RUNICA_CRUA.get().defaultBlockState()));

        if (!player.isCreative()) {
            held.shrink(1);
        }
        event.setCanceled(true);
    }
}
