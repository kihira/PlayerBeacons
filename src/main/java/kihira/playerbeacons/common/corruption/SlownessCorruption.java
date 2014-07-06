package kihira.playerbeacons.common.corruption;

import kihira.playerbeacons.api.corruption.CorruptionEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

public class SlownessCorruption extends CorruptionEffect {

    public SlownessCorruption() {
        super("slowness", CORRUPTION_MAX / 10);
    }

    @Override
    public void init(EntityPlayer player, float corruption) {}

    @Override
    public void onUpdate(EntityPlayer player, float corruption) {
        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 80, (int) MathHelper.clamp_float((corruption / this.corruptionUnlock) - 1, 0, 3)));
        player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 80, (int) MathHelper.clamp_float((corruption / this.corruptionUnlock) - 1, 0, 3)));
    }

    @Override
    public void finish(EntityPlayer player, float corruption) {}
}
