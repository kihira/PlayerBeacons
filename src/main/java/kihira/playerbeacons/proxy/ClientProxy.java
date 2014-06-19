package kihira.playerbeacons.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.client.render.*;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.tileentity.TileEntityDefiledSoulPylon;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

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
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(PlayerBeacons.playerBeaconBlock), new ItemPlayerBeaconRenderer());

		BlockDefiledSoulPylonRenderer blockDefiledSoulPylonRenderer = new BlockDefiledSoulPylonRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDefiledSoulPylon.class, blockDefiledSoulPylonRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(PlayerBeacons.defiledSoulPylonBlock), new ItemDefiledSoulPylonRenderer());

		//Replace skull renderer
        TileEntityRendererDispatcher.instance.mapSpecialRenderers.remove(TileEntitySkull.class);
		BlockSkullRenderer blockSkullRenderer = new BlockSkullRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkull.class, blockSkullRenderer);
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void corruptRandomPixels(EntityPlayer player, float corr) {
        /*
        float prevCorr = player.getEntityData().getFloat("corruption");
        player.getEntityData().setFloat("corruption", corr);

        ThreadDownloadImageData tex = ((EntityPlayerSP) player).getTextureSkin();
        BufferedImage img = ReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, tex, "bufferedImage", "field_110560_d", "g");
        if (img != null) {
            for (int i = 0; i < (corr - prevCorr) / 10; i++) {
                int width = img.getWidth();
                int height = img.getHeight();

                int x = rand.nextInt(width);
                int y = rand.nextInt(height);
                Color color = new Color(img.getRGB(x, y));
                if (color.getRed() + color.getGreen() + color.getRed() > 0) img.setRGB(x, y, color.darker().darker().getRGB());
                ReflectionHelper.setPrivateValue(ThreadDownloadImageData.class, tex, false, "textureUploaded", "field_110559_g", "i");
            }
        }
        */
    }
}
