package playerbeacons.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class SpeedCrystalItem extends CrystalItem {
	public SpeedCrystalItem(int id) {
		super(id);
		setMaxDamage(1000);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabMaterials);
		setUnlocalizedName("speedCrystalItem");
	}
}
