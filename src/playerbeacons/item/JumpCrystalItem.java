package playerbeacons.item;

import net.minecraft.potion.Potion;

public class JumpCrystalItem extends CrystalItem {
	public JumpCrystalItem(int id) {
		super(id);
		func_111206_d("playerbeacon:greenXtal");
		setUnlocalizedName("jumpCrystalItem");
		corruptionValue = 5f;
	}

	public Potion getBuffType() {
		return Potion.jump;
	}

	public int getBuffMax() {
		return 4;
	}
}
