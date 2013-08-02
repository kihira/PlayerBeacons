package playerbeacons.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import playerbeacons.block.ConductorBlock;
import playerbeacons.block.PlayerBeaconBaseBlock;
import playerbeacons.block.PlayerBeaconBlock;
import playerbeacons.item.BeheaderItem;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

@Mod(modid = "PlayerBeacons", name = "Player Beacons", version = "0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class PlayerBeacons {

	public static Config config;

	public static Block playerBeaconBlock;
	public static Block playerBeaconBaseBlock;
	public static Block conductorBlock;

	public static Item beheaderItem;

	public static DecapitationEnchantment decapitationEnchantment;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent e) {

		config = new Config(e.getSuggestedConfigurationFile());

		playerBeaconBlock = new PlayerBeaconBlock(config.playerBeaconBlockID);
		LanguageRegistry.addName(playerBeaconBlock, "Player Beacon");
		GameRegistry.registerBlock(playerBeaconBlock, "playerBeaconBlock");

		playerBeaconBaseBlock = new PlayerBeaconBaseBlock(config.playerBeaconBaseBlockID);
		LanguageRegistry.addName(playerBeaconBaseBlock, "Player Beacon Base");
		GameRegistry.registerBlock(playerBeaconBaseBlock, "playerBeaconBaseBlock");

		conductorBlock = new ConductorBlock(config.conductorBlockID);
		LanguageRegistry.addName(conductorBlock, "Conductor");
		GameRegistry.registerBlock(conductorBlock, "conductorBlock");

		beheaderItem = new BeheaderItem(config.beheaderItemID);
		LanguageRegistry.addName(beheaderItem, "Beheader");
		GameRegistry.registerItem(beheaderItem, "beheaderItem");

		decapitationEnchantment = new DecapitationEnchantment(config.decapitationEnchantmentID, 5, EnumEnchantmentType.weapon);
		LanguageRegistry.instance().addStringLocalization("enchantment.decapitation", "Decapitation");

		GameRegistry.registerTileEntity(TileEntityPlayerBeacon.class, "playerBeaconBlock");

		LanguageRegistry.instance().addStringLocalization("commands.playerhead.usage", "/playerhead <playername> | Playername is case sensitive!");
		//TODO fix formatting
		LanguageRegistry.instance().addStringLocalization("commands.playerhead.success", "Given a playerhead (%d) to %s");

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandPlayerHead());
	}
}
