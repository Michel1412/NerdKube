package br.com.nerdskube;

import br.com.nerdskube.block.entity.CubeMakerBlockEntity;
import br.com.nerdskube.block.entity.RitualPedestalBlockEntity;
import br.com.nerdskube.registry.ModBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = NerdKube.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModCapabilityEvents {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.RITUAL_PEDESTAL.get(),
                (pedestal, side) -> pedestal.getInventory());

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CUBE_MAKER.get(),
                (maker, side) -> maker.getInventory());
    }
}
