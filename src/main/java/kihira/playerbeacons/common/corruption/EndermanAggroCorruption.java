package kihira.playerbeacons.common.corruption;

import kihira.playerbeacons.api.corruption.CorruptionEffect;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

public class EndermanAggroCorruption extends CorruptionEffect {

    public EndermanAggroCorruption() {
        super("enderman", 10000);
    }

    @Override
    public void init(EntityPlayer player, float corruption) {}

    @Override
    public void onUpdate(EntityPlayer player, float corruption) {
        player.addPotionEffect(new PotionEffect(PlayerBeacons.config.corruptionPotionID, 20, (int) MathHelper.clamp_float(((corruption) / this.corruptionUnlock) - 1, 0, 2)));
    }

    @Override
    public void finish(EntityPlayer player, float corruption) {
        player.removePotionEffect(PlayerBeacons.config.corruptionPotionID);
    }
}
