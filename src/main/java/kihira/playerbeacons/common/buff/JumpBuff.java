package kihira.playerbeacons.common.buff;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class JumpBuff extends Buff {

	public JumpBuff() {
		super("jump");
	}

	@Override
	public float doBuff(EntityPlayer player, AbstractBeacon theBeacon, int crystalCount) {
        if (player.worldObj.getTotalWorldTime() % 20 == 0) {
            player.addPotionEffect(new PotionEffect(Potion.jump.id, 300, MathHelper.clamp_int(crystalCount, 0, theBeacon.getLevels()) - 1, true));
        }
        return MathHelper.clamp_int(crystalCount, 0, theBeacon.getLevels()) * 10;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public float[] getRGBA() {
        return new float[] {0.45F, 0.6F, 0.45F, 1F};
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getResourceLocation() {
        return ClientProxy.potionTextures;
    }

    @Override
    public int[] getUV() {
        return new int[]{36, 216};
    }
}
