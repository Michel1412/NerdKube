package br.com.nerdskube.integration.jei.recipe;

import net.minecraft.world.item.ItemStack;

public record RitualRingSlot(ItemStack decor, ItemStack item) {
    public RitualRingSlot {
        decor = decor == null ? ItemStack.EMPTY : decor.copy();
        item = item == null ? ItemStack.EMPTY : item.copy();
    }

    public static RitualRingSlot of(ItemStack item) {
        return new RitualRingSlot(ItemStack.EMPTY, item);
    }
}
