package kihira.playerbeacons.common.corruption;

import kihira.playerbeacons.api.corruption.CorruptionEffect;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class EndermanAggroCorruption extends CorruptionEffect {

    public EndermanAggroCorruption() {
        super("enderman", CORRUPTION_MAX / 5);
    }

    @Override
    public void init(EntityPlayer player, float corruption) {}

    @Override
    public void onUpdate(EntityPlayer player, float corruption) {
        if (player.worldObj.getTotalWorldTime() % 80 == 0 && player.getRNG().nextInt(50) == 0) {
            //Make enderman nearby angry at player
            double d = (corruption / this.corruptionUnlock) * 15;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(player.posX, player.posY, player.posZ, player.posX + 1, player.posY + 1, player.posZ + 1).expand(d, d, d);
            axisalignedbb.maxY = player.worldObj.getHeight();
            List list = player.worldObj.selectEntitiesWithinAABB(EntityEnderman.class, axisalignedbb, new IEntitySelector() {
                @Override
                public boolean isEntityApplicable(Entity entity) {
                    return ((EntityEnderman)entity).getEntityToAttack() == null;
                }
            });
            if (list != null && list.size() > 0) {
                EntityEnderman entityEnderman = (EntityEnderman)list.get(player.getRNG().nextInt(list.size()));
                entityEnderman.setScreaming(true);
                entityEnderman.setTarget(player);
            }
        }
    }

    @Override
    public void finish(EntityPlayer player, float corruption) {
        player.removePotionEffect(PlayerBeacons.config.corruptionPotionID);
    }
}
