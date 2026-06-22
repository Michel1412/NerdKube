package br.com.nerdskube.registry;



import br.com.nerdskube.NerdKube;

import net.minecraft.core.registries.Registries;

import net.minecraft.network.chat.Component;

import net.minecraft.world.item.CreativeModeTab;

import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.registries.DeferredHolder;

import net.neoforged.neoforge.registries.DeferredRegister;



public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =

            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NerdKube.MOD_ID);



    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NERDKUBE = CREATIVE_TABS.register(

            "main",

            () -> CreativeModeTab.builder()

                    .title(Component.translatable("itemGroup.nerdkube"))

                    .icon(() -> new ItemStack(ModItems.NERD_CUBE.get()))

                    .displayItems((params, output) -> {

                        output.accept(ModItems.CUBE_MAKER);

                        output.accept(ModItems.PEDESTAL_TECH);

                        output.accept(ModItems.PEDESTAL_MAGIA);

                        output.accept(ModItems.PEDESTAL_EXPLORACAO);

                        output.accept(ModItems.PEDESTAL_AGRICULTURA);

                        output.accept(ModItems.PEDESTAL_BRUTO);

                        output.accept(ModItems.NERD_CUBE);



                        output.accept(ModItems.NUCLEO_DE_MATERIA);

                        output.accept(ModItems.CORACAO_ARCANO);

                        output.accept(ModItems.RELIQUIA_DESBRAVADOR);

                        output.accept(ModItems.SEMENTE_CRIACAO);



                        output.accept(ModItems.FRAGMENTO_MATRIZ_DADOS);

                        output.accept(ModItems.PROCESSADOR_ANTIMATERIA_INSANA);

                        output.accept(ModItems.MECANISMO_SOMBRA_CORROMPIDO);

                        output.accept(ModItems.SCULK_GEAR_CRYSTAL);

                        output.accept(ModItems.NUCLEO_LOGICO_INFUNDIDO);

                        output.accept(ModItems.MATRIZ_MODULAR_ESTABILIZADA);

                        output.accept(ModItems.CHASSI_CYBER_FLUX);

                        output.accept(ModItems.DISCO_MATERIA_ESCURA);

                        output.accept(ModItems.SINGULARIDADE_HIPERCARREGADA);

                        output.accept(ModItems.OFERTA_POCO_SOMBRIA);
                        output.accept(ModItems.COMPONENTE_MAGIA_STAGE1_COMPLETO);
                        output.accept(ModItems.FRAGMENTO_MAGIA_STAGE2);
                        output.accept(ModItems.COMPONENTE_MAGIA_STAGE2_COMPLETO);
                        output.accept(ModItems.MATRIZ_RUNICA);
                        output.accept(ModItems.ESSENCIA_VITAL_INSTAVEL);

                        output.accept(ModItems.FRAGMENTO_AGRI_STAGE1);
                        output.accept(ModItems.PACOTE_ESSENCIAS_AGRI);
                        output.accept(ModItems.COMPONENTE_AGRI_STAGE1_COMPLETO);
                        output.accept(ModItems.MASSA_RUNICA_CRUA);
                        output.accept(ModItems.RUNIC_CRYSTAL_CAKE);
                        output.accept(ModItems.RAIZES_DOCES);
                        output.accept(ModItems.RAIZES_DOCES_FRITAS);
                        output.accept(ModItems.RAIZES_ANCESTRAIS_EM_CONSERVA);
                        output.accept(ModItems.ALTAR_REVESTIMENTO_AGRI);
                        output.accept(ModItems.MASSA_NUTRIENTES_SUPREMOS);

                        output.accept(ModItems.FRAGMENTO_COMBATE_STAGE1);
                        output.accept(ModItems.COMPONENTE_COMBATE_STAGE1_COMPLETO);
                        output.accept(ModItems.FRAGMENTO_COMBATE_STAGE2);
                        output.accept(ModItems.COMPONENTE_COMBATE_STAGE2_COMPLETO);
                        output.accept(ModItems.LAMINA_CONQUISTADOR);
                        output.accept(ModItems.OLHO_DESBRAVADOR_PRIMAL);

                        output.accept(ModItems.POEIRA_TRANSMUTACAO_PROIBIDA);

                    })

                    .build());



    private ModCreativeTabs() {}

}


