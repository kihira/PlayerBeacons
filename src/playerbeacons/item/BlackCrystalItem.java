package playerbeacons.item;

import java.util.ArrayList;
import java.util.List;

public class BlackCrystalItem extends CrystalItem {
	public BlackCrystalItem(int id) {
		super(id);
		setTextureName("playerbeacon:redXtal");
		setUnlocalizedName("blackCrystalItem");
	}

	@Override
	public List<String> getAffectedBuffs() {
		List<String> list = new ArrayList<String>();
		list.add("resistance");
		return list;
	}
}
