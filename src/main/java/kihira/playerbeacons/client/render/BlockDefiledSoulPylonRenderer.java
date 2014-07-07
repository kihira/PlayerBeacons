package kihira.playerbeacons.client.render;

import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.common.tileentity.TileEntityDefiledSoulPylon;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class BlockDefiledSoulPylonRenderer extends TileEntitySpecialRenderer {

    private final ModelPylonBase modelPylonBase;
    private final ModelPylon modelPylon;
    private final ModelCrystalPort modelCrystalPort;

    public BlockDefiledSoulPylonRenderer() {
        this.modelPylon = new ModelPylon();
        this.modelPylonBase = new ModelPylonBase();
        this.modelCrystalPort = new ModelCrystalPort();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
        TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) tileentity;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslated(x + 0.5D, y, z + 0.5D);

        if (tileEntityDefiledSoulPylon != null) {
            if (tileEntityDefiledSoulPylon.getBlockMetadata() == 2) {
                GL11.glTranslatef(0F, 1.5001F, 0F);
                GL11.glRotatef(180F, 0F, 0F, 1F);
                this.bindTexture(ClientProxy.pylonTextureBase);
                this.modelPylonBase.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
                this.closeRender();
                return;
            }
            else if (tileEntityDefiledSoulPylon.getBlockMetadata() == 1) {
                GL11.glTranslatef(0F, -0.5F, 0F);
                this.bindTexture(ClientProxy.pylonTextureBase);
                this.modelPylonBase.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
                this.closeRender();
                return;
            }
        }

        //Default pylon model
        GL11.glTranslatef(0F, 1.5001F, 0F);
        GL11.glRotatef(180F, 0F, 0F, 1F);
        //Render the pylon
        this.bindTexture(ClientProxy.pylonTexture);
        if (tileEntityDefiledSoulPylon != null && tileEntityDefiledSoulPylon.getBlockMetadata() == 3) this.modelPylon.renderWithoutTop(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        else this.modelPylon.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        //Then the crystal port
        this.bindTexture(ClientProxy.pylonCrystalPortTexture);
        this.modelCrystalPort.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        //Then the overlay
        if (tileEntityDefiledSoulPylon != null) {
            ItemStack itemStack = tileEntityDefiledSoulPylon.getStackInSlot(0);
            if (itemStack != null && itemStack.getItem() instanceof ICrystal) {
                float[] rgba = ((ICrystal) itemStack.getItem()).getRGBA();
                GL11.glColor4d(rgba[0], rgba[1], rgba[2], rgba[3]);
            }
            else {
                GL11.glColor4d(0.8F, 0.8F, 0.8F, 1F);
            }
        }
        else {
            GL11.glColor4d(0.8F, 0.8F, 0.8F, 1F);
        }

        this.bindTexture(ClientProxy.pylonCrystalPortOverlayTexture);
        this.modelCrystalPort.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        this.closeRender();

        //Now render the status effect icons if player is holding crystal
        if (tileEntityDefiledSoulPylon != null && Minecraft.getMinecraft().thePlayer != null) {
            ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
            ItemStack itemStack = tileEntityDefiledSoulPylon.getStackInSlot(0);
            if (heldItem != null && heldItem.getItem() instanceof ICrystal && itemStack != null && itemStack.getItem() instanceof ICrystal) {
                //Reset the render so we can properly rotate
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                float scale = 0.00390625F;
                double d0 = player.posX - 0.5F - tileentity.xCoord;
                double d2 = player.posZ - 0.5F - tileentity.zCoord;
                float angle = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;

                GL11.glTranslated(x + 0.5d, y + 1.5001d, z + 0.5d);
                GL11.glRotatef(-angle + 180F, 0.0F, 1.0F, 0.0F); //Rotate to face the player
                GL11.glRotatef(180F, 0F, 0F, 1F); //Flip it right way up
                GL11.glTranslatef(-0.3F, 0.7F, -0.5F); //Offset from the center
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F); //Make it slightly transparent
                GL11.glScalef(0.03F, 0.03F, 0.03F);
                //TODO limit to one buff or scale to fit both
                List<String> buffList = ((ICrystal) itemStack.getItem()).getAffectedBuffs();
                if (buffList != null && buffList.size() > 0) {
                    for (String buffName : buffList) {
                        Buff buff = Buff.buffs.get(buffName);
                        if (buff != null) {
                            //Bind the texture
                            this.bindTexture(buff.getResourceLocation() != null ? buff.getResourceLocation() : ClientProxy.potionTextures);

                            //Set the UV
                            int[] uv = buff.getUV();
                            if (uv == null || uv.length != 2) {
                                uv = new int[]{0, 0};
                            }

                            Tessellator tessellator = Tessellator.instance;
                            tessellator.startDrawingQuads();
                            tessellator.addVertexWithUV(0, 18, 0, uv[0] * scale, (uv[1] + 18) * scale);
                            tessellator.addVertexWithUV(18, 18, 0, (uv[0] + 18) * scale, (uv[1] + 18) * scale);
                            tessellator.addVertexWithUV(18, 0, 0, (uv[0] + 18) * scale, uv[1] * scale);
                            tessellator.addVertexWithUV(0, 0, 0, uv[0] * scale, uv[1] * scale);
                            tessellator.draw();
                        }
                    }
                }
                GL11.glEnable(GL11.GL_LIGHTING);
                this.closeRender();
            }
        }
    }

    private void closeRender() {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
