package playerbeacons.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import playerbeacons.proxy.ClientProxy;

public class PlayerBeaconRenderer extends TileEntitySpecialRenderer {

	private ModelPlayerBeacon model;

	public PlayerBeaconRenderer() {
		model = new ModelPlayerBeacon();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		func_110628_a(ClientProxy.playerBeaconTexture);
		GL11.glTranslated(x + 0.5d, y + 1.7501d, z + 0.5d);
		GL11.glRotatef(180F, 0F, 0F, 1F);
		GL11.glScalef(1.2F, 1.2F, 1.2F);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		model.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
