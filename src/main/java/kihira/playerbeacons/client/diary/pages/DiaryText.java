package kihira.playerbeacons.client.diary.pages;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.client.diary.DiaryEntry;
import kihira.playerbeacons.client.diary.DiaryPage;
import kihira.playerbeacons.client.gui.GuiDiary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class DiaryText extends DiaryPage {

    public DiaryText(String name) {
        super(name);
    }

    @Override
    public void drawScreen(GuiDiary diary, int width, int height, boolean isLeftPage) {
        //Calculate offset
        int leftOffset = (isLeftPage ? 0 : 128);
        int mainTextTopoffset = (StatCollector.canTranslate(this.getUnlocalisedTitle()) ? 15 : 0);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        //Draw the page background
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiDiary.pageTexture);
        diary.drawTexturedModalRect(diary.getGuiLeft() + leftOffset, diary.getGuiTop(), leftOffset, 0, diary.guiWidth / 2, 173);

        //Title
        if (mainTextTopoffset != 0) {
            fontRenderer.setUnicodeFlag(false);
            String s = StatCollector.translateToLocal(this.getUnlocalisedTitle());
            fontRenderer.drawString(s, diary.getGuiLeft() + leftOffset + (isLeftPage ? 64 : 5) - (fontRenderer.getStringWidth(s) / 2), diary.getGuiTop() + 15, 1973019);
            fontRenderer.setUnicodeFlag(true);
        }

        //Now the test itself
        fontRenderer.drawSplitString(StatCollector.translateToLocal("page." + this.pageName + ".text"), diary.getGuiLeft() + leftOffset + (isLeftPage ? 17 : 5), diary.getGuiTop() + 12 + mainTextTopoffset, 108, 1973019);
/*        GL11.glPushMatrix();
        List<String> list = fontRenderer.listFormattedStringToWidth(StatCollector.translateToLocal("page." + this.pageName + ".text"), 108);
        int offset = 0;
        for (String s : list) {
            FontHelper.drawString(s, diary.getGuiLeft() + leftOffset + (isLeftPage ? 17 : 5), diary.getGuiTop() + offset, FancyFont.fontTest, 0.3F, 0.3F, new float[] {0F, 0F, 0F, 1F});
            offset += FancyFont.fontTest.getHeight() * 0.14F;
        }
        GL11.glPopMatrix();*/
    }

    @Override
    public void onButtonClick(GuiButton button) {

    }

    @Override
    public void onEntryLoad(DiaryEntry entry) {

    }
}
