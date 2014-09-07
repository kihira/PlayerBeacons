package kihira.playerbeacons.common.item.crystal;

import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModItems;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class GreenCrystalItem extends CrystalItem {
	public GreenCrystalItem() {
        this.rgba = new float[]{0.45F, 0.6F, 0.45F, 1};
        this.setUnlocalizedName(ModItems.Names.CRYSTAL_GREEN);
	}

	@Override
	public List<Buff> getAffectedBuffs() {
		List<Buff> list = new ArrayList<Buff>();
		list.add(PlayerBeacons.jumpBuff);
		return list;
	}

    @Override
    public float doEffects(EntityPlayer player, AbstractBeacon beacon, int crystalCount) {
        return PlayerBeacons.jumpBuff.doBuff(player, beacon, crystalCount);
    }
}
