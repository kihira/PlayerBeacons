package kihira.playerbeacons.buff;

import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class DigBuff extends Buff {

	public DigBuff() {
		super("dig", 10, 1, 1);
	}

	@Override
	public void doBuff(EntityPlayer player, IBeacon theBeacon, int crystalCount) {
		if (crystalCount < theBeacon.getLevels()) player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 300, theBeacon.getLevels() - crystalCount - 1, true));
	}

	@Override
	public float getCorruption(int beaconLevel) {
		return beaconLevel * corruptionGenerated;
	}

    @Override
    public float[] getRGBA() {
        return new float[] {0.5F, 0.4F, 0.3F, 1F};
    }

    @Override
	public String getName() {
		return "Dig";
	}
}
