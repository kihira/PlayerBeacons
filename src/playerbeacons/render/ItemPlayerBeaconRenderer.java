package playerbeacons.render;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import playerbeacons.proxy.ClientProxy;

public class ItemPlayerBeaconRenderer implements IItemRenderer {

	private ModelPlayerBeacon modelPlayerBeacon;

	public ItemPlayerBeaconRenderer() {
		modelPlayerBeacon = new ModelPlayerBeacon();
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
				renderPlayerBeaconItem(0f, 0f, 0f, 1f);
				break;
			}
			case EQUIPPED: {
				renderPlayerBeaconItem(0.5f, 1.5f, 0.5f, 1f);
				break;
			}
			case INVENTORY: {
				renderPlayerBeaconItem(0f, 1f, 0f, 1f);
				break;
			}
			case EQUIPPED_FIRST_PERSON: {
				renderPlayerBeaconItem(3f, 1.5f, 0.5f, 2f);
				break;
			}
			default: break;
		}
	}

	private void renderPlayerBeaconItem(float x, float y, float z, float scale) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(ClientProxy.playerBeaconTexture);
		GL11.glTranslated(x, y, z);
		GL11.glScalef(scale, scale, scale);
		GL11.glRotatef(180.0f, 0f, 0f, 1f);
		modelPlayerBeacon.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
