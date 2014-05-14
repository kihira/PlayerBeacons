package kihira.playerbeacons.common.item;

import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class BrownCrystalItem extends CrystalItem {
	public BrownCrystalItem() {
        this.setUnlocalizedName("brownCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("haste");
		return list;
	}

	@Override
	public double[] getRGBA() {
		return new double[]{0.5, 0.4, 0.3, 1};
	}

    @Override
    public float doEffects(EntityPlayer player, IBeacon beacon, int crystalCount) {
        Buff buff = Buff.buffs.get("dig");
        buff.doBuff(player, beacon, crystalCount);
        return buff.getCorruption(player, beacon, crystalCount);
    }
}
