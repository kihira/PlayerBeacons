package playerbeacons.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import playerbeacons.proxy.ClientProxy;
import playerbeacons.tileentity.TileEntityPylon;

public class PylonRenderer extends TileEntitySpecialRenderer {

	private ModelPylonBase modelPylonBase;
	private ModelPylon modelPylon;

	public PylonRenderer() {
		modelPylon = new ModelPylon();
		modelPylonBase = new ModelPylonBase();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
		TileEntityPylon tileEntityPylon = (TileEntityPylon) tileentity;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslated(x + 0.5d, y + 1.5001d, z + 0.5d);
		GL11.glRotatef(180F, 0F, 0F, 1F);
		GL11.glScalef(1F, 1F, 1F);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if (tileEntityPylon.isPylonBase()) {
			func_110628_a(ClientProxy.pylonTextureBase);
			modelPylonBase.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		else {
			func_110628_a(ClientProxy.pylonTexture);
			modelPylon.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
