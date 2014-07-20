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
    private HashMultiset<ICrystal> crystalMultiset = HashMultiset.create();

    public Beacon(int dimID, int posX, int posY, int posZ, GameProfile gameProfile) {
        this.dimID = dimID;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.ownerGameProfile = gameProfile;
    }

    /**
     * Checks if the structure has changed since it was formed. Will return false if it has changed and true if it is
     * the same.
     * @param theBeacon The beacon
     * @return If it has changed
     */
    public boolean checkStructure(IBeacon theBeacon) {
        World world = theBeacon.getTileEntity().getWorldObj();
        int beaconX = theBeacon.getTileEntity().xCoord;
        int beaconY = theBeacon.getTileEntity().yCoord;
        int beaconZ = theBeacon.getTileEntity().zCoord;
        HashMultiset<IBeaconBase> beaconBaseCount = HashMultiset.create();
        HashMultiset<ICrystal> crystals = HashMultiset.create();
        int levels = this.levels;
        this.levels = 0;

        //Having a block above is not a valid structure
        if (!world.isAirBlock(this.posX, this.posY + 1, this.posZ)) {
            return false;
        }

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

            //Now the pylons
            if (this.levels > 0) {
                int checkX = this.posX - this.levels;
                int checkZ = this.posZ - this.levels;
                int checkY = this.posY - this.levels + 1;
                for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                    ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                    if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                        tileEntity.setBeacon(theBeacon);
                        crystals.addAll(this.countCrystals(tileEntity));
                    }
                    else break;
                }

                checkX = this.posX + this.levels;
                for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                    ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                    if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                        tileEntity.setBeacon(theBeacon);
                        crystals.addAll(this.countCrystals(tileEntity));
                    }
                    else break;
                }

                checkZ = this.posZ + this.levels;
                for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                    ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                    if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                        tileEntity.setBeacon(theBeacon);
                        crystals.addAll(this.countCrystals(tileEntity));
                    }
                    else break;
                }

                checkX = this.posX - this.levels;
                for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                    ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                    if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                        tileEntity.setBeacon(theBeacon);
                        crystals.addAll(this.countCrystals(tileEntity));
                    }
                    else break;
                }
            }

            //If levels and crystal count is the same, return true.
            if (this.levels == levels && crystals.equals(this.crystalMultiset)) {
                return true;
            }
        }
        return false;
    }

    public void formStructure(IBeacon theBeacon) {
        World world = theBeacon.getTileEntity().getWorldObj();
        Multiset<IBeaconBase> beaconBases = HashMultiset.create();
        this.corruptionReduction = 0;
        this.crystalMultiset.clear();

        //Having a block above is not a valid structure
        if (!world.isAirBlock(this.posX, this.posY + 1, this.posZ)) {
            this.levels = 0;
            return;
        }

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
                    this.crystalMultiset.addAll(this.countCrystals(tileEntity));
                }
                else break;
            }

            checkX = this.posX + this.levels;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                    tileEntity.setBeacon(theBeacon);
                    this.crystalMultiset.addAll(this.countCrystals(tileEntity));
                }
                else break;
            }

            checkZ = this.posZ + this.levels;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                    tileEntity.setBeacon(theBeacon);
                    this.crystalMultiset.addAll(this.countCrystals(tileEntity));
                }
                else break;
            }

            checkX = this.posX - this.levels;
            for (int y = 0; ((world.getTileEntity(checkX, checkY + y, checkZ) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                ICrystalContainer tileEntity = (ICrystalContainer) world.getTileEntity(checkX, checkY + y, checkZ);
                if (tileEntity.getBeacon() == theBeacon || tileEntity.getBeacon() == null) {
                    tileEntity.setBeacon(theBeacon);
                    this.crystalMultiset.addAll(this.countCrystals(tileEntity));
                }
                else break;
            }
        }
        theBeacon.setLevels(this.levels);
    }

    public void invalidateStructure(IBeacon theBeacon) {
        World world = theBeacon.getTileEntity().getWorldObj();
        this.levels = 4; //Set to max to be sure we properly clean up

        //Loop through and tell buffs to disable themselves
        EntityPlayer player = world.func_152378_a(this.ownerGameProfile.getId()); //Get player by UUID
        if (player != null) {
            for (Multiset.Entry<ICrystal> crystal : this.crystalMultiset.entrySet()) {
                crystal.getElement().doEffects(player, this, 0);
            }
        }

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
        theBeacon.setLevels(0);
    }

    /**
     * Counts the crystals in the {@link kihira.playerbeacons.api.crystal.ICrystalContainer} and adds them to the
     * {@link #crystalMultiset}
     * @param crystalContainer The container
     */
    private Multiset<ICrystal> countCrystals(ICrystalContainer crystalContainer) {
        Multiset<ICrystal> crystals = HashMultiset.create();
        for (int i = 0; i < crystalContainer.getSizeInventory(); i++) {
            ItemStack itemStack = crystalContainer.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof ICrystal) {
                crystals.add((ICrystal) itemStack.getItem());
            }
        }
        return crystals;
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
        return this.levels;
    }
}
