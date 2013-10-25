package playerbeacons.item;

import java.util.ArrayList;
import java.util.List;

public class BrownCrystalItem extends CrystalItem {
	public BrownCrystalItem(int id) {
		super(id);
		setTextureName("playerbeacon:brownXtal");
		setUnlocalizedName("brownCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("dig");
		return list;
	}
}
