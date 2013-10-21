package playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public abstract class Buff {

	public static HashMap<String, Buff> buffs = new HashMap<String, Buff>();

	protected float corruptionGenerated;
	protected int maxBuffLevel;
	protected int minBeaconLevel;

	public Buff(String simpleName, float corruptionGenerated, int maxBuffLevel, int minBeaconLevel) {
		if (!buffs.containsKey(simpleName)) {
			buffs.put(simpleName, this);
			this.corruptionGenerated = corruptionGenerated;
			this.maxBuffLevel = maxBuffLevel;
			this.minBeaconLevel = minBeaconLevel;
		}
		else throw new IllegalArgumentException("Buff " + simpleName + " is already registered");
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
