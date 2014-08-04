package kihira.playerbeacons.client.diary;

import kihira.playerbeacons.client.gui.GuiDiary;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

/**
 * A page that creates a fancy splash/title page
 */
public class DiaryPageSplash extends DiaryPage {

    private final ResourceLocation pageTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/book_title.png");

    public DiaryPageSplash(String name) {
        super(name);
    }

    @Override
    public void drawScreen(GuiDiary diary, int width, int height, boolean isLeftPage) {
        diary.mc.getTextureManager().bindTexture(this.pageTexture);
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
