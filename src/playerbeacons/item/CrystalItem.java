package playerbeacons.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import playerbeacons.buff.Buff;
import playerbeacons.common.PlayerBeacons;

public class CrystalItem extends Item {

	protected float corruptionValue;

	public CrystalItem(int id) {
		super(id);
		//This equals one day in real time. Change it depending on how fast we calculate bad stuff
		setMaxDamage(43200);
		setMaxStackSize(1);
		setCreativeTab(PlayerBeacons.tabPlayerBeacons);
		setTextureName("playerbeacon:grayXtal");
		setUnlocalizedName("Depleted Crystal");
		corruptionValue = 10f;
	}

	public float getCorruptionReduction() {
		return corruptionValue;
	}
}
