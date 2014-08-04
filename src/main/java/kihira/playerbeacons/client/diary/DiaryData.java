package kihira.playerbeacons.client.diary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class DiaryData {

    public static final List<DiaryEntry> entries = new ArrayList<DiaryEntry>(); //TODO sorted

    static {
        entries.add(new DiaryEntry("day1").addDiaryPages(new DiaryText("test")));
    }
}
