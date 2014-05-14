package kihira.playerbeacons.item;

import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class RedCrystalItem extends CrystalItem {
	public RedCrystalItem() {
		this.setUnlocalizedName("redCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("resistance");
		return list;
	}

	@Override
	public double[] getRGBA() {
		return new double[]{0.5, 0, 0, 1};
	}

    @Override
    public float doEffects(EntityPlayer player, IBeacon beacon, int crystalCount) {
        Buff buff = Buff.buffs.get("resistance");
        buff.doBuff(player, beacon, crystalCount);
        return buff.getCorruption(player, beacon, crystalCount);
    }
}
