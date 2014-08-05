package kihira.playerbeacons.client.gui.button;

import kihira.playerbeacons.client.diary.DiaryEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

/**
 * This button is used to link to a specific {@link kihira.playerbeacons.client.diary.DiaryEntry}
 */
public class GuiButtonEntry extends GuiButton {

    public final DiaryEntry diaryEntry;

    public GuiButtonEntry(int id, int posX, int posY, DiaryEntry entry) {
        super(id, posX, posY, 100, 10, entry.getName());
        this.diaryEntry = entry;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean isMouseOver = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int colour = 16777120;

            fontrenderer.drawString(EnumChatFormatting.BLACK + (isMouseOver ? " " : "") + this.displayString, this.xPosition, this.yPosition + (this.height - fontrenderer.FONT_HEIGHT) / 2, colour);
        }
    }
}
