package br.com.nerdskube.integration.jade;

import br.com.nerdskube.block.ThemedPedestalBlock;
import br.com.nerdskube.block.entity.RitualPedestalBlockEntity;
import br.com.nerdskube.ritual.PedestalDirection;
import br.com.nerdskube.ritual.PedestalRitualStatus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum PedestalJadeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("nerdkube", "pedestal_ritual");

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (!(accessor.getBlock() instanceof ThemedPedestalBlock pedestalBlock)) {
            return;
        }

        PedestalRitualStatus.Result status = PedestalRitualStatus.evaluate(
                accessor.getLevel(),
                accessor.getPosition(),
                pedestalBlock);
        tag.putString("Status", status.kind().name());
        tag.putString("ExpectedDir", pedestalBlock.getDirection().name());
        if (status.actualDirection() != null) {
            tag.putString("ActualDir", status.actualDirection().name());
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof RitualPedestalBlockEntity pedestal) {
            ItemStack offering = pedestal.getInventory().getStackInSlot(RitualPedestalBlockEntity.SLOT_OFFERING);
            if (!offering.isEmpty()) {
                tooltip.add(IElementHelper.get().smallItem(offering));
            }
        }

        tooltip.add(statusMessage(accessor.getServerData(), accessor.getBlock()));
    }

    private static Component statusMessage(CompoundTag data, net.minecraft.world.level.block.Block block) {
        if (!data.contains("Status")) {
            if (block instanceof ThemedPedestalBlock pedestalBlock) {
                return Component.translatable(
                        "nerdkube.jade.pedestal.status.label",
                        Component.translatable("nerdkube.jade.pedestal.status.no_cube_maker"));
            }
            return Component.empty();
        }

        PedestalDirection expected = data.contains("ExpectedDir")
                ? PedestalDirection.valueOf(data.getString("ExpectedDir"))
                : null;

        return switch (data.getString("Status")) {
            case "READY" -> Component.translatable(
                    "nerdkube.jade.pedestal.status.label",
                    Component.translatable("nerdkube.jade.pedestal.status.ready"));
            case "MISSING_OFFERING" -> Component.translatable(
                    "nerdkube.jade.pedestal.status.label",
                    Component.translatable("nerdkube.jade.pedestal.status.missing_offering"));
            case "WRONG_OFFERING" -> Component.translatable(
                    "nerdkube.jade.pedestal.status.label",
                    Component.translatable("nerdkube.jade.pedestal.status.wrong_offering"));
            case "WRONG_POSITION" -> {
                Component expectedDir = expected != null
                        ? Component.translatable(expected.translationKey())
                        : Component.empty();
                yield Component.translatable(
                        "nerdkube.jade.pedestal.status.label",
                        Component.translatable("nerdkube.jade.pedestal.status.wrong_position", expectedDir));
            }
            default -> Component.translatable(
                    "nerdkube.jade.pedestal.status.label",
                    Component.translatable("nerdkube.jade.pedestal.status.no_cube_maker"));
        };
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
