package br.com.nerdskube.integration.jei.recipe;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ProgressionCraftRecipe(List<ItemStack> grid, ItemStack output) {
    public static final int GRID_SIZE = 9;

    public ProgressionCraftRecipe {
        if (grid.size() != GRID_SIZE) {
            throw new IllegalArgumentException("Progression craft grid must have 9 slots");
        }
    }
}
