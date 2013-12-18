package playerbeacons.item;

import java.util.ArrayList;
import java.util.List;

public class RedCrystalItem extends CrystalItem {
	public RedCrystalItem(int id) {
		super(id);
		setTextureName("playerbeacon:redXtal");
		setUnlocalizedName("redCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("resistance");
		return list;
	}

	@Override
	public double[] getRGBA() {
		return new double[]{0.5, 0, 0, 1};
	}
}
