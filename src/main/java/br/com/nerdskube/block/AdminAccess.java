package br.com.nerdskube.block;

import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public final class AdminAccess {
    private AdminAccess() {}

    public static boolean isAdmin(@Nullable Player player) {
        return player != null && player.hasPermissions(Commands.LEVEL_GAMEMASTERS);
    }
}
