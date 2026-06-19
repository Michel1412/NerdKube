package br.com.nerdskube.integration.jei.recipe;

import net.minecraft.world.item.ItemStack;

public record EndgameRitualRecipe(
        ItemStack crafter,
        ItemStack northPedestal,
        ItemStack northOffering,
        ItemStack southPedestal,
        ItemStack southOffering,
        ItemStack eastPedestal,
        ItemStack eastOffering,
        ItemStack westPedestal,
        ItemStack westOffering,
        ItemStack output) {}
