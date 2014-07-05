package kihira.playerbeacons.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.client.particle.EntityBuffParticleFX;
import kihira.playerbeacons.client.render.*;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.tileentity.TileEntityDefiledSoulPylon;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.Random;

public class ClientProxy extends CommonProxy {

	public static final ResourceLocation playerBeaconTexture = new ResourceLocation("playerbeacon", "textures/model/playerbeacon.png");
	public static final ResourceLocation pylonTextureBase = new ResourceLocation("playerbeacon", "textures/model/pylonbase.png");
	public static final ResourceLocation pylonTexture = new ResourceLocation("playerbeacon", "textures/model/pylon.png");
	public static final ResourceLocation pylonCrystalPortTexture = new ResourceLocation("playerbeacon", "textures/model/crystalports.png");
    public static final ResourceLocation pylonCrystalPortOverlayTexture = new ResourceLocation("playerbeacon", "textures/model/crystalports_overlay.png");
    public static final ResourceLocation potionTextures = new ResourceLocation("textures/gui/container/inventory.png");
	//public static final ResourceLocation santaHatTexture = new ResourceLocation("playerbeacon", "textures/model/santahat.png");

    public static float playerCorruption = 0F;

    @Override
	public void registerRenderers() {
		BlockPlayerBeaconRenderer playerBeaconRenderer = new BlockPlayerBeaconRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlayerBeacon.class, playerBeaconRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(PlayerBeacons.playerBeaconBlock), new ItemPlayerBeaconRenderer(playerBeaconRenderer));

		BlockDefiledSoulPylonRenderer blockDefiledSoulPylonRenderer = new BlockDefiledSoulPylonRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDefiledSoulPylon.class, blockDefiledSoulPylonRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(PlayerBeacons.defiledSoulPylonBlock), new ItemDefiledSoulPylonRenderer(blockDefiledSoulPylonRenderer));

		//Replace skull renderer
        if (PlayerBeacons.config.overrideSkullRenderer) {
            TileEntityRendererDispatcher.instance.mapSpecialRenderers.remove(TileEntitySkull.class);
            BlockSkullRenderer blockSkullRenderer = new BlockSkullRenderer();
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkull.class, blockSkullRenderer);
        }
	}

    @Override
    public void spawnBeaconParticle(double targetX, double targetY, double targetZ, TileEntityPlayerBeacon sourceBeacon, Buff buff) {
        Random rand = new Random();
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityBuffParticleFX(targetX + (rand.nextFloat() / 5F), targetY + 0.4F + (rand.nextFloat() / 2F), targetZ + (rand.nextFloat() / 5F), sourceBeacon, buff));
    }
}
