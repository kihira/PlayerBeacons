package playerbeacons.item;

import net.minecraft.potion.Potion;

public class FireResCrystalItem extends CrystalItem {
	public FireResCrystalItem(int id) {
		super(id);
	}

	public Potion getBuffType() {
		return Potion.fireResistance;
	}

	public int getBuffMax() {
		return 2;
	}
}
