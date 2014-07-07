package kihira.playerbeacons.api.beacon;

/**
 * Implemented by blocks that can be used as a base
 */
public interface IBeaconBase {

    /**
     * Called to check if this block is valid for the beacon
     * @param beacon The beacon
     * @return if valid
     */
    public boolean isValidForBeacon(IBeacon beacon);

    /**
     * The amount of corruption the block reduces
     * @param beacon The beacon
     * @param blockCount The amount of blocks detected by the beacon
     * @return the reduction for the count of all blocks
     */
    public float getCorruptionReduction(IBeacon beacon, int blockCount);
}
