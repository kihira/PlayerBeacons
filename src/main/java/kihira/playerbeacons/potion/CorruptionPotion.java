package kihira.playerbeacons.potion;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;
import java.util.Random;

public class CorruptionPotion extends Potion {

    public CorruptionPotion(int id) {
        super(id, true, 3484199);
        setPotionName("potion.corruption");
        setIconIndex(1, 2);
        setEffectiveness(0.25D);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBase, int level) {
        if (!entityLivingBase.worldObj.isRemote) {
            Random random = new Random();

            //Make enderman nearby angry at player
            double d = (1 + level) * 15;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, entityLivingBase.posX + 1, entityLivingBase.posY + 1, entityLivingBase.posZ + 1).expand(d, d, d);
            axisalignedbb.maxY = entityLivingBase.worldObj.getHeight();
            List list = entityLivingBase.worldObj.selectEntitiesWithinAABB(EntityEnderman.class, axisalignedbb, new IEntitySelector() {
                @Override
                public boolean isEntityApplicable(Entity entity) {
                    return ((EntityEnderman)entity).getEntityToAttack() == null;
                }
            });
            if (list != null && list.size() > 0) {
                EntityEnderman entityEnderman = (EntityEnderman)list.get(random.nextInt(list.size()));
                entityEnderman.setScreaming(true);
                entityEnderman.setTarget(entityLivingBase);
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 80 == 0;
    }
}
