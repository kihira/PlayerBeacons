package kihira.playerbeacons.common.item.crystal;

import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModItems;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class RedCrystalItem extends CrystalItem {
	public RedCrystalItem() {
        this.rgba = new float[]{0.5F, 0, 0, 1};
        this.setUnlocalizedName(ModItems.Names.CRYSTAL_RED);
	}

	@Override
	public List<Buff> getAffectedBuffs() {
		List<Buff> list = new ArrayList<Buff>();
		list.add(PlayerBeacons.resistanceBuff);
		return list;
	}

    @Override
    public float doEffects(EntityPlayer player, AbstractBeacon beacon, int crystalCount) {
        return PlayerBeacons.resistanceBuff.doBuff(player, beacon, crystalCount);
    }
}
