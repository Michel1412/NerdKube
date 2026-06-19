package br.com.nerdskube.client.screen;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.menu.CubeMakerMenu;
import br.com.nerdskube.ritual.RitualService;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CubeMakerScreen extends AbstractContainerScreen<CubeMakerMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(NerdKube.MOD_ID, "textures/gui/cube_maker.png");

    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;

    private static final int BUTTON_WIDTH = 72;
    private static final int BUTTON_HEIGHT = 20;
    private static final int TITLE_COLOR = 4210752;

    public CubeMakerScreen(CubeMakerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }

    @Override
    protected void init() {
        super.init();
        int buttonX = leftPos + CubeMakerMenu.CRAFT_BUTTON_X;
        int buttonY = topPos + CubeMakerMenu.CRAFT_BUTTON_Y;
        addRenderableWidget(Button.builder(Component.translatable("nerdkube.ritual.craft_button"), button -> {
            if (minecraft != null && minecraft.gameMode != null) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, RitualService.CRAFT_BUTTON_ID);
            }
        }).bounds(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    @Override
    protected void renderSlot(GuiGraphics graphics, Slot slot) {
        if (slot.index == 0) {
            ItemStack stack = slot.getItem();
            if (!stack.isEmpty()) {
                graphics.pose().pushPose();
                graphics.pose().translate(0.0F, 0.0F, 100.0F);
                graphics.renderItem(stack, slot.x, slot.y, slot.x + slot.y * imageWidth);
                graphics.renderItemDecorations(font, stack, slot.x, slot.y);
                graphics.pose().popPose();
            }
            return;
        }
        super.renderSlot(graphics, slot);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int titleX = (imageWidth - font.width(title)) / 2;
        graphics.drawString(font, title, titleX, titleLabelY, TITLE_COLOR, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, TITLE_COLOR, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
