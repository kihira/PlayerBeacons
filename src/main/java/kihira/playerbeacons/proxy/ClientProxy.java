package kihira.playerbeacons.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.render.*;
import kihira.playerbeacons.tileentity.TileEntityDefiledSoulPylon;
import kihira.playerbeacons.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	public static final ResourceLocation playerBeaconTexture = new ResourceLocation("playerbeacon", "textures/model/playerbeacon.png");
	public static final ResourceLocation pylonTextureBase = new ResourceLocation("playerbeacon", "textures/model/pylonbase.png");
	public static final ResourceLocation pylonTexture = new ResourceLocation("playerbeacon", "textures/model/pylon.png");
	public static final ResourceLocation pylonCrystalPortTexture = new ResourceLocation("playerbeacon", "textures/model/crystalports.png");
	public static final ResourceLocation santaHatTexture = new ResourceLocation("playerbeacon", "textures/model/santahat.png");

	public void registerRenderers() {
		BlockPlayerBeaconRenderer playerBeaconRenderer = new BlockPlayerBeaconRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlayerBeacon.class, playerBeaconRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(PlayerBeacons.playerBeaconBlock), new ItemPlayerBeaconRenderer());

		BlockDefiledSoulPylonRenderer blockDefiledSoulPylonRenderer = new BlockDefiledSoulPylonRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDefiledSoulPylon.class, blockDefiledSoulPylonRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(PlayerBeacons.defiledSoulPylonBlock), new ItemDefiledSoulPylonRenderer());

		//Replace skull renderer
        TileEntityRendererDispatcher.instance.mapSpecialRenderers.remove(TileEntitySkull.class);
		BlockSkullRenderer blockSkullRenderer = new BlockSkullRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkull.class, blockSkullRenderer);
		//MinecraftForgeClient.registerItemRenderer(PlayerBeacons.config.defiledSoulPylonBlockID, new ItemDefiledSoulPylonRenderer());
	}
}
