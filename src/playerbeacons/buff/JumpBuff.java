package playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import playerbeacons.item.CrystalItem;

public class JumpBuff extends Buff {

	public JumpBuff(CrystalItem crystalItem) {
		super(crystalItem, 10, 1, 1);
	}

	@Override
	public void doBuff(EntityPlayer player, int beaconLevels, int crystalCount) {
		if (crystalCount < beaconLevels) player.addPotionEffect(new PotionEffect(Potion.jump.id, 300, beaconLevels - crystalCount - 1, true));
	}

	@Override
	public float getCorruption(int beaconLevel) {
		return beaconLevel * corruptionGenerated;
	}

	@Override
	public String getName() {
		return "Jump";
	}
}
