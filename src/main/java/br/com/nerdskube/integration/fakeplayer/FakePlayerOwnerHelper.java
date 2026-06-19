package br.com.nerdskube.integration.fakeplayer;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ServerLevel;

public final class FakePlayerOwnerHelper {
    private static final String FALLBACK_NAME = "nerdkube_automation";

    private FakePlayerOwnerHelper() {}

    public static GameProfile resolveProfile(ServerLevel level, UUID ownerUuid, String ownerName) {
        if (ownerUuid == null) {
            return null;
        }

        String name = ownerName;
        if (name == null || name.isBlank()) {
            name = level.getServer()
                    .getProfileCache()
                    .get(ownerUuid)
                    .map(GameProfile::getName)
                    .orElse(FALLBACK_NAME);
        }
        return new GameProfile(ownerUuid, name);
    }
}
