package kihira.playerbeacons.api.crystal;

import kihira.playerbeacons.api.IBeacon;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * This is implemented by items/blocks that can go inside the standard pylon
 */
public interface ICrystal {

	public double[] getRGBA();

    /**
     * This method is called when the beacon is applying buffs. Return the corruption change made by these effects
     * @param crystalCount
     * @param beacon
     * @return
     */
    public float doEffects(EntityPlayer player, IBeacon beacon, int crystalCount);

    /**
     * A list of Buff's affected by this throttle. Should return a list of the Buff's names, not the Buff itself!
     * Eg "dig" instead of DigBuff
     * @return List of Buff's names that this throttle effects.
     */
    public List<String> getAffectedBuffs();
}
