package kihira.playerbeacons.api.crystal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * This is implemented by items/blocks that can go inside the standard pylon
 */
public interface ICrystal {

    /**
     * Returns the colour that should be rendered when in a pylon
     * @return float array length of 4
     */
    @SideOnly(Side.CLIENT)
	public float[] getRGBA();

    /**
     * This method is called when the beacon is applying buffs. Return the corruption change made by these effects.
     * This is usually called every tick but this rate can be ignored by other mods
     * @param crystalCount The number of crystals detected by the beacon. This can be 0
     * @param beacon The beacon
     * @return The corruption change caused by the effects
     */
    public float doEffects(EntityPlayer player, AbstractBeacon beacon, int crystalCount);

    /**
     * A list of Buff's affected by this throttle. Should return a list of the Buff's names, not the Buff itself!
     * Eg "dig" instead of DigBuff
     * @return List of Buff's names that this throttle effects.
     */
    public List<Buff> getAffectedBuffs();
}
