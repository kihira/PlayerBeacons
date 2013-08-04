package playerbeacons.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CrystalItem extends Item {
	public CrystalItem(int id) {
		super(id);
		//This equals one day in real time. Change it depending on how fast we calculate bad stuff
		//setMaxDamage(43200);
		setMaxDamage(20);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		super.setDamage(stack, damage);
		if (stack.getItemDamage() <= 0) {
		}
	}
}
