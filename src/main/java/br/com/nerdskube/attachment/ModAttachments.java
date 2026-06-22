package br.com.nerdskube.attachment;

import br.com.nerdskube.NerdKube;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, NerdKube.MOD_ID);

    public static final Supplier<AttachmentType<PlayerRitualData>> PLAYER_RITUAL_DATA = ATTACHMENT_TYPES.register(
            "player_ritual_data",
            () -> AttachmentType.builder(() -> PlayerRitualData.DEFAULT)
                    .serialize(PlayerRitualData.CODEC)
                    .copyOnDeath()
                    .build());

    /** Runtime-only: jogador sintético de laser/destroyer/clicker (não persiste). */
    public static final Supplier<AttachmentType<Boolean>> AUTOMATION_PLAYER = ATTACHMENT_TYPES.register(
            "automation_player",
            () -> AttachmentType.builder(() -> false).build());

    private ModAttachments() {}

    public static boolean hasCraftedTrophy(Player player) {
        return player.getData(PLAYER_RITUAL_DATA.get()).hasCraftedTrophy();
    }

    public static void setCraftedTrophy(Player player) {
        player.setData(PLAYER_RITUAL_DATA.get(), player.getData(PLAYER_RITUAL_DATA.get()).withCrafted());
    }

    public static void resetCraftedTrophy(Player player) {
        player.setData(PLAYER_RITUAL_DATA.get(), PlayerRitualData.DEFAULT);
    }
}
