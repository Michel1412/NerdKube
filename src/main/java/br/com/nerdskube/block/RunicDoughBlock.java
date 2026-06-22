package br.com.nerdskube.block;

import br.com.nerdskube.integration.fakeplayer.FakePlayerProgressionGuard;
import br.com.nerdskube.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RunicDoughBlock extends Block {
    public static final MapCodec<RunicDoughBlock> CODEC = simpleCodec(RunicDoughBlock::new);
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    private static final ResourceLocation MASTER_INFUSION_CRYSTAL =
            ResourceLocation.fromNamespaceAndPath("mysticalagriculture", "master_infusion_crystal");

    public RunicDoughBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.SUCCESS;
        }
        if (FakePlayerProgressionGuard.isAutomationPlayer(player)) {
            return ItemInteractionResult.FAIL;
        }
        if (!stack.is(BuiltInRegistries.ITEM.get(MASTER_INFUSION_CRYSTAL))) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        level.setBlock(pos, ModBlocks.BOLO_MISTICO_COLOCAVEL.get().defaultBlockState(), Block.UPDATE_ALL);
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.8F, 1.2F);
        level.levelEvent(2001, pos, Block.getId(ModBlocks.BOLO_MISTICO_COLOCAVEL.get().defaultBlockState()));
        return ItemInteractionResult.SUCCESS;
    }
}
