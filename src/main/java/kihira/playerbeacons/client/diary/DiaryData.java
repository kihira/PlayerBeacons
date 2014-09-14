package kihira.playerbeacons.client.diary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.client.diary.pages.DiaryPageText;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class DiaryData {

    public static final List<DiaryEntry> entries = new ArrayList<DiaryEntry>(); //TODO sorted

    static {
        entries.add(new DiaryEntry("day1").addDiaryPages(new DiaryPageText("page.day1.0.text", "page.day1.0.title")));
        entries.add(new DiaryEntry("day2").addDiaryPages(new DiaryPageText("test2", "test2"), new DiaryPageText("test3", "test3")));
    }
}
