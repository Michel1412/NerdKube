package br.com.nerdskube.client;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.client.renderer.PedestalOfferingRenderer;
import br.com.nerdskube.client.screen.CubeMakerScreen;
import br.com.nerdskube.registry.ModBlockEntities;
import br.com.nerdskube.registry.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = NerdKube.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.CUBE_MAKER.get(), CubeMakerScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.RITUAL_PEDESTAL.get(), PedestalOfferingRenderer::new);
    }
}
