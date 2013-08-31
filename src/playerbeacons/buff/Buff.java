package playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import playerbeacons.item.CrystalItem;

import java.util.ArrayList;

public abstract class Buff {

	public static ArrayList<Buff> buffs = new ArrayList<Buff>();

	protected CrystalItem crystalItem;

	public Buff(CrystalItem crystalItem) {
		if (!buffs.contains(this)) {
			buffs.add(this);
			this.crystalItem = crystalItem;
		}
		else throw new IllegalArgumentException("Buff " + this.getName() + " is already registered");
	}

	public abstract String getName();
	public abstract void doBuff(EntityPlayer player, int beaconLevels, int crystalCount);
	public abstract int getMinBeaconLevel();
	public abstract float getCorruption(int beaconLevel);
	public CrystalItem getCrystal() {
		return crystalItem;
	}
}
