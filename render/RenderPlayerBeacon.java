package playerbeacons.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPlayerBeacon extends TileEntitySpecialRenderer {

	private ResourceLocation texture = new ResourceLocation("playerbeacon", "textures/model/playerbeacon.png");
	private ModelPlayerBeacon model = new ModelPlayerBeacon();
	private int renderID;

	public RenderPlayerBeacon(int renderID) {
		this.renderID = renderID;
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		func_110628_a(texture);
		GL11.glTranslated(x + 0.5d, y + 1.5001d, z + 0.5d);
		GL11.glScalef(1.0F, -1F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glTranslatef((float) x, (float) y, (float) z);
		model.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
