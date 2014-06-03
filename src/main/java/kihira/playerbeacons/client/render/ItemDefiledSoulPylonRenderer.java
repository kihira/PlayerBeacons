package kihira.playerbeacons.client.render;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemDefiledSoulPylonRenderer implements IItemRenderer {

	private final ModelPylon model;
	private final ModelCrystalPort modelCrystalPortDefault;

	public ItemDefiledSoulPylonRenderer() {
		model = new ModelPylon();
		modelCrystalPortDefault = new ModelCrystalPort();
	}

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
			case ENTITY: {
				renderPylonItem(0f, 0f, 0f, 1f);
				break;
			}
			case EQUIPPED: {
				renderPylonItem(0.5f, 1.5f, 0.5f, 1f);
				break;
			}
			case INVENTORY: {
				renderPylonItem(0f, 0.9f, 0f, 1f);
				break;
			}
			case EQUIPPED_FIRST_PERSON: {
				renderPylonItem(3f, 1.5f, 0.5f, 2f);
				break;
			}
			default: break;
		}

	}

	private void renderPylonItem(float x, float y, float z, float scale) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(ClientProxy.pylonTexture);
		GL11.glTranslated(x, y, z);
		GL11.glScalef(scale, scale, scale);
		GL11.glRotatef(180.0f, 0f, 0f, 1f);
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(ClientProxy.pylonCrystalPortTexture);
		modelCrystalPortDefault.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
