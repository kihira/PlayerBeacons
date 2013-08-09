package playerbeacons.item;

import net.minecraft.potion.Potion;

public class ResCrystalItem extends CrystalItem {
	public ResCrystalItem(int id) {
		super(id);
		func_111206_d("playerbeacon:redXtal");
		setUnlocalizedName("resCrystalItem");
		corruptionValue = 5f;
	}

	public Potion getBuffType() {
		return Potion.resistance;
	}

	public int getBuffMax() {
		return 2;
	}
}
