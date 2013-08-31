package playerbeacons.item;

import net.minecraft.potion.Potion;

public class DigCrystalItem extends CrystalItem {
	public DigCrystalItem(int id) {
		super(id);
		func_111206_d("playerbeacon:brownXtal");
		setUnlocalizedName("digCrystalItem");
		corruptionValue = 10f;
	}
}
