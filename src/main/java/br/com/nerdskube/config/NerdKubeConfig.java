package br.com.nerdskube.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class NerdKubeConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> CUBE_MAKER_VARIANT;
    public static final ModConfigSpec.ConfigValue<String> NERD_CUBE_VARIANT;

    public static final ModConfigSpec SPEC;

    static {
        BUILDER.comment("Variantes de textura (a, b ou c).");
        BUILDER.comment("Após alterar, execute: python tools/generate_textures.py e reinicie o jogo.");
        BUILDER.push("textures");

        CUBE_MAKER_VARIANT = BUILDER
                .comment("Visual do Fabricador de Cubo: a (tech), b (runas), c (cyber)")
                .define("cubeMakerVariant", "a");

        NERD_CUBE_VARIANT = BUILDER
                .comment("Visual do NerdCube: a (matéria escura), b (supremium), c (retrô)")
                .define("nerdCubeVariant", "a");

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    private NerdKubeConfig() {}
}
