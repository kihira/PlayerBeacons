package kihira.playerbeacons.client.diary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.client.gui.GuiDiary;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

/**
 * This is a page of an entry. A base class that should be extended to what is needed
 */
@SideOnly(Side.CLIENT)
public abstract class DiaryPage {

    private final String name;

    public DiaryPage(String name) {
        this.name = name;
    }

    public abstract void drawScreen(GuiDiary diary, int width, int height, boolean isLeftPage);

    public abstract void onButtonClick(GuiButton button);

    public abstract void onEntryLoad(DiaryEntry entry);

    public String getName() {
        return StatCollector.translateToLocal("page." + this.name + ".name");
    }
}
