package br.com.nerdskube.client.renderer;

import br.com.nerdskube.block.entity.RitualPedestalBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PedestalOfferingRenderer implements BlockEntityRenderer<RitualPedestalBlockEntity> {
    private static final double TIME_DIVISOR = 800.0D;
    private static final double TWO_PI = Math.PI * 2.0D;

    private final ItemRenderer itemRenderer;

    public PedestalOfferingRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(
            RitualPedestalBlockEntity pedestal,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay) {
        ItemStack offering = pedestal.getInventory().getStackInSlot(RitualPedestalBlockEntity.SLOT_OFFERING);
        if (offering.isEmpty()) {
            return;
        }

        Level level = pedestal.getLevel();
        if (level == null) {
            return;
        }

        double time = System.currentTimeMillis() / TIME_DIVISOR;
        float bob = (float) (Math.sin(time % TWO_PI) * 0.065D);
        float rotation = (float) ((time * 40.0D) % 360.0D);
        float scale = offering.getItem() instanceof BlockItem ? 0.95F : 0.75F;

        poseStack.pushPose();
        poseStack.translate(0.5, 1.2, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, bob, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        itemRenderer.renderStatic(
                offering,
                ItemDisplayContext.GROUND,
                packedLight,
                packedOverlay,
                poseStack,
                buffer,
                level,
                pedestal.getBlockPos().hashCode());

        poseStack.popPose();
    }
}
