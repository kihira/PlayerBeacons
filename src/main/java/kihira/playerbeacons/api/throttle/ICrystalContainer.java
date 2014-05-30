package kihira.playerbeacons.api.throttle;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * This is implemented by TileEntities that can store other IThrottle items/blocks
 */
public interface ICrystalContainer extends IInventory {

    public List<ICrystal> getCrystalList();

    public TileEntity getTileEntity();
}
