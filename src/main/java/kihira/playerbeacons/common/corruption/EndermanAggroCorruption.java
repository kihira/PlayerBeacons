package kihira.playerbeacons.common.corruption;

import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.corruption.CorruptionEffect;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

public class EndermanAggroCorruption extends CorruptionEffect {

    public EndermanAggroCorruption() {
        super("enderman", 5000);
    }

    @Override
    public void init(EntityPlayer player, IBeacon beacon) {}

    @Override
    public void onUpdate(EntityPlayer player, IBeacon beacon) {
        if (player.worldObj.getTotalWorldTime() % 10 == 0) {
            player.addPotionEffect(new PotionEffect(PlayerBeacons.config.corruptionPotionID, MathHelper.clamp_int((int) (beacon.getCorruption() / 5000F), 0, 3), 20));
        }
    }

    @Override
    public void finish(EntityPlayer player) {
        player.removePotionEffect(PlayerBeacons.config.corruptionPotionID);
    }
}
