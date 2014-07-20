package kihira.playerbeacons.api.beacon;

import net.minecraft.world.World;

/**
 * Implemented by blocks that can be used as a base
 */
public interface IBeaconBase {

    /**
     * The amount of corruption the block reduces
     * @param beacon The beacon
     * @param blockCount The amount of blocks detected by the beacon
     * @return the reduction for the count of all blocks
     */
    public float getCorruptionReduction(AbstractBeacon beacon, int blockCount);

    /**
     * Returns the beacon it is already being used by
     * @param world The world
     * @param x The x pos
     * @param y The y pos
     * @param z The z pos
     * @return If it has parent. null if none
     */
    public IBeacon getBeacon(World world, int x, int y, int z);

    /**
     * Sets the beacon for this block
     * @param world The world
     * @param x The x pos
     * @param y The y pos
     * @param z The z pos
     * @param theBeacon The beacon
     */
    public void setBeacon(World world, int x, int y, int z, IBeacon theBeacon);
}
