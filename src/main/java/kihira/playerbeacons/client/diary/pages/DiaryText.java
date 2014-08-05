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

        //Draw the page background
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiDiary.pageTexture);
        diary.drawTexturedModalRect(diary.getGuiLeft() + leftOffset, diary.getGuiTop(), leftOffset, 0, diary.guiWidth / 2, 173);

        //Now the test itself
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawSplitString(StatCollector.translateToLocal("page." + this.pageName + ".text"), diary.getGuiLeft() + leftOffset + (isLeftPage ? 17 : 5), diary.getGuiTop() + 20, 120, -1);
    }

    @Override
    public void onButtonClick(GuiButton button) {

    }

    @Override
    public void onEntryLoad(DiaryEntry entry) {

    }
}
