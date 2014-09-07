package kihira.playerbeacons.common.item.crystal;

import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModItems;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class BrownCrystalItem extends CrystalItem {
	public BrownCrystalItem() {
        this.rgba = new float[]{0.5F, 0.4F, 0.3F, 1};
        this.setUnlocalizedName(ModItems.Names.CRYSTAL_BROWN);
	}

	@Override
	public List<Buff> getAffectedBuffs() {
		List<Buff> list = new ArrayList<Buff>();
		list.add(PlayerBeacons.hasteBuff);
		return list;
	}

    @Override
    public float doEffects(EntityPlayer player, AbstractBeacon beacon, int crystalCount) {
        return PlayerBeacons.hasteBuff.doBuff(player, beacon, crystalCount);
    }
}
