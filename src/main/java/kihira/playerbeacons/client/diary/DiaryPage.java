package kihira.playerbeacons.client.diary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.client.gui.GuiDiary;
import net.minecraft.client.gui.GuiButton;

/**
 * This is a page of an entry. A base class that should be extended to what is needed
 */
@SideOnly(Side.CLIENT)
public abstract class DiaryPage {

    public final String pageName;
    public final String pageTitle;

    public DiaryPage(String name, String pageTitle) {
        this.pageName = name;
        this.pageTitle = pageTitle;
    }

    public abstract void drawScreen(GuiDiary diary, int width, int height, boolean isLeftPage);

    public abstract void onButtonClick(GuiButton button);

    public abstract void onEntryLoad(DiaryEntry entry);

    public String getUnlocalisedTitle() {
        return this.pageTitle;
    }
}
