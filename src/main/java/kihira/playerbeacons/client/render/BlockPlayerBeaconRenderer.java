package kihira.playerbeacons.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.proxy.ClientProxy;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class BlockPlayerBeaconRenderer extends TileEntitySpecialRenderer {

	private final ModelPlayerBeacon playerBeaconModel;
	//private final ModelSantaHat santaHatModel;
    private final ModelSkull modelSkull;

    private final ResourceLocation endSkyTexture = new ResourceLocation("textures/environment/end_sky.png");
    private final ResourceLocation endPortalTexture = new ResourceLocation("textures/entity/end_portal.png");
    FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);

	public BlockPlayerBeaconRenderer() {
		this.playerBeaconModel = new ModelPlayerBeacon();
		//santaHatModel = new ModelSantaHat();
        this.modelSkull = new ModelSkull();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
        TileEntityPlayerBeacon playerBeacon = (TileEntityPlayerBeacon) tileentity;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
        this.renderPortal(x, y, z);
        this.bindTexture(ClientProxy.playerBeaconTexture);
		GL11.glTranslated(x + 0.5d, y + 1.8001d, z + 0.5d);
		GL11.glRotatef(180F, 0F, 0F, 1F);
		GL11.glScalef(1.2F, 1.2F, 1.2F);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		this.playerBeaconModel.render(null, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, 0.0F, partialTickTime, 0.0625F);

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
            this.modelSkull.renderWithoutRotation(0.0625F);
        }

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(x, y + 1, z);
		GL11.glPopMatrix();
	}

    private void renderPortal(double x, double y, double z) {
        float f1 = (float)this.field_147501_a.field_147560_j;
        float f2 = (float)this.field_147501_a.field_147561_k;
        float f3 = (float)this.field_147501_a.field_147558_l;
        GL11.glDisable(GL11.GL_LIGHTING);
        Random random = new Random(31100L);
        float f4 = 0.75F;

        for (int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);

            if (i == 0) {
                this.bindTexture(this.endSkyTexture);
                f7 = 0.1F;
                f5 = 65.0F;
                f6 = 0.225F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (i == 1) {
                this.bindTexture(this.endPortalTexture);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                f6 = 0.3F;
            }

            float f8 = (float)(-(y + (double)f4));
            float f9 = f8 + ActiveRenderInfo.objectY;
            float f10 = f8 + f5 + ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 += (float)(y + (double)f4);
            GL11.glTranslatef(f1, f11, f3);
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, this.func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, this.func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, this.func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, this.func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glTranslatef(0F, 2F, 0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f1, -f3, -f2);
            f9 = f8 + ActiveRenderInfo.objectY;
            GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -f2);
            Tessellator tessellator = Tessellator.instance;
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;

            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x + 0.3, y + (double)f4 - 0.7, z + 0.27);
            tessellator.addVertex(x + 0.3, y + (double)f4 - 0.7 + 0.5D, z + 0.27);
            tessellator.addVertex(x + 0.2 + 0.5D, y + (double)f4 - 0.7 + 0.5D, z + 0.27);
            tessellator.addVertex(x + 0.2 + 0.5D, y + (double)f4 - 0.7, z + 0.27);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x + 0.2 + 0.5D, y + (double)f4 - 0.7, z + 0.75);
            tessellator.addVertex(x + 0.2 + 0.5D, y + (double)f4 - 0.7 + 0.5D, z + 0.75);
            tessellator.addVertex(x + 0.3, y + (double)f4 - 0.7 + 0.5D, z + 0.75);
            tessellator.addVertex(x + 0.3, y + (double)f4 - 0.7, z + 0.75);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x + 0.27, y + (double)f4 - 0.7, z + 0.25 + 0.5D);
            tessellator.addVertex(x + 0.27, y + (double)f4 - 0.7 + 0.5D, z + 0.25 + 0.5D);
            tessellator.addVertex(x + 0.27, y + (double)f4 - 0.7 + 0.5D, z + 0.25);
            tessellator.addVertex(x + 0.27, y + (double)f4 - 0.7, z + 0.25);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x + 0.75, y + (double)f4 - 0.7, z + 0.25);
            tessellator.addVertex(x + 0.75, y + (double)f4 - 0.7 + 0.5D, z + 0.25);
            tessellator.addVertex(x + 0.75, y + (double)f4 - 0.7 + 0.5D, z + 0.25 + 0.5D);
            tessellator.addVertex(x + 0.75, y + (double)f4 - 0.7, z + 0.25 + 0.5D);
            tessellator.draw();

            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
        this.field_147528_b.clear();
        this.field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.field_147528_b.flip();
        return this.field_147528_b;
    }

    private ResourceLocation getSkullTexture(String name) {
        ResourceLocation resourcelocation = AbstractClientPlayer.getLocationSkull(name);
        AbstractClientPlayer.getDownloadImageSkin(resourcelocation, name);
        return resourcelocation;
    }
}
