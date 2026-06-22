package br.com.nerdskube.recipe;

import br.com.nerdskube.NerdKube;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;

/**
 * Aplica overrides no cliente antes do JEI observar {@link RecipesUpdatedEvent}.
 * No servidor, {@code RecipeManagerMixin} aplica após {@code RecipeManager#apply}.
 */
@EventBusSubscriber(modid = NerdKube.MOD_ID, value = Dist.CLIENT)
public final class RecipeOverrideEvents {
    private RecipeOverrideEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRecipesUpdatedApply(RecipesUpdatedEvent event) {
        HolderLookup.Provider registries = resolveClientRegistries();
        if (registries != null) {
            RecipeOverrideHandler.apply(event.getRecipeManager(), registries);
        }
    }

    private static HolderLookup.Provider resolveClientRegistries() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() != null) {
            return minecraft.getConnection().registryAccess();
        }
        if (minecraft.level != null) {
            return minecraft.level.registryAccess();
        }
        return null;
    }
}
