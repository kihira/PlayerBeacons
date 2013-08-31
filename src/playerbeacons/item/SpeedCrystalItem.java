package playerbeacons.item;

import net.minecraft.potion.Potion;

public class SpeedCrystalItem extends CrystalItem {
	public SpeedCrystalItem(int id) {
		super(id);
		func_111206_d("playerbeacon:lightblueXtal");
		setUnlocalizedName("speedCrystalItem");
		corruptionValue = 10f;
	}
}
