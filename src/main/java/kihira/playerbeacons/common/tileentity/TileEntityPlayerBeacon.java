package kihira.playerbeacons.common.tileentity;

import com.google.common.collect.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cpw.mods.fml.common.FMLCommonHandler;
import kihira.foxlib.common.EnumHeadType;
import kihira.foxlib.common.gson.EntityHelper;
import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.beacon.IBeaconBase;
import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.api.crystal.ICrystalContainer;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.util.Util;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityPlayerBeacon extends TileEntity implements IBeacon {

    private EnumHeadType headType = EnumHeadType.NONE;
    private GameProfile ownerGameProfile;
    private float corruption = 0;
    private float corruptionReduction = 0;
    private int levels = 0;

    public float headRotationPitch, headRotationYaw, prevHeadRotationPitch, prevHeadRotationYaw = 0;
    private Multiset<ICrystal> crystalMultiset = HashMultiset.create();

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        this.headType = EnumHeadType.fromId(par1NBTTagCompound.getInteger("headType"));
        this.corruption = par1NBTTagCompound.getFloat("badstuff");
        this.headRotationPitch = par1NBTTagCompound.getFloat("headRotationPitch");
        this.headRotationYaw = par1NBTTagCompound.getFloat("headRotationYaw");

        if (par1NBTTagCompound.hasKey("Owner", 10)) {
            this.ownerGameProfile = NBTUtil.func_152459_a(par1NBTTagCompound.getCompoundTag("Owner"));
        }

        this.refreshGameProfileData();
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("headType", this.headType.getID());
        par1NBTTagCompound.setFloat("badstuff", this.corruption);
        par1NBTTagCompound.setFloat("headRotationPitch", this.headRotationPitch);
        par1NBTTagCompound.setFloat("headRotationYaw", this.headRotationYaw);

        if (this.getOwnerGameProfile() != null) {
            NBTTagCompound gameProfileTag = new NBTTagCompound();
            NBTUtil.func_152460_a(gameProfileTag, this.getOwnerGameProfile());
            par1NBTTagCompound.setTag("Owner", gameProfileTag);
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    @Override
    public boolean isBeaconValid() {
        if (this.worldObj.getTotalWorldTime() % 20 == 0) {
            int levels = 0;
            this.corruptionReduction = 0;
            //Check if there is an owner and that there is no block above
            if (this.getOwnerGameProfile() != null && this.worldObj.isAirBlock(this.xCoord, this.yCoord + 1, this.zCoord)) {
                Multiset<IBeaconBase> beaconBaseCount = HashMultiset.create();
                for (int i = 1; i <= 4; levels = i++) {
                    int y = this.yCoord - i;
                    if (y < 0) break;
                    boolean flag = true;
                    for (int x = this.xCoord - i; x <= this.xCoord + i && flag; ++x) {
                        for (int z = this.zCoord - i; z <= this.zCoord + i; ++z) {
                            //If not beacon base or not valid, break
                            if (this.worldObj.getBlock(x, y, z) instanceof IBeaconBase) {
                                IBeaconBase beaconBase = (IBeaconBase) this.worldObj.getBlock(x, y, z);
                                if (beaconBase.isValidForBeacon(this)) {
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
                for (Multiset.Entry<IBeaconBase> beaconBase : beaconBaseCount.entrySet()) {
                    this.corruptionReduction += beaconBase.getElement().getCorruptionReduction(this, beaconBase.getCount());
                }
            }
            this.levels = levels;

            //Calculate pylons to get crystals
            this.calcPylons();
        }
        return this.levels > 0;
/*		AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) this.xCoord, (double) this.yCoord, (double) this.zCoord, (double) (this.xCoord), (double) (this.yCoord + 1), (double) (this.zCoord));
		List entities = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
		if ((entities != null) && (this.isCloneConstruct())) {
            //TODO Clone construct
			EntityPlayer entityPlayer = (EntityPlayer) entities.get(0);
		}*/
    }

    @Override
    public void update() {
        //Reset corruption change amount
        this.corruption = 0;
        //Do the effects
        this.doEffects();
    }

    @Override
    public GameProfile getOwnerGameProfile() {
        return this.ownerGameProfile;
    }

    @Override
    public int getLevels() {
        return this.levels;
    }

    public void setOwner(EntityPlayer player) {
        if (!BeaconDataHelper.doesPlayerHaveBeaconForDim(player, this.worldObj.provider.dimensionId)) {
            BeaconDataHelper.setBeaconForDim(player, this, this.worldObj.provider.dimensionId);
            this.ownerGameProfile = player.getGameProfile();
            this.refreshGameProfileData();;
        }
        else if (player == null) {
            this.ownerGameProfile = null;
        }
        this.markDirty();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void invalidate() {
        if (FMLCommonHandler.instance().getSide().isServer() && this.getOwnerGameProfile() != null) {
            //Remove player beacon data
            EntityPlayer player = Util.getPlayerFromUUID(this.getOwnerGameProfile().getId()); //Look for by ID first
            if (player == null) MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.getOwnerGameProfile().getName()); //Then by username
            if (player != null) {
                BeaconDataHelper.setBeaconForDim(player, null, this.worldObj.provider.dimensionId);
            }
        }
        super.invalidate();
    }

/*    private boolean isCloneConstruct() {
        if (this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) == Blocks.skull) return false;
        //Check 5x5 underneath and above
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (!(this.worldObj.getBlock(this.xCoord + i, this.yCoord - 1, this.zCoord + j) instanceof IBeaconBase)) return false;
                if (!(this.worldObj.getBlock(this.xCoord + i, this.yCoord + 3, this.zCoord + j) instanceof IBeaconBase)) return false;
            }
        }
        //Check pylons
        for (int i = 0; i <= 3; i++) {
            if (this.worldObj.getBlock(this.xCoord + 2, this.yCoord + i, this.zCoord + 2) != PlayerBeacons.defiledSoulPylonBlock) return false;
            if (this.worldObj.getBlock(this.xCoord + 2, this.yCoord + i, this.zCoord - 2) != PlayerBeacons.defiledSoulPylonBlock) return false;
            if (this.worldObj.getBlock(this.xCoord - 2, this.yCoord + i, this.zCoord + 2) != PlayerBeacons.defiledSoulPylonBlock) return false;
            if (this.worldObj.getBlock(this.xCoord - 2, this.yCoord + i, this.zCoord - 2) != PlayerBeacons.defiledSoulPylonBlock) return false;
        }
        return true;
    }*/

    private void doEffects() {
        //Verify the ownerUUID is valid for receiving effects
        if (this.getOwnerGameProfile() != null) {
            EntityPlayer entityPlayer = this.worldObj.func_152378_a(this.getOwnerGameProfile().getId()); //Get player by UUID
            if (entityPlayer != null && entityPlayer.dimension == this.worldObj.provider.dimensionId) {
                //Loop through the crystals this beacon has detected
                for (Multiset.Entry<ICrystal> entry : this.crystalMultiset.entrySet()) {
                    this.corruption += entry.getElement().doEffects(entityPlayer, this, entry.getCount());
                    //PlayerBeacons.logger.debug("Doing effects for the crystal %s(%d), changing corruption by %d", entry.getElement().getClass().toString(), entry.getCount(), corruptionChange);
                }
            }
        }
    }

    @Override
    public float getCorruption() {
        //Return corruption minus the reduction from base blocks and the natural reduction from beacon
        return MathHelper.clamp_float(this.corruption - this.corruptionReduction - (this.levels * 20), 0, Float.MAX_VALUE);
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    public void calcPylons() {
        if (this.levels > 0) {
            ImmutableMultiset<ICrystal> copyCrystal = Multisets.copyHighestCountFirst(this.crystalMultiset);
            EntityPlayer entityPlayer = Util.getPlayerFromUUID(this.getOwnerGameProfile().getId());
            this.crystalMultiset.clear();

            for (int y = 0; ((this.worldObj.getTileEntity(this.xCoord - this.levels, this.yCoord - this.levels + 1 + y, this.zCoord - this.levels) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                this.doCrystals(this.xCoord - this.levels, this.yCoord - this.levels + 1 + y, this.zCoord - this.levels);
            }
            for (int y = 0; ((this.worldObj.getTileEntity(this.xCoord + this.levels, this.yCoord - this.levels + 1 + y, this.zCoord - this.levels) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                this.doCrystals(this.xCoord + this.levels, this.yCoord - this.levels + 1 + y, this.zCoord - this.levels);
            }
            for (int y = 0; ((this.worldObj.getTileEntity(this.xCoord + this.levels, this.yCoord - this.levels + 1 + y, this.zCoord + this.levels) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                this.doCrystals(this.xCoord + this.levels, this.yCoord - this.levels + 1 + y, this.zCoord + this.levels);
            }
            for (int y = 0; ((this.worldObj.getTileEntity(this.xCoord - this.levels, this.yCoord - this.levels + 1 + y, this.zCoord + this.levels) instanceof ICrystalContainer) && (y < (1 + this.levels))); y++) {
                this.doCrystals(this.xCoord - this.levels, this.yCoord - this.levels + 1 + y, this.zCoord + this.levels);
            }

            //Loop through and check if any crystals are missing from previous. If so, do effects with count 0
            //TODO should a global list of ICrystals be maintained? Then we just loop though that in doEffects so we can easily send 0
            if (entityPlayer != null && copyCrystal.size() > 0) {
                for (Multiset.Entry<ICrystal> crystal : copyCrystal.entrySet()) {
                    if (!this.crystalMultiset.contains(crystal.getElement())) crystal.getElement().doEffects(entityPlayer, this, 0);
                }
            }
        }
    }

    private void doCrystals(int x, int y, int z) {
        ICrystalContainer crystalContainer = (ICrystalContainer) this.worldObj.getTileEntity(x, y, z);

        for (int i = 0; i < crystalContainer.getSizeInventory(); i++) {
            ItemStack itemStack = crystalContainer.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof ICrystal) {
                if (itemStack.attemptDamageItem(1, new Random())) {
                    crystalContainer.setInventorySlotContents(i, null);
                }
                else {
                    this.crystalMultiset.add((ICrystal) itemStack.getItem());
                }
            }
        }
    }

    @Override
    public void updateEntity() {
        if (this.getOwnerGameProfile() != null) {
            EntityPlayer player = this.worldObj.func_152378_a(this.getOwnerGameProfile().getId()); //Get player by UUID
            if (player != null) {
                this.faceEntity(player, this.xCoord, this.yCoord, this.zCoord); //Update rotation

                //Only send packet updates if player is greater then client knows about and is server
                if (!this.worldObj.isRemote && (this.worldObj.getWorldTime() % 5 == 0) && (this.getDistanceFrom(player.posX, player.posY, player.posZ) > MinecraftServer.getServer().getConfigurationManager().getEntityViewDistance() * 16)) {
                    this.faceEntity(player, this.xCoord, this.yCoord, this.zCoord); //Update rotation
                    if ((this.headRotationPitch != this.prevHeadRotationPitch) || (this.headRotationYaw != this.prevHeadRotationYaw)) {
                        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                    }
                }
            }
        }
        else if ((this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) == Blocks.dragon_egg) && (PlayerBeacons.config.enableEasterEgg)) {
            this.worldObj.func_147480_a(this.xCoord, this.yCoord + 1, this.zCoord, false); //Destroy block
            EntityDragon dragon = new EntityDragon(this.worldObj);
            dragon.setLocationAndAngles(this.xCoord, this.yCoord + 30, this.zCoord, 0, 0);
            this.worldObj.spawnEntityInWorld(dragon);
        }
        if (this.headType != EnumHeadType.PLAYER && this.headType != EnumHeadType.NONE && this.levels > 0) {
            double d0 = (double) (this.levels * 7 + 10);
            AxisAlignedBB axisAlignedBB = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(d0, d0, d0);
            List list = null;

            if (this.headType == EnumHeadType.SKELETON || this.headType == EnumHeadType.WITHERSKELETON) {
                List list1 = this.worldObj.getEntitiesWithinAABB(EntitySkeleton.class, axisAlignedBB);
                list = new ArrayList<Object>();
                for (Object object : list1) {
                    EntitySkeleton skeleton = (EntitySkeleton) object;
                    if (skeleton.getSkeletonType() == this.headType.getID()) list.add(object);
                }
            }
            else if (this.headType == EnumHeadType.ZOMBIE) list = this.worldObj.getEntitiesWithinAABB(EntityZombie.class, axisAlignedBB);
            else if (this.headType == EnumHeadType.CREEPER) list = this.worldObj.getEntitiesWithinAABB(EntityCreeper.class, axisAlignedBB);
            if (list != null && !list.isEmpty()) {
                EntityCreature entityCreature;
                for (Object entry : list) {
                    entityCreature = (EntityCreature) entry;
                    if (!entityCreature.hasPath()) {
                        Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entityCreature, 16, 7, Vec3.createVectorHelper(this.xCoord, this.yCoord, this.zCoord));
                        PathNavigate entityPathNavigate = entityCreature.getNavigator();
                        if (entityPathNavigate != null && vec3 != null) {
                            PathEntity entityPathEntity = entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
                            if (entityPathEntity != null) entityPathNavigate.setPath(entityPathEntity, 1.1D);
                        }
                    }
                }
            }
        }
    }

    private void faceEntity(EntityLivingBase par1Entity, double posX, double posY, double posZ) {
        this.prevHeadRotationPitch = this.headRotationPitch;
        this.prevHeadRotationYaw = this.headRotationYaw;

        if (par1Entity != null) {
            float[] pitchYaw = EntityHelper.getPitchYawToEntity(posX, posY, posZ, par1Entity);

            this.headRotationPitch = EntityHelper.updateRotation(this.headRotationPitch, pitchYaw[0], 5F);
            this.headRotationYaw = EntityHelper.updateRotation(this.headRotationYaw, pitchYaw[1], 5F);
        }
    }

    /**
     * This loads texture data into the players profile if it is missing
     */
    private void refreshGameProfileData() {
        if (this.ownerGameProfile != null && !StringUtils.isNullOrEmpty(this.ownerGameProfile.getName())) {
            if (!this.ownerGameProfile.isComplete() || !this.ownerGameProfile.getProperties().containsKey("textures")) {
                GameProfile gameprofile = MinecraftServer.getServer().func_152358_ax().func_152655_a(this.ownerGameProfile.getName());
                if (gameprofile != null) {
                    Property property = (Property) Iterables.getFirst(gameprofile.getProperties().get("textures"), (Object) null);
                    if (property == null) {
                        gameprofile = MinecraftServer.getServer().func_147130_as().fillProfileProperties(gameprofile, true);
                    }

                    this.ownerGameProfile = gameprofile;
                    this.markDirty();
                    this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                }
            }
        }
    }
}