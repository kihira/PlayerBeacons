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
import playerbeacons.item.*;
import playerbeacons.tileentity.TileEntityConductor;
import playerbeacons.tileentity.TileEntityPlayerBeacon;
import playerbeacons.util.BeaconDataHandler;
import playerbeacons.util.EventHandler;

@Mod(modid = "PlayerBeacons", name = "Player Beacons", version = "0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class PlayerBeacons {

	public static Config config;

	public static Block playerBeaconBlock;
	public static Block playerBeaconBaseBlock;
	public static Block conductorBlock;

	public static Item beheaderItem;
	public static SpeedCrystalItem speedCrystalItem;
	public static DigCrystalItem digCrystalItem;
	public static JumpCrystalItem jumpCrystalItem;
	public static ResCrystalItem resCrystalItem;

	public static DecapitationEnchantment decapitationEnchantment;

	public static BeaconDataHandler beaconData;

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
		speedCrystalItem = new SpeedCrystalItem(config.speedCrystalItemID);
		LanguageRegistry.addName(speedCrystalItem, "Speed Crystal");
		GameRegistry.registerItem(speedCrystalItem, "speedCrystalItem");
		digCrystalItem = new DigCrystalItem(config.digCrystalItemID);
		LanguageRegistry.addName(digCrystalItem, "Dig Crystal");
		GameRegistry.registerItem(digCrystalItem, "digCrystalItem");
		jumpCrystalItem = new JumpCrystalItem(config.jumpCrystalItemID);
		LanguageRegistry.addName(jumpCrystalItem, "Jump Crystal");
		GameRegistry.registerItem(jumpCrystalItem, "jumpCrystalItem");
		resCrystalItem = new ResCrystalItem(config.resCrystalItemID);
		LanguageRegistry.addName(resCrystalItem, "Resistance Crystal");
		GameRegistry.registerItem(resCrystalItem, "resCrystalItem");

		decapitationEnchantment = new DecapitationEnchantment(config.decapitationEnchantmentID, 5, EnumEnchantmentType.weapon);
		LanguageRegistry.instance().addStringLocalization("enchantment.decapitation", "Decapitation");

		GameRegistry.registerTileEntity(TileEntityPlayerBeacon.class, "playerBeaconBlock");
		GameRegistry.registerTileEntity(TileEntityConductor.class, "conductorBlock");

		LanguageRegistry.instance().addStringLocalization("commands.playerhead.usage", "/playerhead <playername> | Playername is case sensitive!");
		//TODO fix formatting
		LanguageRegistry.instance().addStringLocalization("commands.playerhead.success", "Given a playerhead (%1$s) to %2$s");
		LanguageRegistry.instance().addStringLocalization("death.attack.behead", "%1$s was beheaded");

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandPlayerHead());
		beaconData = new BeaconDataHandler();
	}
}
