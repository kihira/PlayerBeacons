/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.playerbeacons.client.diary.pages;

import kihira.playerbeacons.client.diary.DiaryEntry;
import kihira.playerbeacons.client.diary.DiaryPage;
import kihira.playerbeacons.client.gui.GuiDiary;
import net.minecraft.client.gui.GuiButton;

public class DiaryContents extends DiaryPage {

    public DiaryContents(String name, DiaryEntry ... entries) {
        super(name, "");
    }

    @Override
    public void drawScreen(GuiDiary diary, int width, int height, boolean isLeftPage) {

    }

    @Override
    public void onButtonClick(GuiButton button) {

    }

    @Override
    public void onEntryLoad(DiaryEntry entry) {

    }
}
