package playerbeacons.buff;

import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public abstract class Buff {

	public static ArrayList<Buff> buffs = new ArrayList<Buff>();

	public static void registerBuff(Buff clazz) {
		if (buffs.contains(clazz)) {
			throw new IllegalArgumentException("Buff " + clazz.getName() + " is already registered!");
		}
		else {
			buffs.add(clazz);
		}
	}

	public abstract String getName();
	public abstract void doBuff(EntityPlayer player, int beaconLevels);
	public abstract int getMinBeaconLevel();
	public abstract float getCorruption(int beaconLevel);
}
