package br.com.nerdskube.integration.jei.category;

import br.com.nerdskube.integration.jei.NerdKubeRecipeTypes;
import br.com.nerdskube.integration.jei.recipe.EndgameRitualRecipe;
import br.com.nerdskube.registry.ModBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class EndgameRitualCategory extends AbstractRecipeCategory<EndgameRitualRecipe> {
    public static final Component TITLE = Component.translatable("jei.category.nerdkube.endgame_ritual");

    public EndgameRitualCategory(IGuiHelper guiHelper) {
        super(NerdKubeRecipeTypes.ENDGAME_RITUAL, TITLE,
                guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.CUBE_MAKER.get())),
                150, 110);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EndgameRitualRecipe recipe, IFocusGroup focuses) {
        int cx = 66;
        int cy = 44;

        addPedestal(builder, cx, cy - 28, recipe.northPedestal(), recipe.northOffering());
        addPedestal(builder, cx, cy + 28, recipe.southPedestal(), recipe.southOffering());
        addPedestal(builder, cx + 28, cy, recipe.eastPedestal(), recipe.eastOffering());
        addPedestal(builder, cx - 28, cy, recipe.westPedestal(), recipe.westOffering());

        builder.addSlot(RecipeIngredientRole.INPUT, cx, cy).addItemStack(recipe.crafter());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 46).addItemStack(recipe.output());
    }

    private static void addPedestal(
            IRecipeLayoutBuilder builder,
            int x,
            int y,
            ItemStack pedestal,
            ItemStack offering) {
        builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStack(pedestal);
        builder.addSlot(RecipeIngredientRole.INPUT, x, y - 14).addItemStack(offering);
    }
}
