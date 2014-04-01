package kihira.playerbeacons.api;

import net.minecraft.tileentity.TileEntity;

public interface IBeacon {

    public String getOwner();

    public int getLevels();

    public float getCorruption();

    public void setCorruption(float newCorruption, boolean adjustLevel);

    public TileEntity getTileEntity();

    public void applyCorruption();
}
