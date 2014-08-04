package kihira.playerbeacons.client.diary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.StatCollector;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a specific entry within the diary. This would usually be something like "day 23" and can contain multiple pages
 */
@SideOnly(Side.CLIENT)
public class DiaryEntry {

    /**
     * The list of pages for this entry
     */
    protected List<DiaryPage> pages = new ArrayList<DiaryPage>();

    private final String name;

    public DiaryEntry(String name) {
        this.name = name;
    }

    /**
     * Adds pages on to the end of the current pages list
     * @param diaryPages The pages
     * @return this
     */
    @SuppressWarnings("unchecked")
    public DiaryEntry addDiaryPages(DiaryPage ... diaryPages) {
        this.pages.addAll(Arrays.asList(diaryPages));

        return this;
    }

    public List<DiaryPage> getPages() {
        return this.pages;
    }

    public String getName() {
        return StatCollector.translateToLocal("entry." + name + ".name");
    }
}
