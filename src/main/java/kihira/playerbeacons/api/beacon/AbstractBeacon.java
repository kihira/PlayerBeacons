package kihira.playerbeacons.api.beacon;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;

/**
 * An instance created each time a new beacon is placed for the associated beacon. This will handle all the logic of
 * the beacon and is ticked independently of the world
 */
public abstract class AbstractBeacon {

    public final int dimID;
    public final int posX;
    public final int posY;
    public final int posZ;

    protected GameProfile ownerGameProfile;
    protected int levels;
    protected float corruption = 0;

    public AbstractBeacon(int dimID, int posX, int posY, int posZ, GameProfile gameProfile) {
        this.dimID = dimID;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.ownerGameProfile = gameProfile;
    }

    /**
     * Checks if the structure for this beacon has changed. If there is no existing structure or structure has changed,
     * this will return false.
     * @param theBeacon The beacon
     * @return If the structure has changed
     */
    public abstract boolean checkStructure(IBeacon theBeacon);

    /**
     * Forms a structure for the beacon. This is called if checkStructure returned false.
     * @param theBeacon The beacon
     */
    public abstract void formStructure(IBeacon theBeacon);

    /**
     * Invalidates the structure. This is called if checkStructure returns false. All possible combinations of the structure
     * should be checked and invalidated for safety
     * @param theBeacon The beacon
     */
    public abstract void invalidateStructure(IBeacon theBeacon);

    /**
     * This is called every tick to do the effects to the passed player. This player would be the owner of the beacon
     * @param entityPlayer The beacon
     */
    public abstract void doEffects(EntityPlayer entityPlayer);

    /**
     * Gets the amount of corruption generated per tick
     * @return The corruption level
     */
    public float getCorruption() {
        return this.corruption;
    }

    /**
     * Gets how many levels this beacon has
     * @return The level count
     */
    public int getLevels() {
        return this.levels;
    }
}
