package br.com.nerdskube.integration.jei;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.config.NerdKubeServerConfigAccess;
import br.com.nerdskube.integration.jei.recipe.NerdKubeRitualRecipe;
import br.com.nerdskube.integration.jei.recipe.RitualCardinalSlot;
import br.com.nerdskube.integration.jei.recipe.RitualLayout;
import br.com.nerdskube.integration.jei.recipe.RitualRingSlot;
import br.com.nerdskube.registry.ModBlocks;
import br.com.nerdskube.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public final class NerdKubeJeiRecipes {
    private NerdKubeJeiRecipes() {}

    public static List<NerdKubeRitualRecipe> ritualRecipes() {
        return List.of(
                cubeFinaleRitual(),
                pedestalMagiaRitual(),
                explorationAmuletRitual(),
                forbiddenTransmutationRitual());
    }

    public static NerdKubeRitualRecipe cubeFinaleRitual() {
        return new NerdKubeRitualRecipe(
                ritualId("cube_finale"),
                new ItemStack(ModBlocks.CUBE_MAKER.get()),
                new ItemStack(ModBlocks.CUBE_MAKER.get()),
                new ItemStack(Items.CRAFTER),
                RitualLayout.CARDINAL,
                List.of(),
                List.of(
                        new RitualCardinalSlot(
                                new ItemStack(ModBlocks.PEDESTAL_TECH.get()),
                                new ItemStack(ModItems.NUCLEO_DE_MATERIA.get())),
                        new RitualCardinalSlot(
                                new ItemStack(ModBlocks.PEDESTAL_EXPLORACAO.get()),
                                new ItemStack(ModItems.RELIQUIA_DESBRAVADOR.get())),
                        new RitualCardinalSlot(
                                new ItemStack(ModBlocks.PEDESTAL_MAGIA.get()),
                                new ItemStack(ModItems.CORACAO_ARCANO.get())),
                        new RitualCardinalSlot(
                                new ItemStack(ModBlocks.PEDESTAL_AGRICULTURA.get()),
                                new ItemStack(ModItems.SEMENTE_CRIACAO.get()))),
                new ItemStack(ModBlocks.NERD_CUBE.get()),
                ritualTitle("cube_finale"),
                ritualInfoLines("cube_finale", 1));
    }

    public static NerdKubeRitualRecipe pedestalMagiaRitual() {
        ItemStack displayIcon = JeiItemStacks.of("occultism:ritual_dummy/craft_pedestal_magia");
        if (displayIcon.isEmpty()) {
            displayIcon = new ItemStack(ModBlocks.PEDESTAL_MAGIA.get());
        }
        ItemStack sacrificialBowl = JeiItemStacks.of("occultism:sacrificial_bowl");
        ItemStack goldenBowl = JeiItemStacks.of("occultism:golden_sacrificial_bowl");
        return new NerdKubeRitualRecipe(
                ritualId("pedestal_magia"),
                displayIcon,
                goldenBowl.isEmpty() ? new ItemStack(ModBlocks.PEDESTAL_MAGIA.get()) : goldenBowl,
                JeiItemStacks.of("occultism:book_of_binding_bound_afrit"),
                RitualLayout.RING,
                occultismIngredientRing(
                        sacrificialBowl,
                        "occultism:sacrificial_bowl",
                        "malum:block_of_soul_stained_steel",
                        "malum:block_of_soul_stained_steel",
                        "evilcraft:dark_power_gem_block",
                        "evilcraft:dark_power_gem_block",
                        "ars_nouveau:sourcestone",
                        "ars_nouveau:sourcestone",
                        "ars_nouveau:sourcestone",
                        "ars_nouveau:sourcestone",
                        "irons_spellbooks:arcane_salvage"),
                List.of(),
                new ItemStack(ModBlocks.PEDESTAL_MAGIA.get()),
                ritualTitle("pedestal_magia"),
                ritualInfoLines("pedestal_magia", 2),
                sacrificialBowl);
    }

    private static List<RitualRingSlot> occultismIngredientRing(ItemStack bowlDecor, String... itemIds) {
        List<RitualRingSlot> slots = new ArrayList<>(itemIds.length);
        for (String itemId : itemIds) {
            ItemStack item = JeiItemStacks.of(itemId);
            if (!item.isEmpty()) {
                slots.add(new RitualRingSlot(bowlDecor, item));
            }
        }
        return slots;
    }

    public static NerdKubeRitualRecipe explorationAmuletRitual() {
        return new NerdKubeRitualRecipe(
                ritualId("exploration_amulet"),
                new ItemStack(ModItems.OLHO_DESBRAVADOR_PRIMAL.get()),
                new ItemStack(Items.ENCHANTING_TABLE),
                new ItemStack(ModItems.OLHO_DESBRAVADOR_PRIMAL.get()),
                RitualLayout.RING,
                List.of(
                        RitualRingSlot.of(new ItemStack(ModItems.LAMINA_CONQUISTADOR.get())),
                        RitualRingSlot.of(new ItemStack(ModItems.COMPONENTE_COMBATE_STAGE1_COMPLETO.get(), 3)),
                        RitualRingSlot.of(new ItemStack(ModItems.COMPONENTE_COMBATE_STAGE2_COMPLETO.get(), 3)),
                        RitualRingSlot.of(new ItemStack(ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get()))),
                List.of(),
                new ItemStack(ModItems.RELIQUIA_DESBRAVADOR.get()),
                ritualTitle("exploration_amulet"),
                ritualInfoLines("exploration_amulet", 3));
    }

    public static NerdKubeRitualRecipe forbiddenTransmutationRitual() {
        int xpCost = NerdKubeServerConfigAccess.transmutationXpLevelCost();
        int chancePercent = Math.round(NerdKubeServerConfigAccess.transmutationSuccessChance() * 100.0F);
        return new NerdKubeRitualRecipe(
                ritualId("forbidden_transmutation"),
                new ItemStack(ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get()),
                new ItemStack(ModBlocks.PEDESTAL_EXPLORACAO.get()),
                new ItemStack(Items.BEACON),
                RitualLayout.RING,
                List.of(),
                List.of(),
                new ItemStack(ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get()),
                ritualTitle("forbidden_transmutation"),
                List.of(
                        Component.translatable("nerdkube.jei.ritual.forbidden_transmutation.info1"),
                        Component.translatable("nerdkube.jei.ritual.forbidden_transmutation.info2", xpCost),
                        Component.translatable("nerdkube.jei.ritual.forbidden_transmutation.info3", chancePercent),
                        Component.translatable("nerdkube.jei.ritual.forbidden_transmutation.info4")),
                ItemStack.EMPTY,
                false);
    }

    public static void registerIngredientInfo(mezz.jei.api.registration.IRecipeRegistration registration) {
        addInfo(registration, ModItems.PEDESTAL_TECH.get(), "pedestal_tech");
        addInfo(registration, ModItems.FRAGMENTO_MATRIZ_DADOS.get(), "fragmento_matriz_dados");
        addInfo(registration, ModItems.PROCESSADOR_ANTIMATERIA_INSANA.get(), "processador_antimateria_insana");
        addInfo(registration, ModItems.MECANISMO_SOMBRA_CORROMPIDO.get(), "mecanismo_sombra_corrompido");
        addInfo(registration, ModItems.SCULK_GEAR_CRYSTAL.get(), "sculk_gear_crystal");
        addInfo(registration, ModItems.NUCLEO_LOGICO_INFUNDIDO.get(), "nucleo_logico_infundido");
        addInfo(registration, ModItems.MATRIZ_MODULAR_ESTABILIZADA.get(), "matriz_modular_estabilizada");
        addInfo(registration, ModItems.CHASSI_CYBER_FLUX.get(), "chassi_cyber_flux");
        addInfo(registration, ModItems.DISCO_MATERIA_ESCURA.get(), "disco_materia_escura");
        addInfo(registration, ModItems.SINGULARIDADE_HIPERCARREGADA.get(), "singularidade_hipercarregada");
        addInfo(registration, ModItems.NUCLEO_DE_MATERIA.get(), "nucleo_de_materia");
        addInfo(registration, ModItems.OFERTA_POCO_SOMBRIA.get(), "oferta_poco_sombria");
        addInfo(registration, ModItems.COMPONENTE_MAGIA_STAGE1_COMPLETO.get(), "componente_magia_stage1_completo");
        addInfo(registration, ModItems.FRAGMENTO_MAGIA_STAGE2.get(), "fragmento_magia_stage2");
        addInfo(registration, ModItems.COMPONENTE_MAGIA_STAGE2_COMPLETO.get(), "componente_magia_stage2_completo");
        addInfo(registration, ModItems.MATRIZ_RUNICA.get(), "matriz_runica");
        addInfo(registration, ModItems.ESSENCIA_VITAL_INSTAVEL.get(), "essencia_vital_instavel");
        addInfo(registration, ModItems.PEDESTAL_BRUTO.get(), "pedestal_bruto");
        addInfo(registration, ModItems.FRAGMENTO_AGRI_STAGE1.get(), "fragmento_agri_stage1");
        addInfo(registration, ModItems.PACOTE_ESSENCIAS_AGRI.get(), "pacote_essencias_agri");
        addInfo(registration, ModItems.COMPONENTE_AGRI_STAGE1_COMPLETO.get(), "componente_agri_stage1_completo", 5);
        addInfo(registration, ModItems.MASSA_RUNICA_CRUA.get(), "massa_runica_crua", 6);
        addInfo(registration, ModItems.RUNIC_CRYSTAL_CAKE.get(), "runic_crystal_cake", 8);
        addInfo(registration, ModItems.RAIZES_DOCES.get(), "raizes_doces", 6);
        addInfo(registration, ModItems.RAIZES_DOCES_FRITAS.get(), "raizes_doces_fritas", 5);
        addInfo(registration, ModItems.RAIZES_ANCESTRAIS_EM_CONSERVA.get(), "raizes_ancestrais_em_conserva", 6);
        addInfo(registration, ModItems.ALTAR_REVESTIMENTO_AGRI.get(), "altar_revestimento_agri", 6);
        addInfo(registration, ModItems.MASSA_NUTRIENTES_SUPREMOS.get(), "massa_nutrientes_supremos", 8);
        addInfo(registration, ModItems.FRAGMENTO_COMBATE_STAGE1.get(), "fragmento_combate_stage1", 4);
        addInfo(registration, ModItems.COMPONENTE_COMBATE_STAGE1_COMPLETO.get(), "componente_combate_stage1_completo", 3);
        addInfo(registration, ModItems.FRAGMENTO_COMBATE_STAGE2.get(), "fragmento_combate_stage2", 4);
        addInfo(registration, ModItems.COMPONENTE_COMBATE_STAGE2_COMPLETO.get(), "componente_combate_stage2_completo", 3);
        addInfo(registration, ModItems.LAMINA_CONQUISTADOR.get(), "lamina_conquistador", 3);
        addInfo(registration, ModItems.OLHO_DESBRAVADOR_PRIMAL.get(), "olho_desbravador_primal", 3);
        addInfo(registration, ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get(), "poeira_transmutacao_proibida", 6);
        addInfo(registration, ModItems.CORACAO_ARCANO.get(), "coracao_arcano");
        addInfo(registration, ModItems.RELIQUIA_DESBRAVADOR.get(), "reliquia_desbravador", 6);
        addInfo(registration, ModItems.SEMENTE_CRIACAO.get(), "semente_criacao", 8);
        addInfo(registration, ModItems.PEDESTAL_MAGIA.get(), "pedestal_magia");
        addInfo(registration, ModItems.PEDESTAL_EXPLORACAO.get(), "pedestal_exploracao", 6);
        addInfo(registration, ModItems.PEDESTAL_AGRICULTURA.get(), "pedestal_agricultura");
        addInfo(registration, ModItems.CUBE_MAKER.get(), "cube_maker");
        addInfo(registration, ModItems.NERD_CUBE.get(), "nerd_cube");

        addExternalInfo(registration, "farmersdelight:skillet", "farmersdelight_skillet", 4);

        addExternalInfo(registration, "farm_and_charm:crafting_bowl", "bakery_crafting_bowl", 4);
        addExternalInfo(registration, "bakery:baker_station", "bakery_baker_station", 5);
    }

    private static ResourceLocation ritualId(String path) {
        return ResourceLocation.fromNamespaceAndPath(NerdKube.MOD_ID, path);
    }

    private static Component ritualTitle(String key) {
        return Component.translatable("nerdkube.jei.ritual." + key + ".title");
    }

    private static List<Component> ritualInfoLines(String key, int count) {
        List<Component> lines = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            lines.add(Component.translatable("nerdkube.jei.ritual." + key + ".info" + i));
        }
        return lines;
    }

    private static void addInfo(
            mezz.jei.api.registration.IRecipeRegistration registration,
            net.minecraft.world.item.Item item,
            String key) {
        addInfo(registration, item, key, 3);
    }

    private static void addInfo(
            mezz.jei.api.registration.IRecipeRegistration registration,
            net.minecraft.world.item.Item item,
            String key,
            int lineCount) {
        List<Component> lines = new ArrayList<>(lineCount);
        for (int i = 1; i <= lineCount; i++) {
            lines.add(Component.translatable("nerdkube.jei.info." + key + ".line" + i));
        }
        registration.addIngredientInfo(
                new ItemStack(item),
                mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
                lines.toArray(Component[]::new));
    }

    private static void addExternalInfo(
            mezz.jei.api.registration.IRecipeRegistration registration,
            String itemId,
            String key,
            int lineCount) {
        ItemStack stack = JeiItemStacks.of(itemId);
        if (stack.isEmpty()) {
            return;
        }
        List<Component> lines = new ArrayList<>(lineCount);
        for (int i = 1; i <= lineCount; i++) {
            lines.add(Component.translatable("nerdkube.jei.info." + key + ".line" + i));
        }
        registration.addIngredientInfo(
                stack, mezz.jei.api.constants.VanillaTypes.ITEM_STACK, lines.toArray(Component[]::new));
    }
}
