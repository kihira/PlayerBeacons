package kihira.playerbeacons.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabPlayerBeacons extends CreativeTabs {

	public CreativeTabPlayerBeacons() {
		super("playerbeacons");
	}

    @Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(PlayerBeacons.playerBeaconBlock);
	}
}
