package br.com.nerdskube.component;

import br.com.nerdskube.NerdKube;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Consumer;

public final class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, NerdKube.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> CRAFTED_BY =
            DATA_COMPONENT_TYPES.register("crafted_by", () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build());

    private ModDataComponents() {}

    public static ItemStack withCraftedBy(ItemStack stack, String crafterName) {
        stack.set(CRAFTED_BY.get(), crafterName);
        stack.set(net.minecraft.core.component.DataComponents.LORE,
                new net.minecraft.world.item.component.ItemLore(List.of(
                        Component.literal("§bO nerd que fabricou foi " + crafterName))));
        return stack;
    }

    public static void appendCraftedByTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String crafter = stack.get(CRAFTED_BY.get());
        if (crafter != null && !crafter.isEmpty()) {
            tooltip.add(Component.literal("§bO nerd que fabricou foi " + crafter));
        }
    }
}
