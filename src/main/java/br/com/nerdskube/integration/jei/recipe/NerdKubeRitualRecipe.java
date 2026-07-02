package br.com.nerdskube.integration.jei.recipe;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record NerdKubeRitualRecipe(
        ResourceLocation id,
        ItemStack displayIcon,
        ItemStack centerCatalyst,
        ItemStack activationItem,
        RitualLayout layout,
        List<RitualRingSlot> ringSlots,
        List<RitualCardinalSlot> cardinalSlots,
        ItemStack output,
        Component title,
        List<Component> infoLines,
        ItemStack ringDecor,
        boolean mirrorCenterAtOutput) {
    public NerdKubeRitualRecipe(
            ResourceLocation id,
            ItemStack displayIcon,
            ItemStack centerCatalyst,
            ItemStack activationItem,
            RitualLayout layout,
            List<RitualRingSlot> ringSlots,
            List<RitualCardinalSlot> cardinalSlots,
            ItemStack output,
            Component title,
            List<Component> infoLines) {
        this(id, displayIcon, centerCatalyst, activationItem, layout, ringSlots, cardinalSlots, output, title, infoLines, ItemStack.EMPTY, true);
    }

    public NerdKubeRitualRecipe(
            ResourceLocation id,
            ItemStack displayIcon,
            ItemStack centerCatalyst,
            ItemStack activationItem,
            RitualLayout layout,
            List<RitualRingSlot> ringSlots,
            List<RitualCardinalSlot> cardinalSlots,
            ItemStack output,
            Component title,
            List<Component> infoLines,
            ItemStack ringDecor) {
        this(id, displayIcon, centerCatalyst, activationItem, layout, ringSlots, cardinalSlots, output, title, infoLines, ringDecor, true);
    }

    public NerdKubeRitualRecipe {
        displayIcon = displayIcon == null ? ItemStack.EMPTY : displayIcon.copy();
        centerCatalyst = centerCatalyst == null ? ItemStack.EMPTY : centerCatalyst.copy();
        activationItem = activationItem == null ? ItemStack.EMPTY : activationItem.copy();
        output = output == null ? ItemStack.EMPTY : output.copy();
        ringSlots = ringSlots == null ? List.of() : List.copyOf(ringSlots);
        cardinalSlots = cardinalSlots == null ? List.of() : List.copyOf(cardinalSlots);
        infoLines = infoLines == null ? List.of() : List.copyOf(infoLines);
        ringDecor = ringDecor == null ? ItemStack.EMPTY : ringDecor.copy();
    }
}
