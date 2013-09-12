package playerbeacons.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import playerbeacons.item.*;
import playerbeacons.proxy.ClientProxy;
import playerbeacons.tileentity.TileEntityDefiledSoulPylon;

public class BlockDefiledSoulPylonRenderer extends TileEntitySpecialRenderer {

	private final ModelPylonBase modelPylonBase;
	private final ModelPylon modelPylon;
	private final ModelCrystalPort modelCrystalPortDefault;
	private final ModelCrystalPort modelCrystalPortDig;
	private final ModelCrystalPort modelCrystalPortJump;
	private final ModelCrystalPort modelCrystalPortSpeed;
	private final ModelCrystalPort modelCrystalPortRes;

	public BlockDefiledSoulPylonRenderer() {
		modelPylon = new ModelPylon();
		modelPylonBase = new ModelPylonBase();
		modelCrystalPortDefault = new ModelCrystalPort(0);
		modelCrystalPortDig = new ModelCrystalPort(24);
		modelCrystalPortJump = new ModelCrystalPort(6);
		modelCrystalPortSpeed = new ModelCrystalPort(12);
		modelCrystalPortRes = new ModelCrystalPort(18);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
		TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) tileentity;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if (tileEntityDefiledSoulPylon.isPylonBase()) {
			GL11.glTranslated(x + 0.5d, y + 1.5001d, z + 0.5d);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glScalef(1F, 1F, 1F);
			bindTexture(ClientProxy.pylonTextureBase);
			modelPylonBase.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		else if (tileEntityDefiledSoulPylon.isPylonTop()) {
			GL11.glTranslated(x + 0.5d, y - 0.5d, z + 0.5d);
			GL11.glRotatef(0F, 0F, 0F, 1F);
			GL11.glScalef(1F, 1F, 1F);
			bindTexture(ClientProxy.pylonTextureBase);
			modelPylonBase.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		else {
			GL11.glTranslated(x + 0.5d, y + 1.5001d, z + 0.5d);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glScalef(1F, 1F, 1F);
			bindTexture(ClientProxy.pylonTexture);
			modelPylon.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			ItemStack itemStack = tileEntityDefiledSoulPylon.getStackInSlot(0);
			bindTexture(ClientProxy.pylonCrystalPortTexture);
			if (itemStack != null) {
				if (itemStack.getItem() instanceof DigCrystalItem) modelCrystalPortDig.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
				else if (itemStack.getItem() instanceof JumpCrystalItem) modelCrystalPortJump.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
				else if (itemStack.getItem() instanceof SpeedCrystalItem) modelCrystalPortSpeed.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
				else if (itemStack.getItem() instanceof ResCrystalItem) modelCrystalPortRes.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
				else modelCrystalPortDefault.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			}
			else {
				modelCrystalPortDefault.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			}
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
