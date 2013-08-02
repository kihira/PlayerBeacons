package playerbeacons.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import playerbeacons.block.ConductorBlock;
import playerbeacons.block.PlayerBeaconBaseBlock;
import playerbeacons.block.PlayerBeaconBlock;
import playerbeacons.item.BeheaderItem;

@Mod(modid = "PlayerBeacons", name = "Player Beacons", version = "0.1")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class PlayerBeacons {

	public Config config;

	public static Block playerBeaconBlock;
	public static Block playerBeaconBaseBlock;
	public static Block conductorBlock;

	public static Item beheaderItem;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent e) {

		config = new Config(e.getSuggestedConfigurationFile());

		playerBeaconBlock = new PlayerBeaconBlock(config.playerBeaconBlockID);
		GameRegistry.registerBlock(playerBeaconBlock, "Player Beacon");
		LanguageRegistry.addName(playerBeaconBaseBlock, "Player Beacon");

		playerBeaconBaseBlock = new PlayerBeaconBaseBlock(config.playerBeaconBaseBlockID);
		GameRegistry.registerBlock(playerBeaconBaseBlock, "Player Beacon Base");
		LanguageRegistry.addName(playerBeaconBaseBlock, "Player Beacon Base");

		conductorBlock = new ConductorBlock(config.conductorBlockID);
		GameRegistry.registerBlock(conductorBlock, "Conductor");
		LanguageRegistry.addName(conductorBlock, "Conductor");

		beheaderItem = new BeheaderItem(config.beheaderItemID);
		GameRegistry.registerItem(beheaderItem, "Beheader");
		LanguageRegistry.addName(beheaderItem, "Beheader");

	}
}
