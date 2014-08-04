package kihira.playerbeacons.client.gui.button;

import kihira.playerbeacons.client.gui.GuiDiary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class GuiButtonNavigation extends GuiButton {

    private final boolean nextPage;

    public GuiButtonNavigation(int id, int posX, int posY, boolean nextPage) {
        super(id, posX, posY, 9, 12, "");
        this.nextPage = nextPage;
    }

    @Override
    public void drawButton(Minecraft mc, int width, int height) {
        boolean isMouseOver = width >= this.xPosition && height >= this.yPosition && width < this.xPosition + this.width && height < this.yPosition + this.height;
        if (this.visible && isMouseOver) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiDiary.pageTexture);
            int u = 0;
            int v = 244;

            if (this.nextPage) {
                u += 10;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, 9, 12);
        }
    }
}
