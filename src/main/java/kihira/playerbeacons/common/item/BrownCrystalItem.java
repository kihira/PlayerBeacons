package kihira.playerbeacons.common.item;

import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class BrownCrystalItem extends CrystalItem {
	public BrownCrystalItem() {
        this.rgba = new float[]{0.5F, 0.4F, 0.3F, 1};
        this.setUnlocalizedName("brownCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("haste");
		return list;
	}

    @Override
    public float doEffects(EntityPlayer player, AbstractBeacon beacon, int crystalCount) {
        Buff buff = Buff.buffs.get("haste");
        return buff.doBuff(player, beacon, crystalCount);
    }
}
