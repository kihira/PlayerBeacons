package kihira.playerbeacons.client.render;

import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemPlayerBeaconRenderer implements IItemRenderer {

    TileEntitySpecialRenderer playerBeaconRenderer;

	public ItemPlayerBeaconRenderer() {
        this.playerBeaconRenderer = TileEntityRendererDispatcher.instance.getSpecialRendererByClass(TileEntityPlayerBeacon.class);
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
        if (this.playerBeaconRenderer != null) {
            switch (type) {
                case ENTITY: {
                    this.playerBeaconRenderer.renderTileEntityAt(null, -0.5D, -0.1D, -0.5D, 0F);
                    break;
                }
                case EQUIPPED: {
                    this.playerBeaconRenderer.renderTileEntityAt(null, 0D, 0D, 0D, 0F);
                    break;
                }
                case INVENTORY: {
                    this.playerBeaconRenderer.renderTileEntityAt(null, 0D, -0.1D, 0D, 0F);
                    break;
                }
                case EQUIPPED_FIRST_PERSON: {
                    this.playerBeaconRenderer.renderTileEntityAt(null, 0D, -0.1D, 0D, 0F);
                    break;
                }
                default: break;
            }
        }
	}
}
