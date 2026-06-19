package br.com.nerdskube.integration.jade;

import br.com.nerdskube.block.NerdCubeBlock;
import br.com.nerdskube.block.ThemedPedestalBlock;
import br.com.nerdskube.block.entity.NerdCubeBlockEntity;
import br.com.nerdskube.block.entity.RitualPedestalBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class NerdKubeJadePlugin implements IWailaPlugin {
    public static final ResourceLocation NERD_CUBE_CRAFTED_BY = ResourceLocation.fromNamespaceAndPath("nerdkube", "nerd_cube_crafted_by");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(PedestalJadeProvider.INSTANCE, RitualPedestalBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(NerdCubeProvider.INSTANCE, NerdCubeBlock.class);
        registration.registerBlockComponent(PedestalJadeProvider.INSTANCE, ThemedPedestalBlock.class);
    }

    public static class NerdCubeProvider implements IBlockComponentProvider {
        public static final NerdCubeProvider INSTANCE = new NerdCubeProvider();

        private NerdCubeProvider() {}

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            String crafter = accessor.getBlockEntity() instanceof NerdCubeBlockEntity nerdCube
                    ? nerdCube.getCraftedBy()
                    : "";
            if (!crafter.isEmpty()) {
                tooltip.add(Component.translatable("nerdkube.jade.crafted_by", crafter));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return NERD_CUBE_CRAFTED_BY;
        }
    }
}
