package br.com.nerdskube.integration.jei;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

final class JeiItemStacks {
    private JeiItemStacks() {}

    static ItemStack of(String id) {
        return of(id, 1);
    }

    static ItemStack of(String id, int count) {
        ResourceLocation key = ResourceLocation.parse(id);
        return BuiltInRegistries.ITEM.getOptional(key)
                .map(item -> new ItemStack(item, count))
                .orElse(ItemStack.EMPTY);
    }
}
