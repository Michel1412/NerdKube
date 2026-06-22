package br.com.nerdskube.registry;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.component.ModDataComponents;
import br.com.nerdskube.item.EternalCubeBlockItem;
import br.com.nerdskube.item.ProgressionLoreBlockItem;
import br.com.nerdskube.item.ProgressionLoreItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NerdKube.MOD_ID);

    public static final DeferredItem<Item> NUCLEO_DE_MATERIA = ITEMS.registerSimpleItem("nucleo_de_materia", new Item.Properties());
    public static final DeferredItem<Item> CORACAO_ARCANO = ITEMS.registerSimpleItem("coracao_arcano", new Item.Properties());
    public static final DeferredItem<Item> RELIQUIA_DESBRAVADOR = progressionItem("reliquia_desbravador");
    public static final DeferredItem<Item> SEMENTE_CRIACAO = ITEMS.registerSimpleItem("semente_criacao", new Item.Properties());

    public static final DeferredItem<Item> FRAGMENTO_MATRIZ_DADOS = progressionItem("fragmento_matriz_dados");
    public static final DeferredItem<Item> PROCESSADOR_ANTIMATERIA_INSANA = progressionItem("processador_antimateria_insana");
    public static final DeferredItem<Item> MECANISMO_SOMBRA_CORROMPIDO = progressionItem("mecanismo_sombra_corrompido");
    public static final DeferredItem<Item> NUCLEO_LOGICO_INFUNDIDO = progressionItem("nucleo_logico_infundido");
    public static final DeferredItem<Item> MATRIZ_MODULAR_ESTABILIZADA = progressionItem("matriz_modular_estabilizada");
    public static final DeferredItem<Item> CHASSI_CYBER_FLUX = progressionItem("chassi_cyber_flux");
    public static final DeferredItem<Item> DISCO_MATERIA_ESCURA = progressionItem("disco_materia_escura");
    public static final DeferredItem<Item> SINGULARIDADE_HIPERCARREGADA = progressionItem("singularidade_hipercarregada");

    public static final DeferredItem<Item> OFERTA_POCO_SOMBRIA = progressionItem("oferta_poco_sombria");
    public static final DeferredItem<Item> COMPONENTE_MAGIA_STAGE1_COMPLETO = progressionItem("componente_magia_stage1_completo");
    public static final DeferredItem<Item> FRAGMENTO_MAGIA_STAGE2 = progressionItem("fragmento_magia_stage2");
    public static final DeferredItem<Item> COMPONENTE_MAGIA_STAGE2_COMPLETO = progressionItem("componente_magia_stage2_completo");
    public static final DeferredItem<Item> MATRIZ_RUNICA = progressionItem("matriz_runica");
    public static final DeferredItem<Item> ESSENCIA_VITAL_INSTAVEL = progressionItem("essencia_vital_instavel");

    public static final DeferredItem<Item> FRAGMENTO_AGRI_STAGE1 = progressionItem("fragmento_agri_stage1");
    public static final DeferredItem<Item> COMPONENTE_AGRI_STAGE1_COMPLETO = progressionItem("componente_agri_stage1_completo");
    public static final DeferredItem<Item> RUNIC_CRYSTAL_CAKE = progressionItem("runic_crystal_cake");
    public static final DeferredItem<Item> RAIZES_DOCES = progressionItem("raizes_doces");
    public static final DeferredItem<Item> RAIZES_DOCES_FRITAS = progressionItem("raizes_doces_fritas");
    public static final DeferredItem<Item> RAIZES_ANCESTRAIS_EM_CONSERVA = progressionItem("raizes_ancestrais_em_conserva");
    public static final DeferredItem<Item> ALTAR_REVESTIMENTO_AGRI = progressionItem("altar_revestimento_agri");
    public static final DeferredItem<Item> MASSA_NUTRIENTES_SUPREMOS = progressionItem("massa_nutrientes_supremos");
    public static final DeferredItem<Item> PACOTE_ESSENCIAS_AGRI = progressionItem("pacote_essencias_agri");

    public static final DeferredItem<Item> FRAGMENTO_COMBATE_STAGE1 = progressionItem("fragmento_combate_stage1");
    public static final DeferredItem<Item> COMPONENTE_COMBATE_STAGE1_COMPLETO = progressionItem("componente_combate_stage1_completo");
    public static final DeferredItem<Item> FRAGMENTO_COMBATE_STAGE2 = progressionItem("fragmento_combate_stage2");
    public static final DeferredItem<Item> COMPONENTE_COMBATE_STAGE2_COMPLETO = progressionItem("componente_combate_stage2_completo");
    public static final DeferredItem<Item> LAMINA_CONQUISTADOR = progressionItem("lamina_conquistador");
    public static final DeferredItem<Item> OLHO_DESBRAVADOR_PRIMAL = progressionItem("olho_desbravador_primal");

    public static final DeferredItem<Item> POEIRA_TRANSMUTACAO_PROIBIDA = progressionItem("poeira_transmutacao_proibida");

    public static final DeferredItem<BlockItem> CUBE_MAKER = ITEMS.register("cube_maker",
            () -> new BlockItem(ModBlocks.CUBE_MAKER.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PEDESTAL_TECH = ITEMS.register("pedestal_tech",
            () -> new BlockItem(ModBlocks.PEDESTAL_TECH.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PEDESTAL_MAGIA = ITEMS.register("pedestal_magia",
            () -> new BlockItem(ModBlocks.PEDESTAL_MAGIA.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PEDESTAL_EXPLORACAO = ITEMS.register("pedestal_exploracao",
            () -> new BlockItem(ModBlocks.PEDESTAL_EXPLORACAO.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PEDESTAL_AGRICULTURA = ITEMS.register("pedestal_agricultura",
            () -> new BlockItem(ModBlocks.PEDESTAL_AGRICULTURA.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PEDESTAL_BRUTO = ITEMS.register("pedestal_bruto",
            () -> new BlockItem(ModBlocks.PEDESTAL_BRUTO.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> MASSA_RUNICA_CRUA = ITEMS.register("massa_runica_crua",
            () -> new BlockItem(ModBlocks.MASSA_RUNICA_CRUA.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> NERD_CUBE = ITEMS.register("nerd_cube",
            () -> new BlockItem(ModBlocks.NERD_CUBE.get(), new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<net.minecraft.network.chat.Component> tooltip, TooltipFlag flag) {
                    ModDataComponents.appendCraftedByTooltip(stack, context, tooltip, flag);
                }
            });
    public static final DeferredItem<BlockItem> SCULK_GEAR_CRYSTAL = ITEMS.register("sculk_gear_crystal",
            () -> new BlockItem(ModBlocks.SCULK_GEAR_CRYSTAL.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> ETERNAL_CUBE = ITEMS.register("eternal_cube",
            () -> new EternalCubeBlockItem(ModBlocks.ETERNAL_CUBE.get(), new Item.Properties()));

    private static DeferredItem<Item> progressionItem(String id) {
        return ITEMS.register(id, () -> new ProgressionLoreItem(
                new Item.Properties(), "item.nerdkube." + id + ".lore"));
    }

    private ModItems() {}
}
