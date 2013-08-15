package playerbeacons.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import playerbeacons.buff.Buff;

public class TeleporterItem extends Item {

	public TeleporterItem(int id) {
		super(id);
		setUnlocalizedName("Recall");
		setCreativeTab(CreativeTabs.tabTools);
		setMaxStackSize(1);
	}
}
