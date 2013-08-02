package playerbeacons.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class JumpCrystalItem extends CrystalItem {
	public JumpCrystalItem(int id) {
		super(id);
		setMaxDamage(1000);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabMaterials);
		setUnlocalizedName("jumpCrystalItem");
	}
}
