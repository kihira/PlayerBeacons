package playerbeacons.item;

import java.util.ArrayList;
import java.util.List;

public class GreenCrystalItem extends CrystalItem {
	public GreenCrystalItem(int id) {
		super(id);
		setTextureName("playerbeacon:greenXtal");
		setUnlocalizedName("greenCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("jump");
		return list;
	}

	@Override
	public double[] getRGBA() {
		return new double[]{0.45, 0.6, 0.45, 1};
	}
}
