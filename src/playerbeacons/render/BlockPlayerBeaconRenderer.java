package playerbeacons.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import org.lwjgl.opengl.GL11;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.proxy.ClientProxy;

public class BlockPlayerBeaconRenderer extends TileEntitySpecialRenderer {

	private final ModelPlayerBeacon playerBeaconModel;
	private final ModelSantaHat santaHatModel;

	public BlockPlayerBeaconRenderer() {
		playerBeaconModel = new ModelPlayerBeacon();
		santaHatModel = new ModelSantaHat();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		bindTexture(ClientProxy.playerBeaconTexture);
		GL11.glTranslated(x + 0.5d, y + 1.8001d, z + 0.5d);
		GL11.glRotatef(180F, 0F, 0F, 1F);
		GL11.glScalef(1.2F, 1.2F, 1.2F);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		playerBeaconModel.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		if (PlayerBeacons.isChristmas && tileentity.worldObj.getBlockId(tileentity.xCoord, tileentity.yCoord + 1, tileentity.zCoord) == Block.skull.blockID) {
			TileEntitySkull tileEntitySkull = (TileEntitySkull) tileentity.worldObj.getBlockTileEntity(tileentity.xCoord, tileentity.yCoord + 1, tileentity.zCoord);
			bindTexture(ClientProxy.santaHatTexture);
			GL11.glScalef(1F, 1F, 1F);
			GL11.glTranslatef(0F, 0.27F, 0F);
			GL11.glRotatef((tileEntitySkull.func_82119_b() * 360) / 16.0F, 0F, 1F, 0F);
			santaHatModel.render(null, 0.0F, 0.0F, 0.0F, 0F, 0.0F, 0.0625F);
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(x, y + 1, z);
		GL11.glPopMatrix();
	}
}
