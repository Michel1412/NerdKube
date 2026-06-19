package br.com.nerdskube.mixin.recipe;

import br.com.nerdskube.recipe.RecipeOverrideHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientRecipeSyncMixin {
    @Inject(method = "handleUpdateRecipes", at = @At("RETURN"))
    private void nerdkube$applyOverridesAfterFullSync(ClientboundUpdateRecipesPacket packet, CallbackInfo callbackInfo) {
        applyClientOverrides();
    }

    @Inject(method = "handleAddOrRemoveRecipes", at = @At("RETURN"))
    private void nerdkube$applyOverridesAfterDeltaSync(ClientboundRecipePacket packet, CallbackInfo callbackInfo) {
        applyClientOverrides();
    }

    private static void applyClientOverrides() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() == null) {
            return;
        }
        RecipeOverrideHandler.apply(
                minecraft.getConnection().getRecipeManager(),
                minecraft.getConnection().registryAccess());
    }
}
