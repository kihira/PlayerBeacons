package playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import playerbeacons.item.CrystalItem;

import java.util.ArrayList;

public abstract class Buff {

	public static ArrayList<Buff> buffs = new ArrayList<Buff>();

	protected CrystalItem crystalItem;
	protected float corruptionGenerated;
	protected int maxBuffLevel;
	protected int minBeaconLevel;

	public Buff(CrystalItem crystalItem, float corruptionGenerated, int maxBuffLevel, int minBeaconLevel) {
		if (!buffs.contains(this)) {
			buffs.add(this);
			this.crystalItem = crystalItem;
			this.corruptionGenerated = corruptionGenerated;
			this.maxBuffLevel = maxBuffLevel;
			this.minBeaconLevel = minBeaconLevel;
		}
		else throw new IllegalArgumentException("Buff " + this.getName() + " is already registered");
	}

	public abstract String getName();
	public abstract void doBuff(EntityPlayer player, int beaconLevels, int crystalCount);
	public abstract float getCorruption(int beaconLevel);

	public int getMinBeaconLevel() {
		return minBeaconLevel;
	}
	public int getMaxBuffLevel() {
		return maxBuffLevel;
	}
	public CrystalItem getCrystal() {
		return crystalItem;
	}
	public void setCorruption(float newCorruption) {
		corruptionGenerated = newCorruption;
	}
	public void setMaxBuffLevel(int maxBuffLevel) {
		this.maxBuffLevel = maxBuffLevel;
	}
	public void setMinBeaconLevel(int minBeaconLevel) {
		this.minBeaconLevel = minBeaconLevel;
	}
}
