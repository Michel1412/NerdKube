package br.com.nerdskube.registry;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.loot.EndCityPoeiraLootModifier;
import br.com.nerdskube.loot.ExplorationChestLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, NerdKube.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ExplorationChestLootModifier>>
            EXPLORATION_CHEST_FRAGMENT =
                    LOOT_MODIFIERS.register("exploration_chest_fragment", () -> ExplorationChestLootModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<EndCityPoeiraLootModifier>>
            END_CITY_POEIRA =
                    LOOT_MODIFIERS.register("end_city_poeira", () -> EndCityPoeiraLootModifier.CODEC);

    private ModLootModifiers() {}
}
