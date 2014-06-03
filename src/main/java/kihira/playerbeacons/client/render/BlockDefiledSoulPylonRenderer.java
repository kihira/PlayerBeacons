package kihira.playerbeacons.client.render;

import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.common.tileentity.TileEntityDefiledSoulPylon;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class BlockDefiledSoulPylonRenderer extends TileEntitySpecialRenderer {

	private final ModelPylonBase modelPylonBase;
	private final ModelPylon modelPylon;
	private final ModelCrystalPort modelCrystalPort;

	public BlockDefiledSoulPylonRenderer() {
		modelPylon = new ModelPylon();
		modelPylonBase = new ModelPylonBase();
		modelCrystalPort = new ModelCrystalPort();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
		TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) tileentity;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if (tileEntityDefiledSoulPylon.isPylonBase()) {
			GL11.glTranslated(x + 0.5d, y + 1.5001d, z + 0.5d);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glScalef(1F, 1F, 1F);
			bindTexture(ClientProxy.pylonTextureBase);
            this.modelPylonBase.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		else if (tileEntityDefiledSoulPylon.isPylonTop()) {
			GL11.glTranslated(x + 0.5d, y - 0.5d, z + 0.5d);
			GL11.glRotatef(0F, 0F, 0F, 1F);
			GL11.glScalef(1F, 1F, 1F);
			bindTexture(ClientProxy.pylonTextureBase);
            this.modelPylonBase.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		else {
			GL11.glTranslated(x + 0.5d, y + 1.5001d, z + 0.5d);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glScalef(1F, 1F, 1F);
            //Render the pylon
			bindTexture(ClientProxy.pylonTexture);
            this.modelPylon.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            //Then the crystal port
            this.bindTexture(ClientProxy.pylonCrystalPortTexture);
            this.modelCrystalPort.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            //Then the overlay
			ItemStack itemStack = tileEntityDefiledSoulPylon.getStackInSlot(0);
			if (itemStack != null && itemStack.getItem() instanceof ICrystal) {
                double[] rgba = ((ICrystal) itemStack.getItem()).getRGBA();
				GL11.glColor4d(rgba[0], rgba[1], rgba[2], rgba[3]);
			}
            else {
                GL11.glColor4d(0.8F, 0.8F, 0.8F, 1F);
            }
            this.bindTexture(ClientProxy.pylonCrystalPortOverlayTexture);
            this.modelCrystalPort.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
