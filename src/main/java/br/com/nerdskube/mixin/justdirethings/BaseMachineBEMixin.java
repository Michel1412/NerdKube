package br.com.nerdskube.mixin.justdirethings;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;

import br.com.nerdskube.integration.fakeplayer.FakePlayerOwnerHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Just Dire Things já salva {@code placedByUUID}; substitui o nome genérico pelo do jogador real
 * para mods de claim (FTB Chunks — Fake Players by Player ID).
 */
@Mixin(value = BaseMachineBE.class, remap = false)
public abstract class BaseMachineBEMixin {
    private static final String JDT_FAKE_NAME = "[JustDiresFakePlayer]";

    @Shadow
    public UUID placedByUUID;

    @Inject(method = "getPlacedByProfile", at = @At("RETURN"), cancellable = true, remap = false)
    private void nerdkube$resolveOwnerProfile(CallbackInfoReturnable<GameProfile> cir) {
        if (placedByUUID == null) {
            return;
        }

        GameProfile profile = cir.getReturnValue();
        if (profile == null || !JDT_FAKE_NAME.equals(profile.getName())) {
            return;
        }

        if (!(((BlockEntity) (Object) this).getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        GameProfile resolved = FakePlayerOwnerHelper.resolveProfile(serverLevel, placedByUUID, null);
        if (resolved != null) {
            cir.setReturnValue(resolved);
        }
    }
}
