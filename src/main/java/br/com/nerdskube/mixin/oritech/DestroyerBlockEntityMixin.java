package br.com.nerdskube.mixin.oritech;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.mojang.authlib.GameProfile;

import br.com.nerdskube.integration.fakeplayer.AutomationPlayerMark;
import br.com.nerdskube.integration.fakeplayer.FakePlayerOwnerAccess;
import br.com.nerdskube.integration.fakeplayer.FakePlayerOwnerHelper;
import br.com.nerdskube.integration.fakeplayer.FakePlayerProgressionGuard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import rearth.oritech.block.entity.interaction.DestroyerBlockEntity;

@Mixin(DestroyerBlockEntity.class)
public abstract class DestroyerBlockEntityMixin implements FakePlayerOwnerAccess {
    @Shadow
    private ServerPlayer destroyerPlayerEntity;

    @Shadow
    private Player getDestroyerPlayerEntity() {
        throw new AssertionError();
    }

    @Unique
    private static final String NERDKUBE_OWNER_UUID_KEY = "nerdkube_owner_uuid";

    @Unique
    private static final String NERDKUBE_OWNER_NAME_KEY = "nerdkube_owner_name";

    @Unique
    private UUID nerdkube$ownerUuid;

    @Unique
    private String nerdkube$ownerName;

    @Override
    public UUID nerdkube$getOwnerUuid() {
        return nerdkube$ownerUuid;
    }

    @Override
    public String nerdkube$getOwnerName() {
        return nerdkube$ownerName;
    }

    @Override
    public void nerdkube$setOwner(UUID uuid, String name) {
        nerdkube$ownerUuid = uuid;
        nerdkube$ownerName = name;
        destroyerPlayerEntity = null;
        ((BlockEntity) (Object) this).setChanged();
    }

    @Inject(method = "saveAdditional", at = @At("RETURN"))
    private void nerdkube$saveOwner(CompoundTag tag, HolderLookup.Provider provider, CallbackInfo ci) {
        if (nerdkube$ownerUuid != null) {
            tag.putUUID(NERDKUBE_OWNER_UUID_KEY, nerdkube$ownerUuid);
        }
        if (nerdkube$ownerName != null && !nerdkube$ownerName.isBlank()) {
            tag.putString(NERDKUBE_OWNER_NAME_KEY, nerdkube$ownerName);
        }
    }

    @Inject(method = "loadAdditional", at = @At("RETURN"))
    private void nerdkube$loadOwner(CompoundTag tag, HolderLookup.Provider provider, CallbackInfo ci) {
        if (tag.hasUUID(NERDKUBE_OWNER_UUID_KEY)) {
            nerdkube$ownerUuid = tag.getUUID(NERDKUBE_OWNER_UUID_KEY);
        }
        if (tag.contains(NERDKUBE_OWNER_NAME_KEY)) {
            nerdkube$ownerName = tag.getString(NERDKUBE_OWNER_NAME_KEY);
        }
        destroyerPlayerEntity = null;
    }

    @ModifyArgs(
            method = "getDestroyerPlayerEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/authlib/GameProfile;<init>(Ljava/util/UUID;Ljava/lang/String;)V"))
    private void nerdkube$useOwnerProfile(Args args) {
        if (nerdkube$ownerUuid == null) {
            return;
        }

        Level level = ((BlockEntity) (Object) this).getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            args.set(0, nerdkube$ownerUuid);
            if (nerdkube$ownerName != null) {
                args.set(1, nerdkube$ownerName);
            }
            return;
        }

        GameProfile profile = FakePlayerOwnerHelper.resolveProfile(serverLevel, nerdkube$ownerUuid, nerdkube$ownerName);
        if (profile != null) {
            args.set(0, profile.getId());
            args.set(1, profile.getName());
        }
    }

    @Inject(method = "getDestroyerPlayerEntity", at = @At("RETURN"))
    private void nerdkube$markDestroyerAutomationPlayer(CallbackInfoReturnable<Player> cir) {
        AutomationPlayerMark.mark(cir.getReturnValue());
    }

    @Inject(method = "finishBlockWork", at = @At("HEAD"), cancellable = true)
    private void nerdkube$blockProgressionHarvest(BlockPos pos, CallbackInfo ci) {
        Level level = ((BlockEntity) (Object) this).getLevel();
        if (level == null) {
            return;
        }
        BlockState state = level.getBlockState(pos);
        if (FakePlayerProgressionGuard.isProtectedInteractionBlock(state.getBlock())) {
            ci.cancel();
        }
    }

    @Inject(
            method = "finishBlockWork",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z",
                    shift = At.Shift.BEFORE),
            cancellable = true)
    private void nerdkube$respectProtectionMods(BlockPos pos, CallbackInfo ci) {
        Level level = ((BlockEntity) (Object) this).getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        BlockState state = serverLevel.getBlockState(pos);
        Player player = getDestroyerPlayerEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(serverLevel, pos, state, serverPlayer);
        NeoForge.EVENT_BUS.post(breakEvent);
        if (breakEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
