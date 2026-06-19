package br.com.nerdskube.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SculkGearCrystalBlock extends DirectionalBlock {
    public static final MapCodec<SculkGearCrystalBlock> CODEC = simpleCodec(SculkGearCrystalBlock::new);

    private static final VoxelShape UP_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D);
    private static final VoxelShape DOWN_SHAPE = Block.box(4.0D, 2.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape NORTH_SHAPE = Block.box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 14.0D);
    private static final VoxelShape SOUTH_SHAPE = Block.box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 14.0D);
    private static final VoxelShape EAST_SHAPE = Block.box(2.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D);
    private static final VoxelShape WEST_SHAPE = Block.box(2.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D);

    public SculkGearCrystalBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeFor(state.getValue(FACING));
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    private static VoxelShape shapeFor(Direction facing) {
        return switch (facing) {
            case DOWN -> DOWN_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> UP_SHAPE;
        };
    }
}
