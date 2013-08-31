package playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import playerbeacons.item.CrystalItem;

public class ResistanceBuff extends Buff {

	public ResistanceBuff(CrystalItem crystalItem) {
		super(crystalItem);
	}

	@Override
	public void doBuff(EntityPlayer player, int beaconLevels, int crystalCount) {
		if (crystalCount < beaconLevels - 2) player.addPotionEffect(new PotionEffect(Potion.resistance.id, 300, beaconLevels - crystalCount - 3, true));
	}

	@Override
	public int getMinBeaconLevel() {
		return 3;
	}

	@Override
	public float getCorruption(int beaconLevel) {
		return (beaconLevel - 2) * 20;
	}

	@Override
	public String getName() {
		return "Resistance";
	}
}
