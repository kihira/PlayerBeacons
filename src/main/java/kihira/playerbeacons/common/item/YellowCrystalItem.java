package kihira.playerbeacons.common.item;

import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class YellowCrystalItem extends CrystalItem {

    public YellowCrystalItem() {
        this.rgba = new float[]{0.9F, 0.8F, 0.1F, 1};
        this.setUnlocalizedName("yellowCrystalItem");
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
