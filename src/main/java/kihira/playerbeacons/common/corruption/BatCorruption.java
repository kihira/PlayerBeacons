package kihira.playerbeacons.common.corruption;

import kihira.playerbeacons.api.corruption.CorruptionEffect;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;

public class BatCorruption extends CorruptionEffect {

    public BatCorruption() {
        super("bats", CORRUPTION_MAX / 20);
    }

    @Override
    public void init(EntityPlayer player, float corruption) {}

    @Override
    public void onUpdate(EntityPlayer player, float corruption) {
        if (player.worldObj.getTotalWorldTime() % 200 == 0 && player.getRNG().nextInt(60) == 0) {
            EntityBat entityBat = new EntityBat(player.worldObj);
            entityBat.setPosition(player.posX, player.posY, player.posZ);
            player.worldObj.spawnEntityInWorld(entityBat);
        }
    }

    @Override
    public void finish(EntityPlayer player, float corruption) {}
}
