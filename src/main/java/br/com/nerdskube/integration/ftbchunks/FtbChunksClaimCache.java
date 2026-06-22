package br.com.nerdskube.integration.ftbchunks;

import br.com.nerdskube.NerdKube;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache leve de chunks claimed para evitar consultas repetidas à API do FTB Chunks.
 * Invalidado em claim/unclaim via {@link FtbChunksIntegration}.
 */
public final class FtbChunksClaimCache {
    private static final long TTL_MS = 30_000L;
    private static final ConcurrentHashMap<Long, CacheEntry> ENTRIES = new ConcurrentHashMap<>();

    private FtbChunksClaimCache() {}

    public static Optional<ClaimedChunk> getClaimedChunk(ServerLevel level, ChunkPos chunkPos) {
        long key = pack(level, chunkPos);
        CacheEntry cached = ENTRIES.get(key);
        if (cached != null && !cached.isExpired()) {
            return Optional.ofNullable(cached.chunk());
        }
        return Optional.empty();
    }

    public static void put(ServerLevel level, ChunkPos chunkPos, ClaimedChunk chunk) {
        ENTRIES.put(pack(level, chunkPos), new CacheEntry(chunk, System.currentTimeMillis()));
    }

    public static void invalidate(ChunkDimPos pos) {
        ENTRIES.remove(pack(pos.dimension().location().toString(), pos.x(), pos.z()));
    }

    public static void clear() {
        ENTRIES.clear();
        NerdKube.LOGGER.debug("NerdKube: cache de claims FTB Chunks limpo.");
    }

    private static long pack(ServerLevel level, ChunkPos chunkPos) {
        return pack(level.dimension().location().toString(), chunkPos.x, chunkPos.z);
    }

    private static long pack(String dimension, int chunkX, int chunkZ) {
        long dimHash = dimension.hashCode();
        return (dimHash << 32) ^ ((long) chunkX << 16) ^ (chunkZ & 0xFFFFL);
    }

    private record CacheEntry(ClaimedChunk chunk, long timestamp) {
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > TTL_MS;
        }
    }
}
