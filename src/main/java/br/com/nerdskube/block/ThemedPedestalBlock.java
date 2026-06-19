package br.com.nerdskube.block;

import br.com.nerdskube.block.entity.RitualPedestalBlockEntity;
import br.com.nerdskube.ritual.PedestalDirection;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ThemedPedestalBlock extends BaseEntityBlock {
    private final PedestalDirection direction;

    public ThemedPedestalBlock(PedestalDirection direction, Properties properties) {
        super(properties);
        this.direction = direction;
    }

    public PedestalDirection getDirection() {
        return direction;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return MapCodec.unit(this);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RitualPedestalBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof RitualPedestalBlockEntity pedestal)) {
            return InteractionResult.PASS;
        }

        ItemStack handStack = player.getItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND);
        ItemStack pedestalStack = pedestal.getInventory().getStackInSlot(RitualPedestalBlockEntity.SLOT_OFFERING);

        if (pedestalStack.isEmpty() && !handStack.isEmpty()) {
            if (!handStack.is(direction.requiredItem())) {
                player.displayClientMessage(Component.translatable("nerdkube.ritual.wrong_offering"), true);
                return InteractionResult.FAIL;
            }
            ItemStack insert = handStack.copyWithCount(1);
            pedestal.getInventory().setStackInSlot(RitualPedestalBlockEntity.SLOT_OFFERING, insert);
            handStack.shrink(1);
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            return InteractionResult.CONSUME;
        }

        if (!pedestalStack.isEmpty()) {
            if (!player.addItem(pedestalStack.copy())) {
                Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), pedestalStack.copy());
            }
            pedestal.getInventory().setStackInSlot(RitualPedestalBlockEntity.SLOT_OFFERING, ItemStack.EMPTY);
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RitualPedestalBlockEntity pedestal) {
                ItemStack stack = pedestal.getInventory().getStackInSlot(RitualPedestalBlockEntity.SLOT_OFFERING);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
