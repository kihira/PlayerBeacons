package kihira.playerbeacons.common;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.mojang.authlib.GameProfile;
import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.beacon.IBeaconBase;
import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.api.crystal.ICrystalContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Beacon {

    public final int dimID;
    public final int posX;
    public final int posY;
    public final int posZ;
    private int levels;
    private float corruption = 0;
    private float corruptionReduction = 0;
    private GameProfile ownerGameProfile;
    private Multiset<ICrystal> crystalMultiset = HashMultiset.create();

    public Beacon(int dimID, int posX, int posY, int posZ, GameProfile gameProfile) {
        this.dimID = dimID;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.ownerGameProfile = gameProfile;
    }

    /**
     * Checks if this structure is valid. Will return false if not or if it has changed since last check
     * @param theBeacon The beacon
     * @return Whether the structure is valid
     */
    public boolean checkStructure(IBeacon theBeacon) {
        World world = theBeacon.getTileEntity().getWorldObj();
        int beaconX = theBeacon.getTileEntity().xCoord;
        int beaconY = theBeacon.getTileEntity().yCoord;
        int beaconZ = theBeacon.getTileEntity().zCoord;
        Multiset<IBeaconBase> beaconBaseCount = HashMultiset.create();
        this.levels = 0;

        if (this.ownerGameProfile != null && world.isAirBlock(beaconX, beaconY + 1, beaconZ)) {
            for (int i = 1; i <= 4; this.levels = i++) {
                int checkY = beaconY - i;
                if (checkY < 0) break;
                boolean flag = true;

                //Loop through once to check if it is valid
                for (int checkX = beaconX - i; checkX <= beaconX + i && flag; ++checkX) {
                    for (int checkZ = beaconZ - i; checkZ <= beaconZ + i; ++checkZ) {
                        //If not beacon base or not valid, break
                        if (world.getBlock(checkX, checkY, checkZ) instanceof IBeaconBase) {
                            IBeaconBase beaconBase = (IBeaconBase) world.getBlock(checkX, checkY, checkZ);
                            if (beaconBase.getBeacon(world, checkX, checkY, checkZ) == null || beaconBase.getBeacon(world, checkX, checkY, checkZ) == theBeacon) {
                                beaconBaseCount.add(beaconBase);
                            }
                            else {
                                flag = false;
                                break;
                            }
                        }
                        else {
                            flag = false;
                            break;
                        }
                    }
                }
                if (!flag) break;
            }
            if (this.levels > 0) return true;
        }
        return false;
    }

    public void formStructure(IBeacon theBeacon) {
        World world = theBeacon.getTileEntity().getWorldObj();
        Multiset<IBeaconBase> beaconBases = HashMultiset.create();
        this.corruptionReduction = 0;
        this.crystalMultiset.clear();

        //Loop through and own the base blocks
        for (int i = 1; i <= this.levels; i++) {
            int checkY = this.posY - i;
            if (checkY < 0) break;

            //Loop through once to check if it is valid
            for (int checkX = this.posX - i; checkX <= this.posX + i; ++checkX) {
                for (int checkZ = this.posZ - i; checkZ <= this.posZ + i; ++checkZ) {
                    //We assume everything is valid
                    IBeaconBase beaconBase = (IBeaconBase) world.getBlock(checkX, checkY, checkZ);
                    beaconBase.setBeacon(world, checkX, checkY, checkZ, theBeacon);
                    beaconBases.add(beaconBase);
                }
            }
        }
        //Calculate corruption reduction
        for (Multiset.Entry<IBeaconBase> beaconBase : beaconBases.entrySet()) {
            this.corruptionReduction += beaconBase.getElement().getCorruptionReduction(this, beaconBase.getCount());
        }

        //Now the pylons
        if (this.levels > 0) {
            //ImmutableMultiset<ICrystal> copyCrystal = Multisets.copyHighestCountFirst(this.crystalMultiset);
            int checkX = this.posX - this.levels;
            int checkZ = this.posZ - this.levels;
            int checkY = this.posY - this.levels + 1;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                    tileEntity.setBeacon(theBeacon);
                    this.doCrystals(tileEntity);
                }
                else break;
            }

            checkX = this.posX + this.levels;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                    tileEntity.setBeacon(theBeacon);
                    this.doCrystals(tileEntity);
                }
                else break;
            }

            checkZ = this.posZ + this.levels;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                    tileEntity.setBeacon(theBeacon);
                    this.doCrystals(tileEntity);
                }
                else break;
            }

            checkX = this.posX - this.levels;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                    tileEntity.setBeacon(theBeacon);
                    this.doCrystals(tileEntity);
                }
                else break;
            }

            //Loop through and check if any crystals are missing from previous. If so, do effects with count 0
            //TODO should a global list of ICrystals be maintained? Then we just loop though that in doEffects so we can easily send 0
/*            if (entityPlayer != null && copyCrystal.size() > 0) {
                for (Multiset.Entry<ICrystal> crystal : copyCrystal.entrySet()) {
                    if (!this.crystalMultiset.contains(crystal.getElement())) crystal.getElement().doEffects(entityPlayer, this, 0);
                }
            }*/
        }
    }

    public void invalidateStructure(IBeacon theBeacon) {
        World world = theBeacon.getTileEntity().getWorldObj();
        this.levels = 4; //Set to max to be sure we properly clean up

        //Loop through and own the base blocks
        for (int i = 1; i <= this.levels; i++) {
            int checkY = this.posY - i;
            if (checkY < 0) break;

            for (int checkX = this.posX - i; checkX <= this.posX + i; ++checkX) {
                for (int checkZ = this.posZ - i; checkZ <= this.posZ + i; ++checkZ) {
                    if (world.getBlock(checkX, checkY, checkZ) instanceof IBeaconBase) {
                        IBeaconBase beaconBase = (IBeaconBase) world.getBlock(checkX, checkY, checkZ);
                        if (beaconBase.getBeacon(world, checkX, checkY, checkZ) == theBeacon) beaconBase.setBeacon(world, checkX, checkY, checkZ, null);
                    }
                }
            }
        }

        //We want to check the pylons on every possible level and invalidate them properly
        for (this.levels = 1; this.levels <= 4; this.levels++) {

            int checkX = this.posX - this.levels;
            int checkZ = this.posZ - this.levels;
            int checkY = this.posY - this.levels + 1;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon) tileEntity.setBeacon(null);
            }

            checkX = this.posX + this.levels;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon) tileEntity.setBeacon(null);
            }

            checkZ = this.posZ + this.levels;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon) tileEntity.setBeacon(null);
            }

            checkX = this.posX - this.levels;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon) tileEntity.setBeacon(null);
            }
        }
        this.corruptionReduction = 0;
        this.corruption = 0;
        this.levels = 0;
        this.crystalMultiset.clear();
    }

    /**
     * Counts the crystals in the {@link kihira.playerbeacons.api.crystal.ICrystalContainer} and adds them to the
     * {@link #crystalMultiset}
     * @param crystalContainer The container
     */
    private void doCrystals(ICrystalContainer crystalContainer) {
        for (int i = 0; i < crystalContainer.getSizeInventory(); i++) {
            ItemStack itemStack = crystalContainer.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof ICrystal) {
                this.crystalMultiset.add((ICrystal) itemStack.getItem());
            }
        }
    }


    /**
     * Does the buffs on the player provided who is also the owner
     * @param entityPlayer The player
     */
    public void doEffects(EntityPlayer entityPlayer) {
        this.corruption = 0;
        //Verify the owner is valid for receiving effects
        if (entityPlayer.getGameProfile().equals(this.ownerGameProfile)) {
            if (entityPlayer.dimension == this.dimID) {
                //Loop through the crystals this beacon has detected
                for (Multiset.Entry<ICrystal> entry : this.crystalMultiset.entrySet()) {
                    this.corruption += entry.getElement().doEffects(entityPlayer, this, entry.getCount());
                    PlayerBeacons.logger.debug("Doing effects for the crystal %s(%d)", entry.getElement().getClass().toString(), entry.getCount());
                }
            }
        }
    }

    public float getCorruption() {
        return MathHelper.clamp_float(this.corruption - this.corruptionReduction - (this.levels * 20), 0, Float.MAX_VALUE);
    }

    public int getLevels() {
        return levels;
    }
}
