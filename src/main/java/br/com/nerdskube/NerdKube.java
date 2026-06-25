package br.com.nerdskube;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import br.com.nerdskube.attachment.ModAttachments;
import br.com.nerdskube.component.ModDataComponents;
import br.com.nerdskube.config.NerdKubeConfig;
import br.com.nerdskube.config.NerdKubeServerConfig;
import br.com.nerdskube.registry.ModLootModifiers;
import br.com.nerdskube.integration.EndRemIntegration;
import br.com.nerdskube.integration.ftbchunks.FtbChunksIntegration;
import br.com.nerdskube.integration.fakeplayer.FakePlayerIntegration;
import br.com.nerdskube.registry.ModBlockEntities;
import br.com.nerdskube.registry.ModBlocks;
import br.com.nerdskube.registry.ModCreativeTabs;
import br.com.nerdskube.registry.ModItems;
import br.com.nerdskube.registry.ModMenus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(NerdKube.MOD_ID)
public class NerdKube {
    public static final String MOD_ID = "nerdkube";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NerdKube(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, NerdKubeConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, NerdKubeServerConfig.SPEC);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (ModList.get().isLoaded(EndRemIntegration.MOD_ID)) {
                EndRemIntegration.init();
            } else {
                LOGGER.info("End Remastered não presente — NerdKube carregará sem integrações de endgame do End Remastered.");
            }

            FakePlayerIntegration.init();
            FtbChunksIntegration.init();
        });
    }

    private void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("NerdKube iniciado no servidor (endgame custom para NerdCube).");
    }
}
