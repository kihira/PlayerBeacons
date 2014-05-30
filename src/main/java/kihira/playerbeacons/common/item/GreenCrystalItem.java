package kihira.playerbeacons.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class GreenCrystalItem extends CrystalItem {
	public GreenCrystalItem() {
        this.setUnlocalizedName("greenCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("jump");
		return list;
	}

	@Override
    @SideOnly(Side.CLIENT)
	public double[] getRGBA() {
		return new double[]{0.45, 0.6, 0.45, 1};
	}

    @Override
    public float doEffects(EntityPlayer player, IBeacon beacon, int crystalCount) {
        Buff buff = Buff.buffs.get("jump");
        return buff.doBuff(player, beacon, crystalCount);
    }
}
