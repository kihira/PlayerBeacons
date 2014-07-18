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
     * Sets the amount of levels on this beacon. Used only by {@link kihira.playerbeacons.common.Beacon}
     */
    public void setLevels(int levels);

    /**
     * Returns the tile entity
     * @return
     */
    public TileEntity getTileEntity();
}
