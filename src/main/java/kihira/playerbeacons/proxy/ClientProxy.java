package kihira.playerbeacons.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import kihira.foxlib.client.TextureHelper;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.client.particle.EntityBuffParticleFX;
import kihira.playerbeacons.client.render.BlockDefiledSoulPylonRenderer;
import kihira.playerbeacons.client.render.BlockPlayerBeaconRenderer;
import kihira.playerbeacons.client.render.ItemDefiledSoulPylonRenderer;
import kihira.playerbeacons.client.render.ItemPlayerBeaconRenderer;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModBlocks;
import kihira.playerbeacons.common.network.CorruptionUpdateMessage;
import kihira.playerbeacons.common.tileentity.TileEntityDefiledSoulPylon;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

public class ClientProxy extends CommonProxy {

	public static final ResourceLocation playerBeaconTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/model/playerbeacon.png");
	public static final ResourceLocation pylonTextureBase = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/model/pylonbase.png");
	public static final ResourceLocation pylonTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/model/pylon.png");
	public static final ResourceLocation pylonCrystalPortTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/model/crystalports.png");
    public static final ResourceLocation pylonCrystalPortOverlayTexture = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/model/crystalports_overlay.png");
    public static final ResourceLocation potionTextures = new ResourceLocation("textures/gui/container/inventory.png");
	//public static final ResourceLocation santaHatTexture = new ResourceLocation("playerbeacon", "textures/model/santahat.png");

    public static float playerCorruption = 0F;

    //TODO store original skin on disk instead of modified skin in HashMap?
    private final HashMap<String, BufferedImage> playerSkins = new HashMap<String, BufferedImage>();

    @Override
    public void registerMessages() {
        PlayerBeacons.networkWrapper.registerMessage(CorruptionUpdateMessage.CorruptionUpdateMessageHandler.class, CorruptionUpdateMessage.class, 0, Side.CLIENT);
        super.registerMessages();
    }

    @Override
	public void registerRenderers() {
		BlockPlayerBeaconRenderer playerBeaconRenderer = new BlockPlayerBeaconRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlayerBeacon.class, playerBeaconRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockPlayerBeacon), new ItemPlayerBeaconRenderer(playerBeaconRenderer));

		BlockDefiledSoulPylonRenderer blockDefiledSoulPylonRenderer = new BlockDefiledSoulPylonRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDefiledSoulPylon.class, blockDefiledSoulPylonRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockDefiledSoulPylon), new ItemDefiledSoulPylonRenderer(blockDefiledSoulPylonRenderer));
	}

    @Override
    public void spawnBeaconParticle(double targetX, double targetY, double targetZ, TileEntityPlayerBeacon sourceBeacon, Buff buff) {
        Random rand = new Random();
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityBuffParticleFX(targetX + (rand.nextFloat() / 5F), targetY + 0.4F + (rand.nextFloat() / 2F), targetZ + (rand.nextFloat() / 5F), sourceBeacon, buff));
    }

    /**
     * Corrupts the players skin from a specified amount to the specified amount
     * @param player The player
     * @param newCorr The new corr value
     * @param oldCorr The old corr value
     */
    @Override
    public void corruptPlayerSkin(AbstractClientPlayer player, int newCorr, int oldCorr) {
        BufferedImage playerSkin = this.playerSkins.get(player.getCommandSenderName());

        if (playerSkin == null) {
            //We store a copy of the skin we are working on
            playerSkin = TextureHelper.getPlayerSkinAsBufferedImage(player);
        }

        if (playerSkin != null) {
            //Loop over changes needed
            for (int i = oldCorr; i <= newCorr; i++) {
                Random rand = new Random(player.hashCode() * i);
                int x = rand.nextInt(playerSkin.getWidth());
                int y = rand.nextInt(playerSkin.getHeight());
                Color color;
                //Eyes
                if (y == 12 && (x == 9 || x == 10 || x == 13 || x == 14 || x == 41 || x == 42 || x == 45 || x == 46)) {
                    color = new Color(204, 0, 250);
                }
                else {
                    color = new Color(playerSkin.getRGB(x, y)).darker();
                }
                playerSkin.setRGB(x, y, color.getRGB());
            }
            //Upload skin to memory and put copy in map
            TextureHelper.uploadTexture(player.getLocationSkin(), playerSkin);
            this.playerSkins.put(player.getCommandSenderName(), playerSkin);
        }
        else PlayerBeacons.logger.log(Level.INFO, String.format("Unable to corrupt %s, Buffered image is null", player));
    }

    /**
     * Restores the players original skin from the Minecraft texture map
     * @param player The player
     */
    @Override
    public void restorePlayerSkin(AbstractClientPlayer player) {
        BufferedImage playerSkin = TextureHelper.getPlayerSkinAsBufferedImage(player);
        TextureHelper.uploadTexture(player.getLocationSkin(), playerSkin);
    }
}
