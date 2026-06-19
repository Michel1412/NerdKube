package br.com.nerdskube.ritual;

import br.com.nerdskube.block.ThemedPedestalBlock;
import br.com.nerdskube.block.entity.CubeMakerBlockEntity;
import br.com.nerdskube.block.entity.RitualPedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class RitualValidator {
    public enum Failure {
        MISSING_PEDESTAL,
        WRONG_PEDESTAL_TYPE,
        WRONG_PEDESTAL_ITEM,
        MISSING_CRAFTER
    }

    public record Result(boolean success, Failure failure, PedestalDirection failedDirection) {
        public static Result ok() {
            return new Result(true, null, null);
        }

        public static Result fail(Failure failure, PedestalDirection direction) {
            return new Result(false, failure, direction);
        }
    }

    private RitualValidator() {}

    public static Result validate(Level level, BlockPos center, CubeMakerBlockEntity cubeMaker) {
        ItemStack centerStack = cubeMaker.getInventory().getStackInSlot(CubeMakerBlockEntity.SLOT_CRAFTING_TABLE);
        if (centerStack.isEmpty() || !centerStack.is(Items.CRAFTER)) {
            return Result.fail(Failure.MISSING_CRAFTER, null);
        }

        for (PedestalDirection direction : PedestalDirection.values()) {
            BlockPos pedestalPos = center.offset(direction.offsetX(), direction.offsetY(), direction.offsetZ());
            BlockState blockState = level.getBlockState(pedestalPos);
            BlockEntity blockEntity = level.getBlockEntity(pedestalPos);

            if (!(blockEntity instanceof RitualPedestalBlockEntity pedestal)) {
                return Result.fail(Failure.MISSING_PEDESTAL, direction);
            }

            if (!(blockState.getBlock() instanceof ThemedPedestalBlock themedPedestal)) {
                return Result.fail(Failure.MISSING_PEDESTAL, direction);
            }

            if (themedPedestal.getDirection() != direction) {
                return Result.fail(Failure.WRONG_PEDESTAL_TYPE, direction);
            }

            if (!blockState.is(direction.expectedBlock())) {
                return Result.fail(Failure.WRONG_PEDESTAL_TYPE, direction);
            }

            ItemStack offering = pedestal.getInventory().getStackInSlot(RitualPedestalBlockEntity.SLOT_OFFERING);
            if (offering.getCount() != 1 || !offering.is(direction.requiredItem())) {
                return Result.fail(Failure.WRONG_PEDESTAL_ITEM, direction);
            }
        }

        return Result.ok();
    }
}
