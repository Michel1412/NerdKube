package br.com.nerdskube.integration.ftbchunks;

import br.com.nerdskube.config.NerdKubeServerConfigAccess;
import br.com.nerdskube.integration.fakeplayer.FakePlayerProgressionGuard;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.ClaimedChunkManager;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.api.Protection;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.fml.ModList;

/**
 * Consulta a API do FTB Chunks para bloquear automações em chunks de terceiros.
 */
public final class FtbChunksFakePlayerGuard {
    private FtbChunksFakePlayerGuard() {}

    public static boolean shouldBlockInteraction(
            ServerLevel level,
            Player player,
            BlockPos pos,
            Protection protection) {
        if (!NerdKubeServerConfigAccess.fakePlayerChunkLockEnabled()) {
            return false;
        }
        if (!ModList.get().isLoaded(FTBChunksAPI.MOD_ID)) {
            return false;
        }
        if (!FakePlayerProgressionGuard.isAutomationPlayer(player)) {
            return false;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }

        var api = FTBChunksAPI.api();
        if (!api.isManagerLoaded()) {
            return false;
        }

        ClaimedChunkManager manager = api.getManager();
        ChunkPos chunkPos = new ChunkPos(pos);
        ClaimedChunk claimed = resolveClaimedChunk(level, manager, chunkPos);
        if (claimed == null) {
            return false;
        }

        return manager.shouldPreventInteraction(serverPlayer, InteractionHand.MAIN_HAND, pos, protection, null);
    }

    private static ClaimedChunk resolveClaimedChunk(
            ServerLevel level,
            ClaimedChunkManager manager,
            ChunkPos chunkPos) {
        var cached = FtbChunksClaimCache.getClaimedChunk(level, chunkPos);
        if (cached.isPresent()) {
            return cached.get();
        }

        ChunkDimPos dimPos = new ChunkDimPos(level.dimension(), chunkPos);
        ClaimedChunk chunk = manager.getChunk(dimPos);
        FtbChunksClaimCache.put(level, chunkPos, chunk);
        return chunk;
    }
}
