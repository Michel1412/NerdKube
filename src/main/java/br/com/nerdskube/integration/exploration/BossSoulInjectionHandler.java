package br.com.nerdskube.integration.exploration;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * Transforma {@code fragmento_combate_stage1} em {@code componente_combate_stage1_completo}
 * ao matar o Wither ou o Ender Dragon com o fragmento no inventário.
 */
@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class BossSoulInjectionHandler {
    private static final ResourceLocation WITHER =
            ResourceLocation.fromNamespaceAndPath("minecraft", "wither");
    private static final ResourceLocation ENDER_DRAGON =
            ResourceLocation.fromNamespaceAndPath("minecraft", "ender_dragon");

    private BossSoulInjectionHandler() {}

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        LivingEntity victim = event.getEntity();
        ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(victim.getType());
        if (!WITHER.equals(entityId) && !ENDER_DRAGON.equals(entityId)) {
            return;
        }

        Player killer = null;
        if (event.getSource().getEntity() instanceof Player sourcePlayer) {
            killer = sourcePlayer;
        }
        if (killer == null && victim.getKillCredit() instanceof Player creditPlayer) {
            killer = creditPlayer;
        }
        if (killer == null) {
            return;
        }

        if (!transformFragment(killer, killer.getInventory())) {
            return;
        }

        killer.level().playSound(null, killer.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.7F, 0.9F);
        if (killer instanceof ServerPlayer serverPlayer) {
            serverPlayer.containerMenu.broadcastChanges();
        }
    }

    private static boolean transformFragment(Player player, Inventory inventory) {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.is(ModItems.FRAGMENTO_COMBATE_STAGE1.get())) {
                continue;
            }
            if (stack.getCount() == 1) {
                inventory.setItem(slot, new ItemStack(ModItems.COMPONENTE_COMBATE_STAGE1_COMPLETO.get()));
            } else {
                stack.shrink(1);
                ItemStack result = new ItemStack(ModItems.COMPONENTE_COMBATE_STAGE1_COMPLETO.get());
                if (!inventory.add(result)) {
                    player.drop(result, false);
                }
            }
            return true;
        }
        return false;
    }
}
