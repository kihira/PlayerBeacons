package playerbeacons.item;

import net.minecraft.potion.Potion;

public class InvisCrystalItem extends CrystalItem {
	public InvisCrystalItem(int id) {
		super(id);
	}

	public Potion getBuffType() {
		return Potion.invisibility;
	}

	public int getBuffMax() {
		return 1;
	}
}
