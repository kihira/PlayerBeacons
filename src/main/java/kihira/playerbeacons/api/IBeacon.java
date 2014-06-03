package kihira.playerbeacons.api;

import net.minecraft.tileentity.TileEntity;

//TODO convert to scala?
public interface IBeacon {

    public String getOwner();

    public int getLevels();

    public float getCorruption();

    public void setCorruption(float newCorruption);

    public TileEntity getTileEntity();
}
