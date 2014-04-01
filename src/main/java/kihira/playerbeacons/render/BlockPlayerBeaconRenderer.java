package kihira.playerbeacons.render;

import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.proxy.ClientProxy;
import kihira.playerbeacons.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BlockPlayerBeaconRenderer extends TileEntitySpecialRenderer {

	private final ModelPlayerBeacon playerBeaconModel;
	private final ModelSantaHat santaHatModel;
    private final ModelSkull modelSkull;

    private float rotationPitch;
    private float rotationYaw;
    private float prevRotationPitch;
    private float prevRotationYaw;

	public BlockPlayerBeaconRenderer() {
		playerBeaconModel = new ModelPlayerBeacon();
		santaHatModel = new ModelSantaHat();
        modelSkull = new ModelSkull();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
        TileEntityPlayerBeacon playerBeacon = (TileEntityPlayerBeacon) tileentity;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

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

		if (PlayerBeacons.isChristmas && tileentity.getWorldObj().getBlock(tileentity.xCoord, tileentity.yCoord + 1, tileentity.zCoord) == Blocks.skull) {
			TileEntitySkull tileEntitySkull = (TileEntitySkull) tileentity.getWorldObj().getTileEntity(tileentity.xCoord, tileentity.yCoord + 1, tileentity.zCoord);
			bindTexture(ClientProxy.santaHatTexture);
			GL11.glScalef(1F, 1F, 1F);
			GL11.glTranslatef(0F, 0.27F, 0F);
			GL11.glRotatef((tileEntitySkull.func_145906_b() * 360) / 16.0F, 0F, 1F, 0F);
			santaHatModel.render(null, 0.0F, 0.0F, 0.0F, 0F, 0.0F, 0.0625F);
		}

        //Render Skull
        if (!playerBeacon.getOwner().equals(" ")) {
            EntityPlayer player = tileentity.getWorldObj().getClosestPlayer(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, 30D);
            if (player != null) faceEntity(player, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
            bindTexture(this.getSkullTexture(playerBeacon.getOwner()));
            GL11.glTranslated(0, 0.55D, 0);
            GL11.glRotatef(this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) + 180, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch), 1.0F, 0.0F, 0.0F);
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

    private void faceEntity(EntityLivingBase par1Entity, double posX, double posY, double posZ) {
        double d0 = par1Entity.posX - posX;
        double d2 = par1Entity.posZ - posZ;
        double d1 = par1Entity.posY - (posY) + (double)par1Entity.getEyeHeight() - 1.1F;

        double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
        float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
        this.rotationPitch = this.updateRotation(this.rotationPitch, f3, 1F);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f2, 1F);
    }

    private float updateRotation(float currRot, float intendedRot, float maxInc) {
        float f3 = MathHelper.wrapAngleTo180_float(intendedRot - currRot);
        if (f3 > maxInc) f3 = maxInc;
        if (f3 < -maxInc) f3 = -maxInc;
        return currRot + f3;
    }
}
