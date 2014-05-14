package kihira.playerbeacons.item;

import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class LightBlueCrystalItem extends CrystalItem {
	public LightBlueCrystalItem() {
        this.setUnlocalizedName("lightBlueCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("speed");
		return list;
	}

	@Override
    public double[] getRGBA() {
        return new double[]{0.5, 0.5, 1, 1};
    }

    @Override
    public float doEffects(EntityPlayer player, IBeacon beacon, int crystalCount) {
        Buff buff = Buff.buffs.get("speed");
        buff.doBuff(player, beacon, crystalCount);
        return buff.getCorruption(player, beacon, crystalCount);
    }
}
