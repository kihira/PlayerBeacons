package kihira.playerbeacons.proxy;

import cpw.mods.fml.relauncher.Side;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.network.CorruptionUpdateMessage;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.entity.AbstractClientPlayer;

public class CommonProxy {

    public void registerMessages() {
        PlayerBeacons.networkWrapper.registerMessage(CorruptionUpdateMessage.CorruptionUpdateMessageHandler.class, CorruptionUpdateMessage.class, 0, Side.SERVER);
    }

	public void registerRenderers() {}

    public void spawnBeaconParticle(double targetX, double targetY, double targetZ, TileEntityPlayerBeacon sourceBeacon, Buff buff) {}

    public void corruptPlayerSkin(AbstractClientPlayer player, int newCorr, int oldCorr) {}

    public void restorePlayerSkin(AbstractClientPlayer player) {}
}
