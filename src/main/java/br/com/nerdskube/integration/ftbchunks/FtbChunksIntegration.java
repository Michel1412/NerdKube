package br.com.nerdskube.integration.ftbchunks;

import br.com.nerdskube.NerdKube;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class FtbChunksIntegration {
    private static boolean registered;

    private FtbChunksIntegration() {}

    public static void init() {
        if (!ModList.get().isLoaded(FTBChunksAPI.MOD_ID)) {
            NerdKube.LOGGER.info("FTB Chunks ausente — trava de chunk para FakePlayers desativada.");
            return;
        }

        if (!registered) {
            ClaimedChunkEvent.AFTER_CLAIM.register((manager, chunk) -> FtbChunksClaimCache.invalidate(chunk.getPos()));
            ClaimedChunkEvent.AFTER_UNCLAIM.register((manager, chunk) -> FtbChunksClaimCache.invalidate(chunk.getPos()));
            registered = true;
        }

        NerdKube.LOGGER.info(
                "FTB Chunks integrado — automações bloqueadas em chunks claimed por outros jogadores.");
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        FtbChunksClaimCache.clear();
    }
}
