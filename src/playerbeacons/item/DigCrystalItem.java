package playerbeacons.item;

public class DigCrystalItem extends CrystalItem {
	public DigCrystalItem(int id) {
		super(id);
		setTextureName("playerbeacon:brownXtal");
		setUnlocalizedName("digCrystalItem");
		corruptionValue = 10f;
	}
}
