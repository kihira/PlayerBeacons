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
     * @return
     */
    public float getCorruptionReduction(IBeacon beacon);
}
