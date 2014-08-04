package kihira.playerbeacons.client.diary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.client.gui.GuiDiary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

@SideOnly(Side.CLIENT)
public class DiaryText extends DiaryPage {

    public DiaryText(String name) {
        super(name);
    }

    @Override
    public void drawScreen(GuiDiary diary, int width, int height, boolean isLeftPage) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiDiary.pageTexture);
        if (isLeftPage) diary.drawTexturedModalRect(diary.getGuiLeft(), diary.getGuiTop(), 0, 0, diary.guiWidth / 2, 173);
        else diary.drawTexturedModalRect(diary.getGuiLeft() + 128, diary.getGuiTop(), 128, 0, diary.guiWidth / 2, 173);
    }

    @Override
    public void onButtonClick(GuiButton button) {

    }

    @Override
    public void onEntryLoad(DiaryEntry entry) {

    }
}
