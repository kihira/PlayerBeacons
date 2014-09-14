package kihira.playerbeacons.client.diary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.client.diary.pages.DiaryPageRecipe;
import kihira.playerbeacons.client.diary.pages.DiaryPageText;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class DiaryData {

    public static final List<DiaryEntry> entries = new ArrayList<DiaryEntry>();

    static {
        entries.add(new DiaryEntry("entry.day1.title").addDiaryPages(new DiaryPageText("page.day1.0.text", "page.day1.0.title")));
        entries.add(new DiaryEntry("entry.day2.title").addDiaryPages(new DiaryPageRecipe("test", "test", (IRecipe) CraftingManager.getInstance().getRecipeList().get(1)), new DiaryPageText("test3", "test3")));
    }
}
