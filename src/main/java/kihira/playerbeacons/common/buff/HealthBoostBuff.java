package kihira.playerbeacons.common.buff;

import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class HealthBoostBuff extends Buff {

    private final UUID uuid = UUID.fromString("092d924e-a1c1-40bc-8c0e-a77ee90b654a");

    public HealthBoostBuff() {
        super("healthBoost");
    }

    @Override
    public float doBuff(EntityPlayer player, IBeacon theBeacon, int crystalCount) {
        if (player.worldObj.getTotalWorldTime() % 10 == 0) {
            IAttributeInstance attribute = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            AttributeModifier attributeModifier = attribute.getModifier(this.uuid);
            //Remove the effect if no crystals
            if (crystalCount == 0 && attributeModifier != null) {
                attribute.removeModifier(attributeModifier);
                player.setHealth(player.getMaxHealth());
            }
            if (theBeacon.getLevels() >= 2) {
                int modifierAmount = MathHelper.clamp_int(crystalCount, 1, theBeacon.getLevels() - 1) * 2;
                if (attributeModifier != null && attributeModifier.getAmount() != modifierAmount) {
                    attribute.removeModifier(attributeModifier);
                    attributeModifier = null;
                    player.setHealth(player.getMaxHealth());
                }
                if (attributeModifier == null) {
                    attribute.applyModifier(new AttributeModifier(this.uuid, "healthBoost", modifierAmount, 0));
                    player.setHealth(player.getMaxHealth());
                }
                return (modifierAmount) * 13;
            }
        }
        return 0;
    }

    @Override
    public float[] getRGBA() {
        return new float[]{0.9F, 0.8F, 0.1F, 1};
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return ClientProxy.potionTextures;
    }

    @Override
    public int[] getUV() {
        return new int[]{36, 234};
    }
}
