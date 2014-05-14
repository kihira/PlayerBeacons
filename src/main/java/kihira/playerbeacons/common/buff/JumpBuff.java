package kihira.playerbeacons.common.buff;

import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

public class JumpBuff extends Buff {

	public JumpBuff() {
		super("jump", 10, 1, 1);
	}

	@Override
	public void doBuff(EntityPlayer player, IBeacon theBeacon, int crystalCount) {
		if (crystalCount < theBeacon.getLevels()) player.addPotionEffect(new PotionEffect(Potion.jump.id, 300, theBeacon.getLevels() - crystalCount - 1, true));
	}

	@Override
	public float getCorruption(EntityPlayer player, IBeacon theBeacon, int crystalCount) {
		return MathHelper.clamp_int(crystalCount, 0, theBeacon.getLevels()) * this.corruptionGenerated;
	}

    @Override
    public float[] getRGBA() {
        return new float[] {0.45F, 0.6F, 0.45F, 1F};
    }

	@Override
	public String getName() {
		return "Jump";
	}
}
