package kihira.playerbeacons.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.Beacon;
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
	public float[] getRGBA() {
		return new float[]{0.45F, 0.6F, 0.45F, 1};
	}

    @Override
    public float doEffects(EntityPlayer player, Beacon beacon, int crystalCount) {
        return Buff.buffs.get("jump").doBuff(player, beacon, crystalCount);
    }
}
