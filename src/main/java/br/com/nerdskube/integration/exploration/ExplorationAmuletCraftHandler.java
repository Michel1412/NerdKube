package br.com.nerdskube.integration.exploration;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Ritual final na Mesa de Encantamentos: Olho Primal + componentes → Relíquia do Desbravador.
 * Exige Eterna/Quanta/Arcana altos (documentado no JEI; infusão Apothic opcional em datapack).
 */
@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class ExplorationAmuletCraftHandler {
    private ExplorationAmuletCraftHandler() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND || event.getLevel().isClientSide()) {
            return;
        }

        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (!state.is(Blocks.ENCHANTING_TABLE)) {
            return;
        }

        Player player = event.getEntity();
        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!held.is(ModItems.OLHO_DESBRAVADOR_PRIMAL.get())) {
            return;
        }

        if (!hasRequiredIngredients(player.getInventory())) {
            player.displayClientMessage(Component.translatable("nerdkube.exploration.amulet.missing_ingredients"), true);
            event.setCanceled(true);
            return;
        }

        if (!consumeIngredients(player.getInventory())) {
            return;
        }

        held.shrink(1);
        ItemStack result = new ItemStack(ModItems.RELIQUIA_DESBRAVADOR.get());
        if (!player.getInventory().add(result)) {
            player.drop(result, false);
        }

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 0.85F);
        level.levelEvent(2001, pos, Block.getId(state));
        player.displayClientMessage(Component.translatable("nerdkube.exploration.amulet.complete"), true);

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.containerMenu.broadcastChanges();
        }
        event.setCanceled(true);
    }

    private static boolean hasRequiredIngredients(Inventory inventory) {
        return count(inventory, ModItems.LAMINA_CONQUISTADOR.get()) >= 1
                && count(inventory, ModItems.COMPONENTE_COMBATE_STAGE1_COMPLETO.get()) >= 3
                && count(inventory, ModItems.COMPONENTE_COMBATE_STAGE2_COMPLETO.get()) >= 3
                && count(inventory, ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get()) >= 1;
    }

    private static boolean consumeIngredients(Inventory inventory) {
        if (!hasRequiredIngredients(inventory)) {
            return false;
        }
        remove(inventory, ModItems.LAMINA_CONQUISTADOR.get(), 1);
        remove(inventory, ModItems.COMPONENTE_COMBATE_STAGE1_COMPLETO.get(), 3);
        remove(inventory, ModItems.COMPONENTE_COMBATE_STAGE2_COMPLETO.get(), 3);
        remove(inventory, ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get(), 1);
        return true;
    }

    private static int count(Inventory inventory, Item item) {
        int total = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(item)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    private static void remove(Inventory inventory, Item item, int amount) {
        int remaining = amount;
        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.is(item)) {
                continue;
            }
            int shrink = Math.min(remaining, stack.getCount());
            stack.shrink(shrink);
            remaining -= shrink;
        }
    }
}
