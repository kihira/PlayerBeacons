package kihira.playerbeacons.common.tileentity;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
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
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityPlayerBeacon extends TileEntity implements IBeacon {

    private Util.EnumHeadType headType = Util.EnumHeadType.NONE;
    private String owner = " ";
    private float corruption = 0;
    private int levels = 0;

    public float headRotationPitch, headRotationYaw, prevHeadRotationPitch, prevHeadRotationYaw = 0;
    private Multiset<ICrystal> crystalMultiset = HashMultiset.create();

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        this.headType = Util.EnumHeadType.fromId(par1NBTTagCompound.getInteger("headType"));
        this.owner = par1NBTTagCompound.getString("owner");
        this.corruption = par1NBTTagCompound.getFloat("badstuff");
        this.headRotationPitch = par1NBTTagCompound.getFloat("headRotationPitch");
        this.headRotationYaw = par1NBTTagCompound.getFloat("headRotationYaw");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("headType", this.headType.getID());
        par1NBTTagCompound.setString("owner", this.owner);
        par1NBTTagCompound.setFloat("badstuff", this.corruption);
        par1NBTTagCompound.setFloat("headRotationPitch", this.headRotationPitch);
        par1NBTTagCompound.setFloat("headRotationYaw", this.headRotationYaw);
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
            this.levels = 0;
            if (!this.getOwner().equals(" ")) {
                for (int i = 1; i <= 4; this.levels = i++) {
                    int j = this.yCoord - i;
                    if (j < 0) break;
                    boolean flag = true;
                    for (int k = this.xCoord - i; k <= this.xCoord + i && flag; ++k) {
                        for (int l = this.zCoord - i; l <= this.zCoord + i; ++l) {
                            if (!(this.worldObj.getBlock(k, j, l) instanceof IBeaconBase) || !((IBeaconBase) this.worldObj.getBlock(k, j, l)).isValidForBeacon(this)) {
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (!flag) break;
                }
            }
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
    public String getOwner() {
        return this.owner;
    }

    @Override
    public int getLevels() {
        return this.levels;
    }

    public void setOwner(EntityPlayer player) {
        if (!BeaconDataHelper.doesPlayerHaveBeaconForDim(player, this.worldObj.provider.dimensionId)) {
            BeaconDataHelper.setBeaconForDim(player, this, this.worldObj.provider.dimensionId);
            this.owner = player.getCommandSenderName();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        else if (player == null) {
            this.owner = " ";
        }
    }

    @Override
    public void invalidate() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && !this.getOwner().equals(" ")) {
            //Remove player beacon data
            EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.getOwner());
            if (player != null) BeaconDataHelper.setBeaconForDim(player, null, this.worldObj.provider.dimensionId);
        }
        super.invalidate();
    }

    private boolean isCloneConstruct() {
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
    }

    private void doEffects() {
        //Verify the owner is valid for receiving effects
        if (!this.getOwner().equals(" ")) {
            EntityPlayer entityPlayer = this.worldObj.getPlayerEntityByName(this.getOwner());
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
        return this.corruption;
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    public void calcPylons() {
        if (this.levels > 0) {
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

/*    @Override
    public void applyCorruption() {
        EntityPlayer player = this.worldObj.getPlayerEntityByName(this.owner);
        if (player != null) {
            if ((this.corruption > 15000) && (this.corruptionLevel == 2)) {
                player.addChatComponentMessage(new ChatComponentText("\u00a74\u00a7oYou feel an unknown force grasp at you from the beyond, pulling you into another dimension"));
                player.travelToDimension(1);
                this.corruptionLevel = 0;
                this.corruption -= this.worldObj.rand.nextInt(9000);
            }
            else if ((this.corruption > 10000) && (this.corruptionLevel == 1)) {
                player.addChatComponentMessage(new ChatComponentText("\u00a74\u00a7oYou feel an unknown force grasp at your soul from the beyond, disorientating you"));
                this.corruptionLevel = 2;
                this.corruption -= this.worldObj.rand.nextInt(2000);
            }
            else if ((this.corruption > 5000) && (this.corruptionLevel == 0)) {
                player.addChatComponentMessage(new ChatComponentText("\u00a74\u00a7oYou feel an unknown force grasp at you from the beyond"));
                this.corruptionLevel = 1;
                this.corruption -= this.worldObj.rand.nextInt(1000);
            }
            if (this.corruptionLevel - 1 > -1) player.addPotionEffect(new PotionEffect(PlayerBeacons.config.corruptionPotionID, 6000, this.corruptionLevel - 1));
        }
    }*/

    @Override
    public void updateEntity() {
        if (!this.getOwner().equals(" ")) {
            EntityPlayer player = this.worldObj.getPlayerEntityByName(this.getOwner());
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
        if (this.headType != Util.EnumHeadType.PLAYER && this.headType != Util.EnumHeadType.NONE && this.levels > 0) {
            double d0 = (double) (this.levels * 7 + 10);
            AxisAlignedBB axisAlignedBB = AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(d0, d0, d0);
            List list = null;

            if (this.headType == Util.EnumHeadType.SKELETON || this.headType == Util.EnumHeadType.WITHERSKELETON) {
                List list1 = this.worldObj.getEntitiesWithinAABB(EntitySkeleton.class, axisAlignedBB);
                list = new ArrayList<Object>();
                for (Object object : list1) {
                    EntitySkeleton skeleton = (EntitySkeleton) object;
                    if (skeleton.getSkeletonType() == this.headType.getID()) list.add(object);
                }
            }
            else if (this.headType == Util.EnumHeadType.ZOMBIE) list = this.worldObj.getEntitiesWithinAABB(EntityZombie.class, axisAlignedBB);
            else if (this.headType == Util.EnumHeadType.CREEPER) list = this.worldObj.getEntitiesWithinAABB(EntityCreeper.class, axisAlignedBB);
            if (list != null && !list.isEmpty()) {
                EntityCreature entityCreature;
                for (Object entry : list) {
                    entityCreature = (EntityCreature) entry;
                    if (!entityCreature.hasPath()) {
                        Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entityCreature, 16, 7, entityCreature.worldObj.getWorldVec3Pool().getVecFromPool(this.xCoord, this.yCoord, this.zCoord));
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
            double d0 = par1Entity.posX - posX - par1Entity.width;
            double d2 = par1Entity.posZ - posZ - par1Entity.width;
            double d1 = par1Entity.posY - posY - par1Entity.height + (double)par1Entity.getEyeHeight() + (this.worldObj.isRemote ? 0.1F : 0.22F);

            double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
            float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            this.headRotationPitch = this.updateRotation(this.headRotationPitch, f3, 5F);
            this.headRotationYaw = this.updateRotation(this.headRotationYaw, f2, 5F);
        }
    }

    private float updateRotation(float currRot, float intendedRot, float maxInc) {
        float f3 = MathHelper.wrapAngleTo180_float(intendedRot - currRot);
        if (f3 > maxInc) f3 = maxInc;
        if (f3 < -maxInc) f3 = -maxInc;
        return currRot + f3;
    }
}