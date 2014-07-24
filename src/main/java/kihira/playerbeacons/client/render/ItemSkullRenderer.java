package kihira.playerbeacons.client.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Map;

public class ItemSkullRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON: {
                this.renderSkull(item, 0.5F, 0, 0.5F, 2F, 0F);
                break;
            }
            case EQUIPPED: {
                this.renderSkull(item, 0.5F, 0, 0.5F, 2F, -90F);
                break;
            }
            case INVENTORY: {
                this.renderSkull(item, 0, -0.5F, 0, 1.8F, 90F);
                break;
            }
            default: this.renderSkull(item, 0, 0, 0, 1F, 0);
        }
    }

    private void renderSkull(ItemStack itemStack, float x, float y, float z, float scale, float rot) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        GameProfile gameProfile = null;

        if (tagCompound.hasKey("SkullOwner", 10)) {
            gameProfile = NBTUtil.func_152459_a(tagCompound.getCompoundTag("SkullOwner"));
        }

        ResourceLocation resourcelocation = AbstractClientPlayer.locationStevePng;
        if (gameProfile != null) {
            Minecraft minecraft = Minecraft.getMinecraft();
            Map map = minecraft.func_152342_ad().func_152788_a(gameProfile);

            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                resourcelocation = minecraft.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            }
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef(x, y, z);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glRotatef(rot, 0F, 1F, 0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        Minecraft.getMinecraft().renderEngine.bindTexture(resourcelocation);
        BlockSkullRenderer.modelSkull.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
        GL11.glPopMatrix();
    }
}
