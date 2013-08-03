package playerbeacons.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import playerbeacons.render.RenderPlayerBeacon;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

public class ClientProxy extends CommonProxy {

	public static int playerBeaconRenderID;

	public void registerRenderers() {
		playerBeaconRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderPlayerBeacon renderPlayerBeacon = new RenderPlayerBeacon(playerBeaconRenderID);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlayerBeacon.class, renderPlayerBeacon);
	}
}
