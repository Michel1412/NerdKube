package br.com.nerdskube.block;

import br.com.nerdskube.block.entity.NerdCubeBlockEntity;
import br.com.nerdskube.component.ModDataComponents;
import br.com.nerdskube.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class NerdCubeBlock extends BaseEntityBlock {
    public static final MapCodec<NerdCubeBlock> CODEC = simpleCodec(NerdCubeBlock::new);

    public NerdCubeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NerdCubeBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof NerdCubeBlockEntity nerdCube) {
                String crafter = stack.get(ModDataComponents.CRAFTED_BY.get());
                if (crafter != null) {
                    nerdCube.setCraftedBy(crafter);
                }
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof NerdCubeBlockEntity nerdCube && !level.isClientSide && !player.isCreative()) {
            ItemStack stack = new ItemStack(ModBlocks.NERD_CUBE.get());
            String crafter = nerdCube.getCraftedBy();
            if (!crafter.isEmpty()) {
                ModDataComponents.withCraftedBy(stack, crafter);
            }
            popResource(level, pos, stack);
            level.removeBlock(pos, false);
            return state;
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Collections.emptyList();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
