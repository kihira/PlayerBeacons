package kihira.playerbeacons.api.crystal;

import kihira.playerbeacons.api.beacon.IBeacon;
import net.minecraft.inventory.IInventory;

/**
 * This is implemented by TileEntities that can store other ICrystal items/blocks
 */
public interface ICrystalContainer extends IInventory {

    /**
     * Returns the beacon it is already being used by
     * @return If it has parent. null if none
     */
    public IBeacon getBeacon();

    /**
     * Sets the beacon for this block
     * @param theBeacon The beacon
     */
    public void setBeacon(IBeacon theBeacon);
}
