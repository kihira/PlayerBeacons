package kihira.playerbeacons.api.buff;

import kihira.playerbeacons.api.IBeacon;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public interface IBuff {

	public static HashMap<String, IBuff> buffs = new HashMap<String, IBuff>();

	/**
	 * This method is called when the requirements to activate the buff are reached.
	 * @param player The owner of the beacon
	 * @param theBeacon The beacon itself
	 * @param crystalCount The amount of crystals detected that throttle the buff
     * @return The change of corruption (can be negative)
	 */
	public float doBuff(EntityPlayer player, IBeacon theBeacon, int crystalCount);

    //TODO depreciate?
    public float[] getRGBA();
}
