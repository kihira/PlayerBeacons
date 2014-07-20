package kihira.playerbeacons.common.item;

import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class LightBlueCrystalItem extends CrystalItem {
	public LightBlueCrystalItem() {
        this.rgba = new float[]{0.5F, 0.5F, 1, 1};
        this.setUnlocalizedName("lightBlueCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("speed");
		return list;
	}

    @Override
    public float doEffects(EntityPlayer player, AbstractBeacon beacon, int crystalCount) {
        Buff buff = Buff.buffs.get("speed");
        return buff.doBuff(player, beacon, crystalCount);
    }
}
