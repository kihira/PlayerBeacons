package kihira.playerbeacons.proxy;

import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.entity.AbstractClientPlayer;

public class CommonProxy {

	public void registerRenderers() {}

    public void spawnBeaconParticle(double targetX, double targetY, double targetZ, TileEntityPlayerBeacon sourceBeacon, Buff buff) {}

    public void corruptPlayerSkin(AbstractClientPlayer player, int newCorr, int oldCorr) {}

    public void restorePlayerSkin(AbstractClientPlayer player) {}
}
