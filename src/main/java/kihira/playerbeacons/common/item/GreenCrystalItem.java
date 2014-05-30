package kihira.playerbeacons.common.item;

import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.IBuff;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class GreenCrystalItem extends CrystalItem {
	public GreenCrystalItem() {
        this.setUnlocalizedName("greenCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("jump");
		return list;
	}

	@Override
	public double[] getRGBA() {
		return new double[]{0.45, 0.6, 0.45, 1};
	}

    @Override
    public float doEffects(EntityPlayer player, IBeacon beacon, int crystalCount) {
        IBuff buff = IBuff.buffs.get("jump");
        return buff.doBuff(player, beacon, crystalCount);
    }
}
