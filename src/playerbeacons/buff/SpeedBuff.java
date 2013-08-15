package playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpeedBuff extends Buff {

	@Override
	public void doBuff(EntityPlayer player, int beaconLevels) {
		player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 300, beaconLevels - 1, true));
	}

	@Override
	public int getMinBeaconLevel() {
		return 1;
	}

	@Override
	public float getCorruption(int beaconLevel) {
		return beaconLevel * 2;
	}

	@Override
	public String getName() {
		return "Speed";
	}
}
