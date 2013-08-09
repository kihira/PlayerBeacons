package playerbeacons.item;

import net.minecraft.potion.Potion;

public class WaterCrystalItem extends CrystalItem {
	public WaterCrystalItem(int id) {
		super(id);
	}

	public Potion getBuffType() {
		return Potion.waterBreathing;
	}

	public int getBuffMax() {
		return 2;
	}
}
