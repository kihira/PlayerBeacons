package playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ResistanceBuff extends Buff {

	@Override
	public void doBuff(EntityPlayer player, int beaconLevels) {
		player.addPotionEffect(new PotionEffect(Potion.resistance.id, 300, beaconLevels - 3, true));
	}

	@Override
	public int getMinBeaconLevel() {
		return 3;
	}

	@Override
	public float getCorruption(int beaconLevel) {
		return beaconLevel * 5;
	}

	@Override
	public String getName() {
		return "Resistance";
	}
}
