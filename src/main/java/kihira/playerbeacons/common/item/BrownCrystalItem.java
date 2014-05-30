package kihira.playerbeacons.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class BrownCrystalItem extends CrystalItem {
	public BrownCrystalItem() {
        this.setUnlocalizedName("brownCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("haste");
		return list;
	}

	@Override
    @SideOnly(Side.CLIENT)
	public double[] getRGBA() {
		return new double[]{0.5, 0.4, 0.3, 1};
	}

    @Override
    public float doEffects(EntityPlayer player, IBeacon beacon, int crystalCount) {
        Buff buff = Buff.buffs.get("haste");
        return buff.doBuff(player, beacon, crystalCount);
    }
}
