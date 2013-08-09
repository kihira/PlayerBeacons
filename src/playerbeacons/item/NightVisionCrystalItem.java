package playerbeacons.item;

import net.minecraft.potion.Potion;

public class NightVisionCrystalItem extends CrystalItem {
	public NightVisionCrystalItem(int id) {
		super(id);
	}

	public Potion getBuffType() {
		return Potion.nightVision;
	}

	public int getBuffMax() {
		return 2;
	}
}
