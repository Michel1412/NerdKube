package br.com.nerdskube.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Aplica overrides após o {@link RecipeManager} concluir o reload.
 * O estágio extra no {@code gameExecutor} evita corrida com {@code RecipeManager#apply}.
 */
public final class RecipeOverrideReloadListener implements PreparableReloadListener {
    private final RecipeManager recipeManager;
    private final HolderLookup.Provider registries;

    public RecipeOverrideReloadListener(RecipeManager recipeManager, HolderLookup.Provider registries) {
        this.recipeManager = recipeManager;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<Void> reload(
            PreparationBarrier barrier,
            ResourceManager resourceManager,
            ProfilerFiller preparationsProfiler,
            ProfilerFiller reloadProfiler,
            Executor backgroundExecutor,
            Executor gameExecutor) {
        return barrier.wait(CompletableFuture.completedFuture(null))
                .thenRunAsync(() -> {}, gameExecutor)
                .thenRunAsync(() -> RecipeOverrideHandler.apply(recipeManager, registries), gameExecutor);
    }
}
