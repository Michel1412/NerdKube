package br.com.nerdskube.recipe;

import br.com.nerdskube.NerdKube;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class RecipeOverrideEvents {
    private RecipeOverrideEvents() {}

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new RecipeOverrideReloadListener(
                event.getServerResources().getRecipeManager(),
                event.getRegistryAccess()));
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        applyOnServer(event.getServer());
    }

    /** Garante overrides no servidor antes do sync de receitas ao cliente (JEI 19.x). */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        applyOnServer(event.getPlayerList().getServer());
    }

    private static void applyOnServer(MinecraftServer server) {
        RecipeOverrideHandler.apply(server.getRecipeManager(), server.registryAccess());
    }

    @EventBusSubscriber(modid = NerdKube.MOD_ID, value = Dist.CLIENT)
    public static final class Client {
        private Client() {}

        /** Aplica antes do JEI (NORMAL) observar {@link RecipesUpdatedEvent}. */
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
}
