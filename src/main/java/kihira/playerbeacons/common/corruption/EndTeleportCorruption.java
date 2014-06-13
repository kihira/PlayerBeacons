package kihira.playerbeacons.common.corruption;

import kihira.playerbeacons.api.corruption.CorruptionEffect;
import net.minecraft.entity.player.EntityPlayer;

public class EndTeleportCorruption extends CorruptionEffect {

    public EndTeleportCorruption() {
        super("endTeleport", CORRUPTION_MAX);
    }

    @Override
    public void init(EntityPlayer player, float corruption) {
        player.travelToDimension(1);
    }

    @Override
    public void onUpdate(EntityPlayer player, float corruption) {}

    @Override
    public void finish(EntityPlayer player, float corruption) {}
}
