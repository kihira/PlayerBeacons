package kihira.playerbeacons.client.particle;

import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;

public class EntityBuffParticleFX extends EntityFX {

    private double targetX;
    private double targetY;
    private double targetZ;

    private final EntityPlayer player;

    public EntityBuffParticleFX(EntityPlayer targetPlayer, TileEntityPlayerBeacon beacon, Buff buff) {
        super(targetPlayer.getEntityWorld(), beacon.xCoord + 0.3F, beacon.yCoord + 0.05F, beacon.zCoord + 0.3F, 0, 0, 0);

        if (buff != null) {
            float[] RGBA = buff.getRGBA();
            this.setRBGColorF(RGBA[0], RGBA[1], RGBA[2]);
            this.setAlphaF(RGBA[3]);
        }

        this.setPosition(this.posX + (rand.nextFloat() / 3F), this.posY + (rand.nextFloat() / 3F), this.posZ + (rand.nextFloat() / 3F));
        this.motionX = this.motionY = this.motionZ = 0;

        this.particleMaxAge = 200;

        this.player = targetPlayer;
        this.targetX = targetPlayer.posX;
        this.targetY = targetPlayer.posY;
        this.targetZ = targetPlayer.posZ;
    }

    public EntityBuffParticleFX(double targetX, double targetY, double targetZ, TileEntityPlayerBeacon beacon, Buff buff) {
        super(beacon.getWorldObj(), beacon.xCoord + 0.3F, beacon.yCoord + 0.05F, beacon.zCoord + 0.3F, 0, 0, 0);
        float[] RGBA = buff.getRGBA();
        this.setRBGColorF(RGBA[0], RGBA[1], RGBA[2]);
        this.setAlphaF(RGBA[3]);

        this.setPosition(this.posX + (rand.nextFloat() / 3F), this.posY + (rand.nextFloat() / 3F), this.posZ + (rand.nextFloat() / 3F));
        this.motionX = this.motionY = this.motionZ = 0;

        this.particleMaxAge = 200;

        this.player = null;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;

    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.player != null) {
            this.targetX = this.player.posX;
            this.targetY = this.player.posY;
            this.targetY = this.player.posZ;
        }

        double d0 = this.targetX - this.posX;
        double d1 = this.targetY - this.posY;
        double d2 = this.targetZ - this.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        this.motionX += d0 / d3 * 0.1D;
        this.motionY += d1 / d3 * 0.1D;
        this.motionZ += d2 / d3 * 0.1D;

        if (this.particleAge++ >= this.particleMaxAge)
            setDead();

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
    }
}
