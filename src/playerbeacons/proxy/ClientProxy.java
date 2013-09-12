package playerbeacons.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.render.BlockDefiledSoulPylonRenderer;
import playerbeacons.render.BlockPlayerBeaconRenderer;
import playerbeacons.render.ItemDefiledSoulPylonRenderer;
import playerbeacons.render.ItemPlayerBeaconRenderer;
import playerbeacons.tileentity.TileEntityDefiledSoulPylon;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

public class ClientProxy extends CommonProxy {

	public static final ResourceLocation playerBeaconTexture = new ResourceLocation("playerbeacon", "textures/model/playerbeacon.png");
	public static final ResourceLocation pylonTextureBase = new ResourceLocation("playerbeacon", "textures/model/pylonbase.png");
	public static final ResourceLocation pylonTexture = new ResourceLocation("playerbeacon", "textures/model/pylon.png");
	public static final ResourceLocation pylonCrystalPortTexture = new ResourceLocation("playerbeacon", "textures/model/crystalports.png");

	public void registerRenderers() {
		BlockPlayerBeaconRenderer playerBeaconRenderer = new BlockPlayerBeaconRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlayerBeacon.class, playerBeaconRenderer);
		MinecraftForgeClient.registerItemRenderer(PlayerBeacons.config.playerBeaconBlockID, new ItemPlayerBeaconRenderer());

		BlockDefiledSoulPylonRenderer blockDefiledSoulPylonRenderer = new BlockDefiledSoulPylonRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDefiledSoulPylon.class, blockDefiledSoulPylonRenderer);
		MinecraftForgeClient.registerItemRenderer(PlayerBeacons.config.defiledSoulPylonBlockID, new ItemDefiledSoulPylonRenderer());
	}
}
