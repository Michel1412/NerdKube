package br.com.nerdskube.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EternalCubeBlock extends Block {
    public static final MapCodec<EternalCubeBlock> CODEC = simpleCodec(EternalCubeBlock::new);

    public EternalCubeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (!AdminAccess.isAdmin(player)) {
            return 0.0F;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!AdminAccess.isAdmin(player)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("nerdkube.eternal_cube.admin_only"), true);
            }
            return state;
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}
