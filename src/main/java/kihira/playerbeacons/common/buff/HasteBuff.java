package kihira.playerbeacons.common.buff;

import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.IBuff;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

public class HasteBuff implements IBuff {

	@Override
	public float doBuff(EntityPlayer player, IBeacon theBeacon, int crystalCount) {
        player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 300, MathHelper.clamp_int(crystalCount, 0, theBeacon.getLevels()) - 1, true));
        return MathHelper.clamp_int(crystalCount, 0, theBeacon.getLevels()) * 10;
	}

    @Override
    public float[] getRGBA() {
        return new float[] {0.5F, 0.4F, 0.3F, 1F};
    }
}
