package kihira.playerbeacons.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.client.diary.DiaryData;
import kihira.playerbeacons.client.diary.DiaryEntry;
import kihira.playerbeacons.client.diary.DiaryPage;
import kihira.playerbeacons.client.gui.button.GuiButtonEntry;
import kihira.playerbeacons.client.gui.button.GuiButtonNavigation;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiDiary extends GuiScreen {

    public final int guiWidth = 256;
    public final int guiHeight = 180;
    private int guiLeft, guiTop;

    private int currentIndex = 0;

    public static final ResourceLocation bookCoverGuiTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/gui/diary/book_cover.png");
    public static final ResourceLocation pageTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/gui/diary/book_pages.png");
    public static final ResourceLocation pageTitleTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/gui/diary/book_title.png");

    private GuiButtonNavigation prevPage;
    private GuiButtonNavigation nextPage;
    private List<DiaryPage> pages;

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        this.buttonList.clear();

        this.guiLeft = this.width / 2 - this.guiWidth / 2;
        this.guiTop = this.height / 2 - this.guiHeight / 2;

        //Navigation
        this.buttonList.add(this.prevPage = new GuiButtonNavigation(0, this.guiLeft + 13, this.guiTop + this.guiHeight - 22, false)); //Prev
        this.buttonList.add(this.nextPage = new GuiButtonNavigation(1, this.guiLeft + this.guiWidth - 22, this.guiTop + this.guiHeight - 22, true)); //Next

        //Entries
        compilePages();

        updateButtons();
    }

    @Override
    public void drawScreen(int width, int height, float p_73863_3_) {
        this.fontRendererObj.setUnicodeFlag(true);
        this.mc.getTextureManager().bindTexture(bookCoverGuiTexture);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.guiWidth, this.guiHeight);

        //Not the content pages. NOTE: 4 is used as an "offset" of when actual entries start
        if (this.currentIndex >= 4) {
            for (int i = 0; i < 2; i++) {
                if (pages.size() > currentIndex + i - 4) {
                    DiaryPage page = this.pages.get(this.currentIndex + i - 4);
                    if (page != null) {
                        page.drawScreen(this, width, height, i == 0);
                    }
                }
            }
        }
        else {
            //Draw the cover page
            if (this.currentIndex == 0) {
                this.mc.getTextureManager().bindTexture(pageTitleTexture);
                this.drawTexturedModalRect(this.guiLeft + 128, this.guiTop, 128, 0, this.guiWidth / 2, 173);
            }
            //Draw the contents
            else {
                this.mc.getTextureManager().bindTexture(pageTexture);
                this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.guiWidth, 173);
                //FontHelper.drawCenteredString("Contents", this.guiLeft + (this.guiWidth / 4) + 5, this.guiTop + 15, FancyFont.fontOther, 1F, 1F, 0F, 0F, 0F, 1F);
                GL11.glPushMatrix();
                GL11.glTranslatef(this.guiLeft + (this.guiWidth / 4) + 5, this.guiTop + 15, 0);
                GL11.glScalef(1.3F, 1.3F, 0F);
                this.fontRendererObj.setUnicodeFlag(false);
                this.fontRendererObj.drawString(EnumChatFormatting.BLACK + "Contents", -this.fontRendererObj.getStringWidth("contents") / 2, 0, -1);
                this.fontRendererObj.setUnicodeFlag(true);
                GL11.glPopMatrix();
            }
        }

        super.drawScreen(width, height, p_73863_3_);
        this.fontRendererObj.setUnicodeFlag(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == this.prevPage) {
            if (this.currentIndex != 0) {
                this.adjustIndex(-2);
            }
        }
        if (button == this.nextPage) {
            this.adjustIndex(2);
        }

        //Entry
        if (button.id >= 500) {
            GuiButtonEntry buttonEntry = (GuiButtonEntry) button;
            //We want currentIndex to always be even
            this.currentIndex = buttonEntry.index + 4 - (buttonEntry.index % 2);
        }

        this.updateButtons();
    }

    @SuppressWarnings("unchecked")
    private void compilePages() {
        ArrayList<DiaryPage> pages = new ArrayList<DiaryPage>();
/*        pages.add(null);
        pages.add(new DiaryCover());
        pages.add(new DiaryContents());*/
        int index = 0;
        int buttonOffset = 0;
        int buttonID = 500;
        for (DiaryEntry entry : DiaryData.entries) {
            pages.addAll(entry.getPages());

            //Add contents button
            GuiButtonEntry buttonEntry = new GuiButtonEntry(buttonID, this.getGuiLeft() + 20, this.getGuiTop() + 35 + (buttonOffset * 10), entry, index);
            buttonEntry.visible = buttonEntry.enabled = false;
            buttonList.add(buttonEntry);

            buttonOffset++;
            buttonID++;
            index += entry.getPages().size();
            if (buttonOffset > 13) {
                buttonOffset = 0;
            }
        }

        this.pages = Collections.unmodifiableList(pages);
    }

    private void updateButtons() {
        //Prev button
        if (this.currentIndex < 2) this.prevPage.enabled = this.prevPage.visible = false;
        else this.prevPage.enabled = this.prevPage.visible = true;

        //Next button. + 2 to check if there are entries ahead
        if (this.currentIndex < 4 || this.pages.size() > this.currentIndex + 2 - 4) {
            this.nextPage.enabled = this.nextPage.visible = true;
        }
        else this.nextPage.enabled = this.nextPage.visible = false;

        //Contents
        for (Object button : this.buttonList) {
            if (button instanceof GuiButtonEntry) {
                GuiButtonEntry guiButtonEntry = (GuiButtonEntry) button;
                guiButtonEntry.enabled = guiButtonEntry.visible = this.currentIndex == 2;
            }
        }
    }

    public void adjustIndex(int change) {
        this.currentIndex = MathHelper.clamp_int(this.currentIndex += change, 0, Integer.MAX_VALUE);
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public int getGuiLeft() {
        return this.guiLeft;
    }

    public int getGuiTop() {
        return this.guiTop;
    }
}
