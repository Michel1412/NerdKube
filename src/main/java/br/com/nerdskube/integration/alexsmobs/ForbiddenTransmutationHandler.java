package br.com.nerdskube.integration.alexsmobs;

import br.com.nerdskube.config.NerdKubeServerConfigAccess;
import br.com.nerdskube.NerdKube;
import br.com.nerdskube.integration.fakeplayer.FakePlayerProgressionGuard;
import br.com.nerdskube.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Roleta alquímica na Mesa de Transmutação do Alex's Mobs ({@code alexsmobs:transmutation_table}).
 * Shift + clique direito com Beacon: 30 níveis de XP + 1 Beacon; 2% de Poeira de Transmutação Proibida.
 * O item também pode sair no slot raro da mesa (loot table {@code alexsmobs:gameplay/transmutation_table_rare}).
 */
@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class ForbiddenTransmutationHandler {
    private static final ResourceLocation TRANSMUTATION_TABLE =
            ResourceLocation.fromNamespaceAndPath("alexsmobs", "transmutation_table");

    private ForbiddenTransmutationHandler() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!ModList.get().isLoaded("alexsmobs") || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Player player = event.getEntity();
        if (FakePlayerProgressionGuard.isAutomationPlayer(player)) {
            return;
        }
        if (!player.isShiftKeyDown()) {
            return;
        }

        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!held.is(Items.BEACON)) {
            return;
        }

        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (!state.is(BuiltInRegistries.BLOCK.get(TRANSMUTATION_TABLE))) {
            return;
        }

        event.setCanceled(true);

        int xpCost = NerdKubeServerConfigAccess.transmutationXpLevelCost();
        if (player.experienceLevel < xpCost) {
            player.displayClientMessage(Component.translatable("nerdkube.transmutation.insufficient_xp", xpCost), true);
            return;
        }

        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos pos = event.getPos();

        player.giveExperienceLevels(-xpCost);
        held.shrink(1);

        if (serverLevel.random.nextFloat() <= NerdKubeServerConfigAccess.transmutationSuccessChance()) {
            ItemStack reward = new ItemStack(ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get());
            ItemEntity drop = new ItemEntity(
                    serverLevel,
                    pos.getX() + 0.5D,
                    pos.getY() + 1.1D,
                    pos.getZ() + 0.5D,
                    reward);
            drop.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(drop);
            serverLevel.sendParticles(
                    ParticleTypes.TOTEM_OF_UNDYING,
                    pos.getX() + 0.5D,
                    pos.getY() + 1.0D,
                    pos.getZ() + 0.5D,
                    24,
                    0.4D,
                    0.5D,
                    0.4D,
                    0.08D);
            serverLevel.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.9F, 1.6F);
            player.displayClientMessage(Component.translatable("nerdkube.transmutation.success"), true);
        } else {
            serverLevel.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.8F, 0.9F);
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    pos.getX() + 0.5D,
                    pos.getY() + 0.8D,
                    pos.getZ() + 0.5D,
                    16,
                    0.3D,
                    0.2D,
                    0.3D,
                    0.02D);
            player.displayClientMessage(Component.translatable("nerdkube.transmutation.fail"), true);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.containerMenu.broadcastChanges();
        }
    }
}
