package playerbeacons.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {

		playerBeaconBlock = new PlayerBeaconBlock(config.playerBeaconBlockID);
		LanguageRegistry.addName(playerBeaconBlock, "Player Beacon");
		GameRegistry.registerBlock(playerBeaconBlock, "Player Beacon");

		playerBeaconBaseBlock = new PlayerBeaconBaseBlock(config.playerBeaconBaseBlockID);
		LanguageRegistry.addName(playerBeaconBaseBlock, "Player Beacon Base");
		GameRegistry.registerBlock(playerBeaconBaseBlock, "Player Beacon Base");

		conductorBlock = new ConductorBlock(config.conductorBlockID);
		LanguageRegistry.addName(conductorBlock, "Conductor");
		GameRegistry.registerBlock(conductorBlock, "Conductor");

		beheaderItem = new BeheaderItem(config.beheaderItemID);
		LanguageRegistry.addName(beheaderItem, "Beheader");
		GameRegistry.registerItem(beheaderItem, "Beheader");

		decapitationEnchantment = new DecapitationEnchantment(config.decapitationEnchantmentID, 5, EnumEnchantmentType.weapon);
		LanguageRegistry.instance().addStringLocalization("enchantment.decapitation", "Decapitation");
	}
}
