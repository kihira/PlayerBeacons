package kihira.playerbeacons.common.item;

import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.Beacon;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class RedCrystalItem extends CrystalItem {
	public RedCrystalItem() {
        this.rgba = new float[]{0.5F, 0, 0, 1};
		this.setUnlocalizedName("redCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("resistance");
		return list;
	}

    @Override
    public float doEffects(EntityPlayer player, Beacon beacon, int crystalCount) {
        Buff buff = Buff.buffs.get("resistance");
        return buff.doBuff(player, beacon, crystalCount);
    }
}
