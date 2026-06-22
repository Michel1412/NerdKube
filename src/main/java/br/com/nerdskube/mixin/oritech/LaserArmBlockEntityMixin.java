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
import rearth.oritech.block.entity.interaction.LaserArmBlockEntity;

/**
 * Associa o Enderic Laser ao jogador que o colocou (modelo Mekanism Digital Miner).
 * Quebras usam {@link LaserArmBlockEntity#getLaserPlayerEntity()} com UUID/nome do dono;
 * mods de proteção (FTB Chunks, OPC, etc.) decidem via eventos — sem API direta do FTB.
 */
@Mixin(LaserArmBlockEntity.class)
public abstract class LaserArmBlockEntityMixin implements FakePlayerOwnerAccess {
    @Shadow
    public abstract Player getLaserPlayerEntity();

    @Shadow
    private Player laserPlayerEntity;

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
        laserPlayerEntity = null;
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
        } else if (tag.hasUUID("nerdkube_placer_uuid")) {
            nerdkube$ownerUuid = tag.getUUID("nerdkube_placer_uuid");
        }
        if (tag.contains(NERDKUBE_OWNER_NAME_KEY)) {
            nerdkube$ownerName = tag.getString(NERDKUBE_OWNER_NAME_KEY);
        }
        laserPlayerEntity = null;
    }

    /**
     * Substitui o GameProfile aleatório do Oritech pelo dono da máquina.
     */
    @ModifyArgs(
            method = "getLaserPlayerEntity",
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

    @Inject(method = "getLaserPlayerEntity", at = @At("RETURN"))
    private void nerdkube$markLaserAutomationPlayer(CallbackInfoReturnable<Player> cir) {
        AutomationPlayerMark.mark(cir.getReturnValue());
    }

    /**
     * O laser não deve quebrar blocos de progressão NerdKube nem mesas de ritual externas.
     * Independente do UUID do dono — o mixin de ownership existe só para claims (FTB Chunks).
     */
    @Inject(method = "finishBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void nerdkube$blockProgressionHarvest(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (FakePlayerProgressionGuard.isProtectedInteractionBlock(state.getBlock())) {
            ci.cancel();
        }
    }

    /**
     * Deixa mods de claim cancelarem a quebra via {@link BlockEvent.BreakEvent} usando o FakePlayer do dono.
     */
    @Inject(
            method = "finishBlockBreaking",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z",
                    shift = At.Shift.BEFORE),
            cancellable = true)
    private void nerdkube$respectProtectionMods(BlockPos pos, BlockState state, CallbackInfo ci) {
        Level level = ((BlockEntity) (Object) this).getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        Player player = getLaserPlayerEntity();
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
