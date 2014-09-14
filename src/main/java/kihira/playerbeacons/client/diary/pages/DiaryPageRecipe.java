/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.playerbeacons.client.diary.pages;

import kihira.playerbeacons.client.diary.DiaryEntry;
import kihira.playerbeacons.client.diary.DiaryPage;
import kihira.playerbeacons.client.gui.GuiDiary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class DiaryPageRecipe extends DiaryPage {

    public final IRecipe recipe;

    public DiaryPageRecipe(String name, String pageTitle, IRecipe recipe) {
        super(name, pageTitle);
        this.recipe = recipe;
    }

    @Override
    public void drawScreen(GuiDiary diary, int width, int height, boolean isLeftPage) {
        //Calculate offset
        int leftOffset = (isLeftPage ? 0 : 128);
        int mainTextTopOffset = (StatCollector.canTranslate(this.getUnlocalisedTitle()) ? 15 : 0);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        //Draw the page background
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiDiary.pageTexture);
        diary.drawTexturedModalRect(diary.getGuiLeft() + leftOffset, diary.getGuiTop(), leftOffset, 0, diary.guiWidth / 2, 173);

        //Title
        if (mainTextTopOffset != 0) {
            fontRenderer.setUnicodeFlag(false);
            String s = StatCollector.translateToLocal(this.getUnlocalisedTitle());
            fontRenderer.drawString(s, diary.getGuiLeft() + leftOffset + (isLeftPage ? 64 : 5) - (fontRenderer.getStringWidth(s) / 2), diary.getGuiTop() + 15, 1973019);
            fontRenderer.setUnicodeFlag(true);
        }

        //Recipe
        renderRecipe(diary.getGuiLeft() + leftOffset + 32, diary.getGuiTop() + 20, recipe);

        //Lower Text
        fontRenderer.drawSplitString(StatCollector.translateToLocal(this.pageName), diary.getGuiLeft() + leftOffset + (isLeftPage ? 17 : 5), diary.getGuiTop() + 12 + mainTextTopOffset, 108, 1973019);
        GL11.glColor3f(1F, 1F, 1F);
    }

    private void renderRecipe(int xPos, int yPos, IRecipe recipe) {
        if (recipe instanceof ShapedRecipes) {
            ShapedRecipes shapedRecipe = (ShapedRecipes) recipe;
            for (int recipeWidth = 0; recipeWidth < shapedRecipe.recipeWidth; recipeWidth++) {
                for (int recipeHeight = 0; recipeHeight < shapedRecipe.recipeHeight; recipeHeight++) {
                    renderItemStack(shapedRecipe.recipeItems[recipeHeight * shapedRecipe.recipeWidth + recipeWidth], xPos + (16 * recipeWidth), yPos + (16 * recipeHeight));
                }
            }
        }

        renderItemStack(recipe.getRecipeOutput(), xPos + 16, yPos + 48);
    }

    private void renderItemStack(ItemStack itemStack, int xPos, int yPos) {
        RenderItem renderItem = new RenderItem();
        FontRenderer font = null;

        GL11.glPushMatrix();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);

        if (itemStack != null) font = itemStack.getItem().getFontRenderer(itemStack);
        if (font == null) font = Minecraft.getMinecraft().fontRenderer;
        renderItem.renderItemAndEffectIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), itemStack, xPos, yPos);
        renderItem.renderItemOverlayIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), itemStack, xPos, yPos);

        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }

    @Override
    public void onButtonClick(GuiButton button) {

    }

    @Override
    public void onEntryLoad(DiaryEntry entry) {

    }
}
