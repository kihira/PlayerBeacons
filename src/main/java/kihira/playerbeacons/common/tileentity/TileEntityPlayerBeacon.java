package kihira.playerbeacons.common.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cpw.mods.fml.common.FMLCommonHandler;
import kihira.foxlib.common.EntityHelper;
import kihira.foxlib.common.EnumHeadType;
import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.api.beacon.IBeacon;
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
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TileEntityPlayerBeacon extends TileEntityMultiBlock implements IBeacon {

    private EnumHeadType headType = EnumHeadType.NONE;
    private GameProfile ownerGameProfile;
    private int levels;

    public float headRotationPitch, headRotationYaw, prevHeadRotationPitch, prevHeadRotationYaw = 0;

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        this.headType = EnumHeadType.fromId(par1NBTTagCompound.getInteger("headType"));
        this.headRotationPitch = par1NBTTagCompound.getFloat("headRotationPitch");
        this.headRotationYaw = par1NBTTagCompound.getFloat("headRotationYaw");
        this.levels = par1NBTTagCompound.getInteger("levels");

        if (par1NBTTagCompound.hasKey("Owner", 10)) {
            this.ownerGameProfile = NBTUtil.func_152459_a(par1NBTTagCompound.getCompoundTag("Owner"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("headType", this.headType.getID());
        par1NBTTagCompound.setFloat("headRotationPitch", this.headRotationPitch);
        par1NBTTagCompound.setFloat("headRotationYaw", this.headRotationYaw);
        par1NBTTagCompound.setInteger("levels", this.levels);

        if (this.ownerGameProfile != null) {
            NBTTagCompound gameProfileTag = new NBTTagCompound();
            NBTUtil.func_152460_a(gameProfileTag, this.ownerGameProfile);
            par1NBTTagCompound.setTag("Owner", gameProfileTag);
        }
    }

    //TODO only sync what is needed
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
        this.refreshGameProfileData();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    public void setOwner(EntityPlayer player) {
        if (!BeaconDataHelper.doesPlayerHaveBeaconForDim(player, this.worldObj.provider.dimensionId)) {
            BeaconDataHelper.setBeaconForDim(player, this, this.worldObj.provider.dimensionId);
            this.ownerGameProfile = player.getGameProfile();
            this.refreshGameProfileData();
        }
        else if (player == null) {
            this.ownerGameProfile = null;
        }
        this.markDirty();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void invalidate() {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer() && this.ownerGameProfile != null) {
            //Remove player beacon data
            EntityPlayer player = Util.getPlayerFromUUID(this.ownerGameProfile.getId()); //Look for by ID first
            if (player == null) MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.ownerGameProfile.getName()); //Then by username
            if (player != null) {
                BeaconDataHelper.setBeaconForDim(player, null, this.worldObj.provider.dimensionId);
            }
        }
        super.invalidate();
    }

    @Override
    public GameProfile getOwnerGameProfile() {
        return this.ownerGameProfile;
    }

    @Override
    public int getLevels() {
        return this.levels;
    }

    @Override
    public void setLevels(int levels) {
        this.levels = levels;
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public void updateEntity() {
        if (this.ownerGameProfile != null) {
            EntityPlayer player = this.worldObj.func_152378_a(this.ownerGameProfile.getId()); //Get player by UUID
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
        if (this.headType != EnumHeadType.PLAYER && this.headType != EnumHeadType.NONE) {
            double d0 = 30D;
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

    @Override
    public boolean canUpdate() {
        return true;
    }
}