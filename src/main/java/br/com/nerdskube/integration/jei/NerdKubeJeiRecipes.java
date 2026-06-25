package br.com.nerdskube.integration.jei;

import br.com.nerdskube.integration.jei.recipe.EndgameRitualRecipe;
import br.com.nerdskube.integration.jei.recipe.ProgressionCraftRecipe;
import br.com.nerdskube.registry.ModBlocks;
import br.com.nerdskube.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public final class NerdKubeJeiRecipes {
    private NerdKubeJeiRecipes() {}

    public static List<ProgressionCraftRecipe> progressionCrafts() {
        return List.of(pedestalBrutoCraft());
    }

    public static ProgressionCraftRecipe pedestalBrutoCraft() {
        return new ProgressionCraftRecipe(
                List.of(
                        JeiItemStacks.of("framedblocks:framed_cube"),
                        JeiItemStacks.of("minecraft:mossy_stone_brick_stairs"),
                        JeiItemStacks.of("framedblocks:framed_cube"),
                        JeiItemStacks.of("rechiseled:quartz_block_bordered"),
                        JeiItemStacks.of("mysticalagriculture:supremium_block"),
                        JeiItemStacks.of("rechiseled:quartz_block_bordered"),
                        JeiItemStacks.of("mcwroofs:oak_roof"),
                        JeiItemStacks.of("functionalstorage:storage_controller"),
                        JeiItemStacks.of("mcwroofs:oak_roof")),
                new ItemStack(ModBlocks.PEDESTAL_BRUTO.get()));
    }

    public static EndgameRitualRecipe endgameRitual() {
        return new EndgameRitualRecipe(
                new ItemStack(Items.CRAFTER),
                new ItemStack(ModBlocks.PEDESTAL_TECH.get()),
                new ItemStack(ModItems.NUCLEO_DE_MATERIA.get()),
                new ItemStack(ModBlocks.PEDESTAL_MAGIA.get()),
                new ItemStack(ModItems.CORACAO_ARCANO.get()),
                new ItemStack(ModBlocks.PEDESTAL_EXPLORACAO.get()),
                new ItemStack(ModItems.RELIQUIA_DESBRAVADOR.get()),
                new ItemStack(ModBlocks.PEDESTAL_AGRICULTURA.get()),
                new ItemStack(ModItems.SEMENTE_CRIACAO.get()),
                new ItemStack(ModBlocks.NERD_CUBE.get()));
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
