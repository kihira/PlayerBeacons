package kihira.playerbeacons.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemPlayerBeaconRenderer implements IItemRenderer {

    private final TileEntitySpecialRenderer playerBeaconRenderer;

	public ItemPlayerBeaconRenderer(TileEntitySpecialRenderer playerBeaconRenderer) {
        this.playerBeaconRenderer = playerBeaconRenderer;
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
