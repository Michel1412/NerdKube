package br.com.nerdskube.block;

import br.com.nerdskube.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AgriCoreCropBlock extends CropBlock {
    public static final MapCodec<AgriCoreCropBlock> CODEC = simpleCodec(AgriCoreCropBlock::new);

    public AgriCoreCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }

    @Override
    public int getMaxAge() {
        return 2;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.RUNIC_CRYSTAL_CAKE.get();
    }

    @Override
    public ItemStack getCloneItemStack(net.minecraft.world.level.LevelReader level, net.minecraft.core.BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.RUNIC_CRYSTAL_CAKE.get());
    }
}
