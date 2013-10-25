package playerbeacons.item;

import java.util.ArrayList;
import java.util.List;

public class LightBlueCrystalItem extends CrystalItem {
	public LightBlueCrystalItem(int id) {
		super(id);
		setTextureName("playerbeacon:lightblueXtal");
		setUnlocalizedName("lightBlueCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("speed");
		return list;
	}
}
