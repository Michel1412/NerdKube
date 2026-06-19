package br.com.nerdskube.integration.jei.category;

import br.com.nerdskube.integration.jei.NerdKubeRecipeTypes;
import br.com.nerdskube.integration.jei.recipe.ProgressionCraftRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ProgressionCraftCategory extends AbstractRecipeCategory<ProgressionCraftRecipe> {
    public static final Component TITLE = Component.translatable("jei.category.nerdkube.progression_craft");

    private static final int SLOT = 18;
    private static final int ORIGIN_X = 12;
    private static final int ORIGIN_Y = 12;

    public ProgressionCraftCategory(IGuiHelper guiHelper) {
        super(NerdKubeRecipeTypes.PROGRESSION_CRAFT, TITLE,
                guiHelper.createDrawableItemStack(new ItemStack(Items.CRAFTING_TABLE)),
                116, 74);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ProgressionCraftRecipe recipe, IFocusGroup focuses) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                ItemStack stack = recipe.grid().get(index);
                if (!stack.isEmpty()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, ORIGIN_X + col * SLOT, ORIGIN_Y + row * SLOT)
                            .addItemStack(stack);
                }
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 86, 24).addItemStack(recipe.output());
    }
}
