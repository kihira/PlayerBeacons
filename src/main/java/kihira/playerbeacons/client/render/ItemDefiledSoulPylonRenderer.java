package kihira.playerbeacons.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class ItemDefiledSoulPylonRenderer implements IItemRenderer {

    private final BlockDefiledSoulPylonRenderer pylonModelRenderer;

	public ItemDefiledSoulPylonRenderer(BlockDefiledSoulPylonRenderer pylonModelRenderer) {
        this.pylonModelRenderer = pylonModelRenderer;
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
				this.pylonModelRenderer.renderTileEntityAt(null, 0F, 0F, 0F, 0F);
				break;
			}
			case EQUIPPED: {
                this.pylonModelRenderer.renderTileEntityAt(null, 0F, 0F, 0F, 0F);
				break;
			}
			case INVENTORY: {
                this.pylonModelRenderer.renderTileEntityAt(null, 0F, -0.2F, 0F, 0F);
				break;
			}
			case EQUIPPED_FIRST_PERSON: {
                this.pylonModelRenderer.renderTileEntityAt(null, 0F, 0F, 0F, 0F);
				break;
			}
			default: break;
		}

	}
}
