package kihira.playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import kihira.playerbeacons.api.buff.Buff;

public class JumpBuff extends Buff {

	public JumpBuff() {
		super("jump", 10, 1, 1);
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
