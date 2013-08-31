package playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import playerbeacons.item.CrystalItem;

public class DigBuff extends Buff {

	public DigBuff(CrystalItem crystalItem) {
		super(crystalItem);
	}

	@Override
	public void doBuff(EntityPlayer player, int beaconLevels, int crystalCount) {
		if (crystalCount < beaconLevels) player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 300, beaconLevels - crystalCount - 1, true));
	}

	@Override
	public int getMinBeaconLevel() {
		return 1;
	}

	@Override
	public float getCorruption(int beaconLevel) {
		return beaconLevel * 10;
	}

	@Override
	public String getName() {
		return "Dig";
	}
}
