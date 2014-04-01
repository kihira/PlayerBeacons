package kihira.playerbeacons.buff;

import kihira.playerbeacons.api.IBeacon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import kihira.playerbeacons.api.buff.Buff;

public class SpeedBuff extends Buff {

	public SpeedBuff() {
		super("speed", 10, 1, 1);
	}

	@Override
	public void doBuff(EntityPlayer player, IBeacon theBeacon, int crystalCount) {
		if (crystalCount < theBeacon.getLevels()) player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 300, theBeacon.getLevels() - crystalCount - 1, true));
	}

	@Override
	public float getCorruption(int beaconLevel) {
		return beaconLevel * corruptionGenerated;
	}

	@Override
	public String getName() {
		return "Speed";
	}
}
