package kihira.playerbeacons.common.item.crystal;

import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModItems;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class LightBlueCrystalItem extends CrystalItem {
	public LightBlueCrystalItem() {
        this.rgba = new float[]{0.5F, 0.5F, 1, 1};
        this.setUnlocalizedName(ModItems.Names.CRYSTAL_LIGHT_BLUE);
	}

	@Override
	public List<Buff> getAffectedBuffs() {
		List<Buff> list = new ArrayList<Buff>();
		list.add(PlayerBeacons.speedbuff);
		return list;
	}

    @Override
    public float doEffects(EntityPlayer player, AbstractBeacon beacon, int crystalCount) {
        return PlayerBeacons.speedbuff.doBuff(player, beacon, crystalCount);
    }
}
