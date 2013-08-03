package playerbeacons.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.render.ItemPlayerBeaconRenderer;
import playerbeacons.render.ItemPylonRenderer;
import playerbeacons.render.PlayerBeaconRenderer;
import playerbeacons.render.PylonRenderer;
import playerbeacons.tileentity.TileEntityConductor;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

public class ClientProxy extends CommonProxy {

	public static ResourceLocation playerBeaconTexture = new ResourceLocation("playerbeacon", "textures/model/playerbeacon.png");
	public static ResourceLocation pylonTextureBase = new ResourceLocation("playerbeacon", "textures/model/pylonbase.png");
	public static ResourceLocation pylonTexture = new ResourceLocation("playerbeacon", "textures/model/pylon.png");

	public void registerRenderers() {
		PlayerBeaconRenderer playerBeaconRenderer = new PlayerBeaconRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlayerBeacon.class, playerBeaconRenderer);
		MinecraftForgeClient.registerItemRenderer(PlayerBeacons.config.playerBeaconBlockID, new ItemPlayerBeaconRenderer());

		PylonRenderer pylonRenderer = new PylonRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConductor.class, pylonRenderer);
		MinecraftForgeClient.registerItemRenderer(PlayerBeacons.config.conductorBlockID, new ItemPylonRenderer());
	}
}
