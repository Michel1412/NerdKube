package br.com.nerdskube.ritual;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public final class RitualFinaleEffects {
    private RitualFinaleEffects() {}

    public static void play(Level level, BlockPos center) {
        RandomSource random = level.getRandom();

        for (PedestalDirection direction : PedestalDirection.values()) {
            BlockPos targetPos = center.offset(direction.offsetX(), direction.offsetY(), direction.offsetZ());

            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, targetPos, SoundEvents.WITHER_BREAK_BLOCK, SoundSource.BLOCKS, 1.0F, 1.0F);

            ItemStack fireworkStack = createRandomFirework(random);
            FireworkRocketEntity fireworkEntity = new FireworkRocketEntity(
                    level,
                    targetPos.getX() + 0.5,
                    targetPos.getY() + 1.0,
                    targetPos.getZ() + 0.5,
                    fireworkStack);
            fireworkEntity.setDeltaMovement(
                    (random.nextDouble() - 0.5) * 0.15,
                    0.4 + random.nextDouble() * 0.2,
                    (random.nextDouble() - 0.5) * 0.15);
            level.addFreshEntity(fireworkEntity);
        }
    }

    private static ItemStack createRandomFirework(RandomSource random) {
        FireworkExplosion.Shape shape;
        IntArrayList colors;
        boolean hasTrail = false;
        boolean hasTwinkle = false;

        switch (random.nextInt(4)) {
            case 0 -> {
                colors = IntArrayList.of(11743532, 3887386, 2437522, 14602026);
                shape = FireworkExplosion.Shape.LARGE_BALL;
            }
            case 1 -> {
                colors = IntArrayList.of(15435844, 12801229, 43123);
                shape = FireworkExplosion.Shape.STAR;
            }
            case 2 -> {
                colors = IntArrayList.of(5025616, 1973019, 15790320);
                shape = FireworkExplosion.Shape.SMALL_BALL;
                hasTwinkle = true;
            }
            default -> {
                colors = IntArrayList.of(6719955, 14188952, 16777215);
                shape = FireworkExplosion.Shape.BURST;
                hasTrail = true;
            }
        }

        FireworkExplosion explosion = new FireworkExplosion(
                shape,
                colors,
                new IntArrayList(),
                hasTrail,
                hasTwinkle);

        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
        stack.set(DataComponents.FIREWORKS, new Fireworks(1 + random.nextInt(2), List.of(explosion)));
        return stack;
    }
}
