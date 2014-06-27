package kihira.playerbeacons.client.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class BlockSkullRenderer extends TileEntitySkullRenderer {

	private final ModelSkull modelSkull = new ModelSkull();

	@Override
	public void func_152674_a(float par1, float par2, float par3, int par4, float par5, int par6, GameProfile gameProfile) {
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
			modelSkull.render(null, 0.0F, 0.0F, 0.0F, par5, 0.0F, 0.0625F);
			GL11.glPopMatrix();
		}
		else super.func_152674_a(par1, par2, par3, par4, par5, par6, gameProfile);
	}

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
        super.renderTileEntityAt((TileEntitySkull) tileentity, d0, d1, d2, f);
    }
}
