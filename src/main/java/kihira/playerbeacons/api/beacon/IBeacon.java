package kihira.playerbeacons.api.beacon;

import com.mojang.authlib.GameProfile;
import net.minecraft.tileentity.TileEntity;

/**
 * Any beacon TileEntities implement this
 */
public interface IBeacon {

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
     * Gets the instance of a {@link kihira.playerbeacons.api.beacon.AbstractBeacon} for this beacon.
     * @return A beacon instance
     */
    public AbstractBeacon getBeaconInstance(int dimID, int posX, int posY, int posZ, GameProfile gameProfile);

    /**
     * A helper method to reduce casting. Returns the tile entity
     * @return The tile entity
     */
    public TileEntity getTileEntity();
}
