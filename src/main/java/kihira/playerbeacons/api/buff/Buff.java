package kihira.playerbeacons.api.buff;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.beacon.AbstractBeacon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

/**
 * Base class for any Buff's. Extend this class and call a new instance of your Buff
 * and the buff will be automatically registered.
 */
public abstract class Buff {

	public static HashMap<String, Buff> buffs = new HashMap<String, Buff>();
    public final String buffName;

	public Buff(String simpleName) {
		if (!buffs.containsKey(simpleName)) {
            this.buffName = simpleName;
			buffs.put(simpleName, this);
		}
		else throw new IllegalArgumentException("Buff " + simpleName + " is already registered");
    }

	/**
	 * This method is called when the requirements to activate the buff are reached.
	 * @param player The owner of the beacon
	 * @param theBeacon The beacon itself
	 * @param crystalCount The amount of crystals detected that throttle the buff
     * @return The corruption change (can be negative)
	 */
	public abstract float doBuff(EntityPlayer player, AbstractBeacon theBeacon, int crystalCount);

    @SideOnly(Side.CLIENT)
    public abstract float[] getRGBA();

    @SideOnly(Side.CLIENT)
    public abstract ResourceLocation getResourceLocation();

    @SideOnly(Side.CLIENT)
    public abstract int[] getUV();
}
