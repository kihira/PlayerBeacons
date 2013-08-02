package playerbeacons.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ResCrystalItem extends CrystalItem {
	public ResCrystalItem(int id) {
		super(id);
		setMaxDamage(1000);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabMaterials);
		setUnlocalizedName("resCrystalItem");
	}
}
