package kihira.playerbeacons.api.throttle;

import java.util.List;

/**
 * This is implemented by TileEntities that can store other IThrottle items/blocks
 */
public interface ICrystalContainer {

    public List<ICrystal> getCrystalList();
}
