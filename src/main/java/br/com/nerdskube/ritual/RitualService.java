package br.com.nerdskube.ritual;

import br.com.nerdskube.attachment.ModAttachments;
import br.com.nerdskube.block.entity.CubeMakerBlockEntity;
import br.com.nerdskube.block.entity.RitualPedestalBlockEntity;
import br.com.nerdskube.component.ModDataComponents;
import br.com.nerdskube.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class RitualService {
    public static final int CRAFT_BUTTON_ID = 0;

    private RitualService() {}

    public static boolean tryPerform(Level level, BlockPos center, Player player) {
        if (level.isClientSide) {
            return false;
        }

        if (ModAttachments.hasCraftedTrophy(player)) {
            player.displayClientMessage(Component.translatable("nerdkube.ritual.already_crafted"), true);
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(center);
        if (!(blockEntity instanceof CubeMakerBlockEntity cubeMaker)) {
            return false;
        }

        RitualValidator.Result validation = RitualValidator.validate(level, center, cubeMaker);
        if (!validation.success()) {
            sendValidationError(player, validation);
            return false;
        }

        consumeOfferings(level, center, cubeMaker);
        ModAttachments.setCraftedTrophy(player);

        ItemStack trophy = ModDataComponents.withCraftedBy(
                new ItemStack(ModBlocks.NERD_CUBE.get()),
                player.getScoreboardName());

        if (!player.getInventory().add(trophy)) {
            ItemEntity entity = new ItemEntity(level, center.getX() + 0.5, center.getY() + 1.0, center.getZ() + 0.5, trophy);
            level.addFreshEntity(entity);
        }

        RitualFinaleEffects.play(level, center);

        player.displayClientMessage(Component.translatable("nerdkube.ritual.success"), true);
        return true;
    }

    private static void sendValidationError(Player player, RitualValidator.Result validation) {
        switch (validation.failure()) {
            case MISSING_CRAFTER -> player.displayClientMessage(
                    Component.translatable("nerdkube.ritual.missing_crafter"), true);
            case MISSING_PEDESTAL -> player.displayClientMessage(
                    Component.translatable("nerdkube.ritual.missing_pedestal",
                            Component.translatable(validation.failedDirection().translationKey())), true);
            case WRONG_PEDESTAL_TYPE -> player.displayClientMessage(
                    Component.translatable("nerdkube.ritual.wrong_pedestal_type",
                            Component.translatable(validation.failedDirection().translationKey())), true);
            case WRONG_PEDESTAL_ITEM -> player.displayClientMessage(
                    Component.translatable("nerdkube.ritual.wrong_pedestal_item",
                            Component.translatable(validation.failedDirection().translationKey())), true);
        }
    }

    private static void consumeOfferings(Level level, BlockPos center, CubeMakerBlockEntity cubeMaker) {
        cubeMaker.getInventory().setStackInSlot(CubeMakerBlockEntity.SLOT_CRAFTING_TABLE, ItemStack.EMPTY);

        for (PedestalDirection direction : PedestalDirection.values()) {
            BlockPos pedestalPos = center.offset(direction.offsetX(), direction.offsetY(), direction.offsetZ());
            BlockEntity pedestalEntity = level.getBlockEntity(pedestalPos);
            if (pedestalEntity instanceof RitualPedestalBlockEntity pedestal) {
                pedestal.getInventory().setStackInSlot(RitualPedestalBlockEntity.SLOT_OFFERING, ItemStack.EMPTY);
                pedestal.setChanged();
            }
        }

        cubeMaker.setChanged();
    }
}
