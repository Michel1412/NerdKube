package br.com.nerdskube.ritual;

import br.com.nerdskube.block.CubeMakerBlock;
import br.com.nerdskube.block.ThemedPedestalBlock;
import br.com.nerdskube.block.entity.RitualPedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class PedestalRitualStatus {
    public enum Kind {
        NO_CUBE_MAKER,
        WRONG_POSITION,
        MISSING_OFFERING,
        WRONG_OFFERING,
        READY
    }

    public record Result(Kind kind, PedestalDirection expectedDirection, PedestalDirection actualDirection) {
        public static Result of(Kind kind) {
            return new Result(kind, null, null);
        }

        public static Result wrongPosition(PedestalDirection expected, PedestalDirection actual) {
            return new Result(Kind.WRONG_POSITION, expected, actual);
        }
    }

    private PedestalRitualStatus() {}

    public static Result evaluate(Level level, BlockPos pedestalPos, ThemedPedestalBlock pedestalBlock) {
        PedestalDirection expected = pedestalBlock.getDirection();
        BlockPos expectedMakerPos = makerPosFor(pedestalPos, expected);

        if (isCubeMaker(level, expectedMakerPos)) {
            return evaluateOffering(level, pedestalPos, expected);
        }

        for (PedestalDirection direction : PedestalDirection.values()) {
            if (direction == expected) {
                continue;
            }
            BlockPos makerPos = makerPosFor(pedestalPos, direction);
            if (isCubeMaker(level, makerPos)) {
                return Result.wrongPosition(expected, direction);
            }
        }

        return Result.of(Kind.NO_CUBE_MAKER);
    }

    private static Result evaluateOffering(Level level, BlockPos pedestalPos, PedestalDirection expected) {
        BlockEntity blockEntity = level.getBlockEntity(pedestalPos);
        if (!(blockEntity instanceof RitualPedestalBlockEntity pedestal)) {
            return Result.of(Kind.MISSING_OFFERING);
        }

        ItemStack offering = pedestal.getInventory().getStackInSlot(RitualPedestalBlockEntity.SLOT_OFFERING);
        if (offering.isEmpty()) {
            return Result.of(Kind.MISSING_OFFERING);
        }
        if (offering.getCount() == 1 && offering.is(expected.requiredItem())) {
            return Result.of(Kind.READY);
        }
        return Result.of(Kind.WRONG_OFFERING);
    }

    private static BlockPos makerPosFor(BlockPos pedestalPos, PedestalDirection fromPedestalToMaker) {
        return pedestalPos.offset(-fromPedestalToMaker.offsetX(), -fromPedestalToMaker.offsetY(), -fromPedestalToMaker.offsetZ());
    }

    private static boolean isCubeMaker(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof CubeMakerBlock;
    }
}
