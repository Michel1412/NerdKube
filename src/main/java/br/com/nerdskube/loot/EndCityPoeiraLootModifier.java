package br.com.nerdskube.loot;

import br.com.nerdskube.config.NerdKubeServerConfigAccess;
import br.com.nerdskube.registry.ModItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class EndCityPoeiraLootModifier extends LootModifier {
    private static final ResourceLocation END_CITY_CHEST =
            ResourceLocation.fromNamespaceAndPath("minecraft", "chests/end_city");

    public static final MapCodec<EndCityPoeiraLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).apply(inst, EndCityPoeiraLootModifier::new));

    public EndCityPoeiraLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation tableId = context.getQueriedLootTableId();
        if (!END_CITY_CHEST.equals(tableId)) {
            return generatedLoot;
        }

        if (context.getRandom().nextFloat() >= NerdKubeServerConfigAccess.transmutationSuccessChance()) {
            return generatedLoot;
        }

        generatedLoot.add(new ItemStack(ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get()));
        return generatedLoot;
    }
}
