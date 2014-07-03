package kihira.playerbeacons.client.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Calendar;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class BlockSkullRenderer extends TileEntitySkullRenderer {

	private final ModelSkull modelSkull = new ModelSkull();

    @Override
    public void renderTileEntityAt(TileEntitySkull p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
        this.render(p_147500_1_, (float) p_147500_2_, (float) p_147500_4_, (float) p_147500_6_, p_147500_1_.getBlockMetadata() & 7, (float) (p_147500_1_.func_145906_b() * 360) / 16.0F, p_147500_1_.func_145904_a(), p_147500_1_.func_152108_a());
    }

	//@Override
	//public void func_152674_a(float par1, float par2, float par3, int par4, float par5, int par6, GameProfile gameProfile) {
    public void render(TileEntitySkull skull, float par1, float par2, float par3, int par4, float par5, int par6, GameProfile gameProfile) {
		//We only need to override the player head
		if (par6 == 3) {
			ResourceLocation resourcelocation = AbstractClientPlayer.locationStevePng;
            if (gameProfile != null) {
                Minecraft minecraft = Minecraft.getMinecraft();
                Map map = minecraft.func_152342_ad().func_152788_a(gameProfile);

                if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                    resourcelocation = minecraft.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                }
            }
			this.bindTexture(resourcelocation);
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_CULL_FACE);
			if (par4 != 1) {
				switch (par4) {
					case 2:
						GL11.glTranslatef(par1 + 0.5F, par2 + 0.25F, par3 + 0.74F);
						break;
					case 3:
						GL11.glTranslatef(par1 + 0.5F, par2 + 0.25F, par3 + 0.26F);
						par5 = 180.0F;
						break;
					case 4:
						GL11.glTranslatef(par1 + 0.74F, par2 + 0.25F, par3 + 0.5F);
						par5 = 270.0F;
						break;
					case 5:
					default:
						GL11.glTranslatef(par1 + 0.26F, par2 + 0.25F, par3 + 0.5F);
						par5 = 90.0F;
				}
			}
			else GL11.glTranslatef(par1 + 0.5F, par2, par3 + 0.5F);

			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hour >= 19 && hour <= 21) {
                this.facePlayer(skull.xCoord, skull.yCoord, skull.zCoord);
                par5 = 0F;
            }
			modelSkull.render(null, 0F, 0F, 0F, par5, 0F, 0.0625F);
			GL11.glPopMatrix();
		}
		else super.func_152674_a(par1, par2, par3, par4, par5, par6, gameProfile);
	}

    private void facePlayer(double posX, double posY, double posZ) {
        EntityPlayer par1Entity = Minecraft.getMinecraft().thePlayer;
        if (par1Entity != null) {
            double d0 = par1Entity.posX - posX - par1Entity.width;
            double d2 = par1Entity.posZ - posZ - par1Entity.width;
            double d1 = par1Entity.posY - posY - par1Entity.height + (double)par1Entity.getEyeHeight() + (par1Entity.worldObj.isRemote ? 0.1F : 0.22F);

            double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
            float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));

            GL11.glRotatef(f2 + 180, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(f3, 1.0F, 0.0F, 0.0F);
        }
    }
}
