package br.com.nerdskube.integration.jei.recipe;

import net.minecraft.world.item.ItemStack;

public record RitualCardinalSlot(ItemStack pedestal, ItemStack offering) {
    public RitualCardinalSlot {
        pedestal = pedestal == null ? ItemStack.EMPTY : pedestal.copy();
        offering = offering == null ? ItemStack.EMPTY : offering.copy();
    }
}
