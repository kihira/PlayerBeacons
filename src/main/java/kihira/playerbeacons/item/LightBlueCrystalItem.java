package kihira.playerbeacons.item;

import java.util.ArrayList;
import java.util.List;

public class LightBlueCrystalItem extends CrystalItem {
	public LightBlueCrystalItem() {
		setUnlocalizedName("lightBlueCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("speed");
		return list;
	}

	@Override
	public double[] getRGBA() {
		return new double[]{0.5, 0.5, 1, 1};
	}
}
