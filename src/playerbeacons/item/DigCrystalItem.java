package playerbeacons.item;

import net.minecraft.potion.Potion;

public class DigCrystalItem extends CrystalItem {
	public DigCrystalItem(int id) {
		super(id);
		func_111206_d("playerbeacon:brownXtal");
		setUnlocalizedName("digCrystalItem");
		corruptionValue = 5f;
	}

	public Potion getBuffType() {
		return Potion.digSpeed;
	}

	public int getBuffMax() {
		return 4;
	}
}
