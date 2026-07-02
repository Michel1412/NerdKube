package br.com.nerdskube.integration.jei.category;

import br.com.nerdskube.integration.jei.JeiItemStacks;
import br.com.nerdskube.NerdKube;
import br.com.nerdskube.integration.jei.NerdKubeRecipeTypes;
import br.com.nerdskube.integration.jei.recipe.NerdKubeRitualRecipe;
import br.com.nerdskube.integration.jei.recipe.RitualCardinalSlot;
import br.com.nerdskube.integration.jei.recipe.RitualLayout;
import br.com.nerdskube.integration.jei.recipe.RitualRingSlot;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NerdKubeRitualCategory implements IRecipeCategory<NerdKubeRitualRecipe> {
    public static final Component TITLE = Component.translatable("jei.category.nerdkube.endgame_ritual");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(NerdKube.MOD_ID, "textures/gui/jei/arrow.png");

    private final IDrawable background;
    private final IDrawable arrow;
    private final IDrawable categoryIcon;
    private final int ritualCenterX;
    private final int ritualCenterY;
    private final int iconWidth = 16;
    private int recipeOutputOffsetX = 75;
    private final int infoTextX = 94;

    public NerdKubeRitualCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(168, 120);
        this.ritualCenterX = this.background.getWidth() / 2 - this.iconWidth / 2 - 30;
        this.ritualCenterY = this.background.getHeight() / 2 - this.iconWidth / 2 + 20;
        this.arrow = guiHelper.createDrawable(ARROW_TEXTURE, 0, 0, 64, 46);
        ItemStack iconStack = JeiItemStacks.of("occultism:golden_sacrificial_bowl");
        this.categoryIcon = iconStack.isEmpty()
                ? guiHelper.createDrawableItemStack(new ItemStack(net.minecraft.world.item.Items.ENCHANTING_TABLE))
                : guiHelper.createDrawableItemStack(iconStack);
    }

    @Override
    public RecipeType<NerdKubeRitualRecipe> getRecipeType() {
        return NerdKubeRecipeTypes.NERDKUBE_RITUAL;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return categoryIcon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, NerdKubeRitualRecipe recipe, IFocusGroup focuses) {
        recipeOutputOffsetX = 75;

        if (!recipe.displayIcon().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 0, 0).addItemStack(recipe.displayIcon());
        }

        if (!recipe.activationItem().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, ritualCenterX, ritualCenterY - 5)
                    .addItemStack(recipe.activationItem());
        }

        if (!recipe.centerCatalyst().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, ritualCenterX, ritualCenterY)
                    .addItemStack(recipe.centerCatalyst());
        }

        if (recipe.layout() == RitualLayout.CARDINAL) {
            addCardinalSlots(builder, recipe.cardinalSlots());
        } else {
            addRingSlots(builder, recipe);
        }

        if (!recipe.output().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, ritualCenterX + recipeOutputOffsetX, ritualCenterY - 5)
                    .addItemStack(recipe.output());
        }

        if (!recipe.centerCatalyst().isEmpty() && recipe.mirrorCenterAtOutput()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, ritualCenterX + recipeOutputOffsetX, ritualCenterY)
                    .addItemStack(recipe.centerCatalyst());
        }
    }

    @Override
    public void draw(
            NerdKubeRitualRecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics guiGraphics,
            double mouseX,
            double mouseY) {
        RenderSystem.enableBlend();
        arrow.draw(guiGraphics, ritualCenterX + recipeOutputOffsetX - 20, ritualCenterY);
        RenderSystem.disableBlend();

        Font font = Minecraft.getInstance().font;
        int lineY = 0;
        int lineHeight = font.lineHeight;

        drawStringCentered(guiGraphics, font, recipe.title(), infoTextX, lineY);
        lineY += lineHeight;

        for (Component line : recipe.infoLines()) {
            drawStringCentered(guiGraphics, font, line, infoTextX, lineY);
            lineY += lineHeight;
        }
    }

    private void addCardinalSlots(IRecipeLayoutBuilder builder, List<RitualCardinalSlot> slots) {
        List<Vec3i> positions = cardinalPositions();
        for (int i = 0; i < slots.size() && i < positions.size(); i++) {
            RitualCardinalSlot slot = slots.get(i);
            Vec3i pos = positions.get(i);
            if (!slot.offering().isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, pos.getX(), pos.getY() - 5)
                        .addItemStack(slot.offering());
            }
            if (!slot.pedestal().isEmpty()) {
                builder.addSlot(RecipeIngredientRole.RENDER_ONLY, pos.getX(), pos.getY())
                        .addItemStack(slot.pedestal());
            }
        }
    }

    private void addRingSlots(IRecipeLayoutBuilder builder, NerdKubeRitualRecipe recipe) {
        List<Vec3i> positions = ringPositions();
        ItemStack defaultDecor = recipe.ringDecor();
        for (int i = 0; i < recipe.ringSlots().size() && i < positions.size(); i++) {
            RitualRingSlot slot = recipe.ringSlots().get(i);
            Vec3i pos = positions.get(i);
            if (!slot.item().isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, pos.getX(), pos.getY() - 5)
                        .addItemStack(slot.item());
            }
            ItemStack decor = !slot.decor().isEmpty() ? slot.decor() : defaultDecor;
            if (!decor.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.RENDER_ONLY, pos.getX(), pos.getY())
                        .addItemStack(decor);
            }
        }
    }

    private List<Vec3i> cardinalPositions() {
        int radius = 30;
        return List.of(
                new Vec3i(ritualCenterX, ritualCenterY - radius, 0),
                new Vec3i(ritualCenterX + radius, ritualCenterY, 0),
                new Vec3i(ritualCenterX, ritualCenterY + radius, 0),
                new Vec3i(ritualCenterX - radius, ritualCenterY, 0));
    }

    private List<Vec3i> ringPositions() {
        int sacrificialCircleRadius = 30;
        int sacrificialBowlPaddingVertical = 20;
        int sacrificialBowlPaddingHorizontal = 15;
        return Stream.of(
                        new Vec3i(ritualCenterX, ritualCenterY - sacrificialCircleRadius, 0),
                        new Vec3i(ritualCenterX + sacrificialCircleRadius, ritualCenterY, 0),
                        new Vec3i(ritualCenterX, ritualCenterY + sacrificialCircleRadius, 0),
                        new Vec3i(ritualCenterX - sacrificialCircleRadius, ritualCenterY, 0),
                        new Vec3i(
                                ritualCenterX + sacrificialBowlPaddingHorizontal,
                                ritualCenterY - sacrificialCircleRadius,
                                0),
                        new Vec3i(
                                ritualCenterX + sacrificialCircleRadius,
                                ritualCenterY - sacrificialBowlPaddingVertical,
                                0),
                        new Vec3i(
                                ritualCenterX - sacrificialBowlPaddingHorizontal,
                                ritualCenterY + sacrificialCircleRadius,
                                0),
                        new Vec3i(
                                ritualCenterX - sacrificialCircleRadius,
                                ritualCenterY + sacrificialBowlPaddingVertical,
                                0),
                        new Vec3i(
                                ritualCenterX - sacrificialBowlPaddingHorizontal,
                                ritualCenterY - sacrificialCircleRadius,
                                0),
                        new Vec3i(
                                ritualCenterX + sacrificialCircleRadius,
                                ritualCenterY + sacrificialBowlPaddingVertical,
                                0),
                        new Vec3i(
                                ritualCenterX + sacrificialBowlPaddingHorizontal,
                                ritualCenterY + sacrificialCircleRadius,
                                0),
                        new Vec3i(
                                ritualCenterX - sacrificialCircleRadius,
                                ritualCenterY - sacrificialBowlPaddingVertical,
                                0))
                .collect(Collectors.toList());
    }

    private static void drawStringCentered(GuiGraphics guiGraphics, Font font, Component text, int x, int y) {
        guiGraphics.drawString(font, text, (int) (x - font.width(text) / 2.0f), y, 0x404040, false);
    }
}
