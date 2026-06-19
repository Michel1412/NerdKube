package br.com.nerdskube.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PlayerRitualData(boolean hasCraftedTrophy) {
    public static final PlayerRitualData DEFAULT = new PlayerRitualData(false);

    public static final Codec<PlayerRitualData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("has_crafted_trophy").forGetter(PlayerRitualData::hasCraftedTrophy)
    ).apply(instance, PlayerRitualData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerRitualData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, PlayerRitualData::hasCraftedTrophy,
            PlayerRitualData::new);

    public PlayerRitualData withCrafted() {
        return new PlayerRitualData(true);
    }
}
