package br.com.nerdskube.integration.agriculture;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fallback quando a receita datapack do Botany Pots não germina o fragmento.
 * Atua apenas em vasos ultra com solo supremium e sem crop datapack válido.
 */
@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class BotanyPotGerminationHandler {
    private static final int GROW_TICKS = 12000;
    private static final Map<BlockPos, Integer> PROGRESS = new ConcurrentHashMap<>();

    private BotanyPotGerminationHandler() {}

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        long gameTime = serverLevel.getGameTime();
        if (gameTime % 20 == 0) {
            tickProgress(serverLevel);
        }
        if (gameTime % 200 == 0) {
            scanNearPlayers(serverLevel);
        }
    }

    private static void tickProgress(ServerLevel level) {
        PROGRESS.entrySet().removeIf(entry -> {
            BlockPos pos = entry.getKey();
            if (!level.isLoaded(pos) || !isUltraBotanyPot(level.getBlockState(pos))) {
                return true;
            }
            BlockEntity be = level.getBlockEntity(pos);
            if (!matchesGerminationSetup(be) || hasDatapackCrop(be)) {
                return true;
            }

            int ticks = entry.getValue() + 20;
            if (ticks >= GROW_TICKS) {
                Block.popResource(level, pos, new ItemStack(ModItems.RAIZES_DOCES.get()));
                setSeedItem(be, ItemStack.EMPTY);
                return true;
            }
            entry.setValue(ticks);
            return false;
        });
    }

    private static void scanNearPlayers(ServerLevel level) {
        for (ServerPlayer player : level.players()) {
            BlockPos center = player.blockPosition();
            BlockPos.betweenClosedStream(center.offset(-24, -8, -24), center.offset(24, 8, 24))
                    .forEach(pos -> {
                        if (!isUltraBotanyPot(level.getBlockState(pos))) {
                            return;
                        }
                        BlockEntity be = level.getBlockEntity(pos);
                        if (!matchesGerminationSetup(be) || hasDatapackCrop(be)) {
                            PROGRESS.remove(pos);
                            return;
                        }
                        PROGRESS.putIfAbsent(pos.immutable(), 0);
                    });
        }
    }

    private static boolean isUltraBotanyPot(BlockState state) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return "botanypotstiers".equals(id.getNamespace()) && id.getPath().contains("ultra");
    }

    private static boolean matchesGerminationSetup(BlockEntity be) {
        if (be == null || !be.getClass().getName().contains("BotanyPotBlockEntity")) {
            return false;
        }
        ItemStack seed = getSeedItem(be);
        ItemStack soil = getSoilItem(be);
        if (seed.isEmpty() || !seed.is(ModItems.RUNIC_CRYSTAL_CAKE.get())) {
            return false;
        }
        if (soil.isEmpty()) {
            return false;
        }
        ResourceLocation soilId = BuiltInRegistries.ITEM.getKey(soil.getItem());
        return "mysticalagriculture".equals(soilId.getNamespace())
                && soilId.getPath().contains("supremium_farmland");
    }

    private static boolean hasDatapackCrop(BlockEntity be) {
        try {
            Method cropMethod = be.getClass().getMethod("getOrInvalidateCrop");
            Object crop = cropMethod.invoke(be);
            return crop != null;
        } catch (ReflectiveOperationException ex) {
            return false;
        }
    }

    private static ItemStack getSeedItem(BlockEntity be) {
        return invokeItemStack(be, "getSeedItem");
    }

    private static ItemStack getSoilItem(BlockEntity be) {
        return invokeItemStack(be, "getSoilItem");
    }

    private static void setSeedItem(BlockEntity be, ItemStack stack) {
        try {
            Method method = be.getClass().getMethod("setSeed", ItemStack.class);
            method.invoke(be, stack);
        } catch (ReflectiveOperationException ignored) {
            // datapack crop é o caminho principal
        }
    }

    private static ItemStack invokeItemStack(BlockEntity be, String methodName) {
        try {
            Method method = be.getClass().getMethod(methodName);
            Object result = method.invoke(be);
            return result instanceof ItemStack stack ? stack : ItemStack.EMPTY;
        } catch (ReflectiveOperationException ex) {
            return ItemStack.EMPTY;
        }
    }
}
