package br.com.nerdskube.registry;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.block.entity.CubeMakerBlockEntity;
import br.com.nerdskube.block.entity.NerdCubeBlockEntity;
import br.com.nerdskube.block.entity.RitualPedestalBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NerdKube.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CubeMakerBlockEntity>> CUBE_MAKER =
            BLOCK_ENTITIES.register("cube_maker", () -> BlockEntityType.Builder
                    .of(CubeMakerBlockEntity::new, ModBlocks.CUBE_MAKER.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RitualPedestalBlockEntity>> RITUAL_PEDESTAL =
            BLOCK_ENTITIES.register("ritual_pedestal", () -> BlockEntityType.Builder
                    .of(RitualPedestalBlockEntity::new,
                            ModBlocks.PEDESTAL_TECH.get(),
                            ModBlocks.PEDESTAL_MAGIA.get(),
                            ModBlocks.PEDESTAL_EXPLORACAO.get(),
                            ModBlocks.PEDESTAL_AGRICULTURA.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NerdCubeBlockEntity>> NERD_CUBE =
            BLOCK_ENTITIES.register("nerd_cube", () -> BlockEntityType.Builder
                    .of(NerdCubeBlockEntity::new, ModBlocks.NERD_CUBE.get())
                    .build(null));

    private ModBlockEntities() {}
}
