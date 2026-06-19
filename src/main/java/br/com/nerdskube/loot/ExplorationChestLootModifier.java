package br.com.nerdskube.loot;

import br.com.nerdskube.registry.ModItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class ExplorationChestLootModifier extends LootModifier {
    private static final float CHANCE = 0.015F;
    private static final TagKey<Item> ARTIFACTS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("artifacts", "artifacts"));

    public static final MapCodec<ExplorationChestLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).apply(inst, ExplorationChestLootModifier::new));

    public ExplorationChestLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation tableId = context.getQueriedLootTableId();
        if (tableId == null || !isExplorationChestTable(tableId)) {
            return generatedLoot;
        }

        Player player = resolvePlayer(context);
        if (player == null || countArtifacts(player) < 3) {
            return generatedLoot;
        }

        if (context.getRandom().nextFloat() >= CHANCE) {
            return generatedLoot;
        }

        generatedLoot.add(new ItemStack(ModItems.FRAGMENTO_COMBATE_STAGE2.get()));
        return generatedLoot;
    }

    private static boolean isExplorationChestTable(ResourceLocation tableId) {
        String id = tableId.toString();
        return id.contains("lootr")
                || id.contains("yungsapi")
                || id.contains("explorify")
                || id.contains("better_dungeons");
    }

    private static Player resolvePlayer(LootContext context) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            return player;
        }
        if (context.getParamOrNull(LootContextParams.DIRECT_ATTACKING_ENTITY) instanceof Player player) {
            return player;
        }
        if (context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER) instanceof Player player) {
            return player;
        }
        return null;
    }

    private static int countArtifacts(Player player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.is(ARTIFACTS)) {
                count += stack.getCount();
            }
        }
        return count;
    }
}
