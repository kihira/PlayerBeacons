package kihira.playerbeacons.common.item.crystal;

import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModItems;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class YellowCrystalItem extends CrystalItem {

    public YellowCrystalItem() {
        this.rgba = new float[]{0.9F, 0.8F, 0.1F, 1};
        this.setUnlocalizedName(ModItems.Names.CRYSTAL_YELLOW);
    }

    @Override
    public List<Buff> getAffectedBuffs() {
        List<Buff> list = new ArrayList<Buff>();
        list.add(PlayerBeacons.healthBuff);
        return list;
    }

    @Override
    public float doEffects(EntityPlayer player, AbstractBeacon beacon, int crystalCount) {
        return PlayerBeacons.healthBuff.doBuff(player, beacon, crystalCount);
    }
}
