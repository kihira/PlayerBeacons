package kihira.playerbeacons.common.item;

import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.Beacon;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class YellowCrystalItem extends CrystalItem {

    public YellowCrystalItem() {
        this.rgba = new float[]{0.9F, 0.8F, 0.1F, 1};
        this.setUnlocalizedName("yellowCrystalItem");
    }

    @Override
    public List<String> getAffectedBuffs() {
        List<String> list = new ArrayList<String>();
        list.add("healthBoost");
        return list;
    }

    @Override
    public float doEffects(EntityPlayer player, Beacon beacon, int crystalCount) {
        return Buff.buffs.get("healthBoost").doBuff(player, beacon, crystalCount);
    }
}
