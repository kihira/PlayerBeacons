package kihira.playerbeacons.item;

import java.util.ArrayList;
import java.util.List;

public class BrownCrystalItem extends CrystalItem {
	public BrownCrystalItem(int id) {
		super(id);
		setUnlocalizedName("brownCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("dig");
		return list;
	}

	@Override
	public double[] getRGBA() {
		return new double[]{0.5, 0.4, 0.3, 1};
	}
}
