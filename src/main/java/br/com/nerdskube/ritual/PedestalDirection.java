package br.com.nerdskube.ritual;

import br.com.nerdskube.block.ThemedPedestalBlock;
import br.com.nerdskube.registry.ModBlocks;
import br.com.nerdskube.registry.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public enum PedestalDirection {
    NORTH(0, 0, -3, ModItems.NUCLEO_DE_MATERIA, ModBlocks.PEDESTAL_TECH),
    SOUTH(0, 0, 3, ModItems.CORACAO_ARCANO, ModBlocks.PEDESTAL_MAGIA),
    EAST(3, 0, 0, ModItems.RELIQUIA_DESBRAVADOR, ModBlocks.PEDESTAL_EXPLORACAO),
    WEST(-3, 0, 0, ModItems.SEMENTE_CRIACAO, ModBlocks.PEDESTAL_AGRICULTURA);

    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;
    private final DeferredItem<Item> requiredItem;
    private final DeferredBlock<ThemedPedestalBlock> expectedBlock;

    PedestalDirection(
            int offsetX,
            int offsetY,
            int offsetZ,
            DeferredItem<Item> requiredItem,
            DeferredBlock<ThemedPedestalBlock> expectedBlock) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.requiredItem = requiredItem;
        this.expectedBlock = expectedBlock;
    }

    public int offsetX() {
        return offsetX;
    }

    public int offsetY() {
        return offsetY;
    }

    public int offsetZ() {
        return offsetZ;
    }

    public Item requiredItem() {
        return requiredItem.get();
    }

    public Block expectedBlock() {
        return expectedBlock.get();
    }

    public String translationKey() {
        return "nerdkube.ritual.direction." + name().toLowerCase();
    }
}
