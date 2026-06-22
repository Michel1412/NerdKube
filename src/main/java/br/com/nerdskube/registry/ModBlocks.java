package br.com.nerdskube.registry;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.block.AgriCoreCropBlock;
import br.com.nerdskube.block.CubeMakerBlock;
import br.com.nerdskube.block.EternalCubeBlock;
import br.com.nerdskube.block.MysticCakeBlock;
import br.com.nerdskube.block.NerdCubeBlock;
import br.com.nerdskube.block.RunicDoughBlock;
import br.com.nerdskube.block.SculkGearCrystalBlock;
import br.com.nerdskube.block.ThemedPedestalBlock;
import br.com.nerdskube.ritual.PedestalDirection;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NerdKube.MOD_ID);

    private static BlockBehaviour.Properties pedestalProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(2.0F, 6.0F)
                .sound(SoundType.STONE)
                .noOcclusion();
    }

    public static final DeferredBlock<CubeMakerBlock> CUBE_MAKER = BLOCKS.register("cube_maker",
            () -> new CubeMakerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.5F, 6.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()));

    public static final DeferredBlock<ThemedPedestalBlock> PEDESTAL_TECH = BLOCKS.register("pedestal_tech",
            () -> new ThemedPedestalBlock(PedestalDirection.NORTH, pedestalProperties()));

    public static final DeferredBlock<ThemedPedestalBlock> PEDESTAL_MAGIA = BLOCKS.register("pedestal_magia",
            () -> new ThemedPedestalBlock(PedestalDirection.SOUTH, pedestalProperties()));

    public static final DeferredBlock<ThemedPedestalBlock> PEDESTAL_EXPLORACAO = BLOCKS.register("pedestal_exploracao",
            () -> new ThemedPedestalBlock(PedestalDirection.EAST, pedestalProperties()));

    public static final DeferredBlock<ThemedPedestalBlock> PEDESTAL_AGRICULTURA = BLOCKS.register("pedestal_agricultura",
            () -> new ThemedPedestalBlock(PedestalDirection.WEST, pedestalProperties()));

    public static final DeferredBlock<Block> PEDESTAL_BRUTO = BLOCKS.register("pedestal_bruto",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.STONE)));

    private static BlockBehaviour.Properties thinBakeryLayer() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.SAND)
                .strength(0.5F)
                .sound(SoundType.WOOL)
                .noOcclusion();
    }

    public static final DeferredBlock<RunicDoughBlock> MASSA_RUNICA_CRUA = BLOCKS.register("massa_runica_crua",
            () -> new RunicDoughBlock(thinBakeryLayer().noCollission()));

    public static final DeferredBlock<MysticCakeBlock> BOLO_MISTICO_COLOCAVEL = BLOCKS.register("bolo_mistico_colocavel",
            () -> new MysticCakeBlock(thinBakeryLayer().lightLevel(state -> 6).noCollission()));

    public static final DeferredBlock<AgriCoreCropBlock> AGRI_CORE_CROP = BLOCKS.register("agri_core_crop",
            () -> new AgriCoreCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .strength(0.0F)
                    .sound(SoundType.CROP)));

    public static final DeferredBlock<NerdCubeBlock> NERD_CUBE = BLOCKS.register("nerd_cube",
            () -> new NerdCubeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(-1.0F, 3600000.0F)
                    .lightLevel(state -> 15)
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<SculkGearCrystalBlock> SCULK_GEAR_CRYSTAL = BLOCKS.register("sculk_gear_crystal",
            () -> new SculkGearCrystalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(1.5F, 3.0F)
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .lightLevel(state -> 8)
                    .noOcclusion()));

    public static final DeferredBlock<EternalCubeBlock> ETERNAL_CUBE = BLOCKS.register("eternal_cube",
            () -> new EternalCubeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(-1.0F, 3600000.0F)
                    .sound(SoundType.AMETHYST)
                    .lightLevel(state -> 15)
                    .noOcclusion()));

    private ModBlocks() {}
}
