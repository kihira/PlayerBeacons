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

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiDiary extends GuiScreen {

    public final int guiWidth = 256;
    public final int guiHeight = 180;
    private int guiLeft, guiTop;

    private DiaryEntry currentEntry;
    private int currentIndex = 0;

    public static final ResourceLocation bookCoverGuiTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/gui/diary/book_cover.png");
    public static final ResourceLocation pageTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/gui/diary/book_pages.png");
    public static final ResourceLocation pageTitleTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/gui/diary/book_title.png");

    private GuiButtonNavigation prevPage;
    private GuiButtonNavigation nextPage;

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
        this.addEntries();

        this.updateButtons();
    }

    @Override
    public void drawScreen(int width, int height, float p_73863_3_) {
        this.fontRendererObj.setUnicodeFlag(true);
        this.mc.getTextureManager().bindTexture(bookCoverGuiTexture);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.guiWidth, this.guiHeight);

        if (this.currentEntry != null) {
            List<DiaryPage> pages = this.currentEntry.getPages();
            for (int i = 0; i < 2; i++) {
                if (pages.size() > (this.currentIndex + i)) {
                    DiaryPage page = this.currentEntry.getPages().get(this.currentIndex + i);
                    if (page != null) page.drawScreen(this, width, height, i == 0);
                }
                else {
                    //TODO show start of next entry
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
            //If we're already at the first page, go back to contents TODO Go back to earlier entry
            if (this.currentIndex == 0) {
                this.setCurrentEntry(null);
            }
            this.adjustIndex(-2);
        }
        if (button == this.nextPage) this.adjustIndex(2);

        //Entry
        if (button.id >= 500) {
            GuiButtonEntry buttonEntry = (GuiButtonEntry) button;
            this.setCurrentEntry(buttonEntry.diaryEntry);
        }

        this.updateButtons();
    }

    @SuppressWarnings("unchecked")
    private void addEntries() {
        int index = 0;
        for (DiaryEntry entry : DiaryData.entries) {
            GuiButtonEntry buttonEntry = new GuiButtonEntry(500 + index, this.guiLeft + 20, this.guiTop + 35 + (index * 10), entry);
            buttonEntry.visible = buttonEntry.enabled = false;
            this.buttonList.add(buttonEntry);
            index++;
        }
    }

    private void updateButtons() {
        //Prev button
        if (this.currentEntry == null && this.currentIndex < 2) this.prevPage.enabled = this.prevPage.visible = false;
        else this.prevPage.enabled = this.prevPage.visible = true;

        //Next button
/*        if (this.currentIndex > 0) this.nextPage.enabled = this.nextPage.visible = true;
        else this.nextPage.enabled = this.nextPage.visible = false;*/

        //Contents
        for (Object button : this.buttonList) {
            if (button instanceof GuiButtonEntry) {
                GuiButtonEntry guiButtonEntry = (GuiButtonEntry) button;
                guiButtonEntry.enabled = guiButtonEntry.visible = (this.currentEntry == null && this.currentIndex > 0);
            }
        }
    }

    public void adjustIndex(int change) {
        this.currentIndex = MathHelper.clamp_int(this.currentIndex += change, 0, Integer.MAX_VALUE);
    }

    public void setCurrentEntry(DiaryEntry entry) {
        this.currentEntry = entry;
        this.currentIndex = 0;
    }

    public DiaryEntry getDiaryEntry() {
        return this.currentEntry;
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
