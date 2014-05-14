package kihira.playerbeacons.client.render;

import kihira.playerbeacons.proxy.ClientProxy;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BlockPlayerBeaconRenderer extends TileEntitySpecialRenderer {

	private final ModelPlayerBeacon playerBeaconModel;
	private final ModelSantaHat santaHatModel;
    private final ModelSkull modelSkull;

	public BlockPlayerBeaconRenderer() {
		playerBeaconModel = new ModelPlayerBeacon();
		santaHatModel = new ModelSantaHat();
        modelSkull = new ModelSkull();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
        TileEntityPlayerBeacon playerBeacon = (TileEntityPlayerBeacon) tileentity;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		bindTexture(ClientProxy.playerBeaconTexture);
		GL11.glTranslated(x + 0.5d, y + 1.8001d, z + 0.5d);
		GL11.glRotatef(180F, 0F, 0F, 1F);
		GL11.glScalef(1.2F, 1.2F, 1.2F);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		playerBeaconModel.render(null, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, 0.0F, partialTickTime, 0.0625F);

        /*
		if (PlayerBeacons.isChristmas && tileentity.getWorldObj().getBlock(tileentity.xCoord, tileentity.yCoord + 1, tileentity.zCoord) == Blocks.skull) {
			TileEntitySkull tileEntitySkull = (TileEntitySkull) tileentity.getWorldObj().getTileEntity(tileentity.xCoord, tileentity.yCoord + 1, tileentity.zCoord);
			bindTexture(ClientProxy.santaHatTexture);
			GL11.glScalef(1F, 1F, 1F);
			GL11.glTranslatef(0F, 0.27F, 0F);
			GL11.glRotatef((tileEntitySkull.func_145906_b() * 360) / 16.0F, 0F, 1F, 0F);
			santaHatModel.render(null, 0.0F, 0.0F, 0.0F, 0F, 0.0F, 0.0625F);
		}
		*/

        //Render Skull
        if (!playerBeacon.getOwner().equals(" ")) {
            bindTexture(this.getSkullTexture(playerBeacon.getOwner()));
            GL11.glTranslated(0, 0.55D, 0);
            GL11.glRotatef(playerBeacon.prevHeadRotationYaw + (playerBeacon.headRotationYaw - playerBeacon.prevHeadRotationYaw) + 180, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(playerBeacon.prevHeadRotationPitch + (playerBeacon.headRotationPitch - playerBeacon.headRotationPitch), 1.0F, 0.0F, 0.0F);
            modelSkull.renderWithoutRotation(0.0625F);
        }

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(x, y + 1, z);
		GL11.glPopMatrix();
	}

    private ResourceLocation getSkullTexture(String name) {
        ResourceLocation resourcelocation = AbstractClientPlayer.getLocationSkull(name);
        AbstractClientPlayer.getDownloadImageSkin(resourcelocation, name);
        return resourcelocation;
    }
}
