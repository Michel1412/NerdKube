package br.com.nerdskube.integration.fakeplayer;

import br.com.nerdskube.attachment.ModAttachments;
import net.minecraft.world.entity.player.Player;

/**
 * Marca jogadores sintéticos de máquinas (Oritech FakeMachinePlayer, etc.).
 * Necessário porque o Enderic Laser assume o UUID/nome do dono e não é {@link net.neoforged.neoforge.common.util.FakePlayer}.
 */
public final class AutomationPlayerMark {
    private AutomationPlayerMark() {}

    public static void mark(Player player) {
        if (player != null) {
            player.setData(ModAttachments.AUTOMATION_PLAYER.get(), true);
        }
    }

    public static boolean isMarked(Player player) {
        return player != null && player.getData(ModAttachments.AUTOMATION_PLAYER.get());
    }
}
