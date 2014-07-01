package kihira.playerbeacons.api.beacon;

import com.mojang.authlib.GameProfile;
import net.minecraft.tileentity.TileEntity;

/**
 * Any beacon TileEntities implement this
 */
public interface IBeacon {

    /**
     * Check if the beacon should apply buffs. Called just before update()
     * This is called by TickHandler every tick so it is advised you cache the result and only run a check every 10-20
     * ticks to prevent higher resource usage (depending on your setup)
     * @return If beacon is valid
     */
    public boolean isBeaconValid();

    /**
     * This is called if isBeaconValid returns true and the player is in the same dimension
     */
    public void update();

    /**
     * Gets the owner of the beacon
     * @return The owner
     */
    public GameProfile getOwnerGameProfile();

    /**
     * The amount of levels to the beacon
     * @return The level count
     */
    public int getLevels();

    /**
     * How much corruption the beacon has generated per second
     * @return The corruption count
     */
    public float getCorruption();

    /**
     * Returns the tile entity
     * @return
     */
    public TileEntity getTileEntity();
}
