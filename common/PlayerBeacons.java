package playerbeacons.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import playerbeacons.block.BlockDefiledSoulConductor;
import playerbeacons.block.BlockDefiledSoulPylon;
import playerbeacons.block.BlockPlayerBeacon;
import playerbeacons.item.*;
import playerbeacons.proxy.CommonProxy;
import playerbeacons.tileentity.TileEntityDefiledSoulPylon;
import playerbeacons.tileentity.TileEntityPlayerBeacon;
import playerbeacons.util.BeaconDataHandler;
import playerbeacons.util.EventHandler;

@Mod(modid = "PlayerBeacons", name = "Player Beacons", version = "0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class PlayerBeacons {

	public static Config config;

	public static Block playerBeaconBlock;
	public static Block defiledSoulConductorBlock;
	public static Block defiledSoulPylonBlock;

	public static Item beheaderItem;
	public static CrystalItem crystalItem;
	public static SpeedCrystalItem speedCrystalItem;
	public static DigCrystalItem digCrystalItem;
	public static JumpCrystalItem jumpCrystalItem;
	public static ResCrystalItem resCrystalItem;

	public static EnchantmentDecapitation enchantmentDecapitation;

	public static BeaconDataHandler beaconData;

	@SidedProxy(clientSide = "playerbeacons.proxy.ClientProxy", serverSide = "playerbeacons.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance("PlayerBeacons")
	public static PlayerBeacons instance;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent e) {

		config = new Config(e.getSuggestedConfigurationFile());

		playerBeaconBlock = new BlockPlayerBeacon(config.playerBeaconBlockID);
		LanguageRegistry.addName(playerBeaconBlock, "Player Beacon");
		GameRegistry.registerBlock(playerBeaconBlock, "playerBeaconBlock");
		defiledSoulConductorBlock = new BlockDefiledSoulConductor(config.defiledSoulConductorBlockID);
		LanguageRegistry.addName(defiledSoulConductorBlock, "Defiled Soul Conductor");
		GameRegistry.registerBlock(defiledSoulConductorBlock, "defiledSoulConductorBlock");
		defiledSoulPylonBlock = new BlockDefiledSoulPylon(config.defiledSoulPylonBlockID);
		LanguageRegistry.addName(defiledSoulPylonBlock, "Defiled Soul Pylon");
		GameRegistry.registerBlock(defiledSoulPylonBlock, "defiledSoulPylonBlock");

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
		crystalItem = new CrystalItem(config.crystalItemID);
		LanguageRegistry.addName(crystalItem, "Depleted Crystal");
		GameRegistry.registerItem(crystalItem, "crystalItem");


		enchantmentDecapitation = new EnchantmentDecapitation(config.decapitationEnchantmentID, 5, EnumEnchantmentType.weapon);
		LanguageRegistry.instance().addStringLocalization("enchantment.decapitation", "Decapitation");

		GameRegistry.registerTileEntity(TileEntityPlayerBeacon.class, "playerBeaconBlock");
		GameRegistry.registerTileEntity(TileEntityDefiledSoulPylon.class, "defiledSoulPylonBlock");

		LanguageRegistry.instance().addStringLocalization("commands.playerhead.usage", "/playerhead <playername> | Playername is case sensitive!");
		LanguageRegistry.instance().addStringLocalization("commands.playerhead.success", "Given a playerhead (%1$s) to %2$s");
		LanguageRegistry.instance().addStringLocalization("death.attack.behead", "%1$s was beheaded");
		LanguageRegistry.instance().addStringLocalization("death.attack.behead.player", "%1$s was beheaded by %2$s");
		LanguageRegistry.instance().addStringLocalization("death.attack.behead.item", "%1$s was beheaded by %2$s with %3$s");

		proxy.registerRenderers();

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	@Mod.EventHandler
	public void serverStart(FMLServerAboutToStartEvent e) {
		beaconData = new BeaconDataHandler();
		System.out.println("Loaded beacon data");
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandPlayerHead());
		registerRecipes();
		TickRegistry.registerScheduledTickHandler(new ServerTickHandler(), Side.SERVER);
	}

	public void registerRecipes() {
		//TODO balance pass and fix some recipes
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.defiledSoulConductorBlock, 5), "OPO", "PDP", "OPO", 'O', new ItemStack(Block.obsidian), 'P', new ItemStack(Item.eyeOfEnder), 'D', new ItemStack(Block.blockDiamond));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.defiledSoulPylonBlock, 10), "OPO", "GNG", "OPO", 'O', new ItemStack(PlayerBeacons.defiledSoulConductorBlock), 'P', new ItemStack(Item.eyeOfEnder), 'N', new ItemStack(Item.netherStar), 'N', new ItemStack(Item.ingotGold));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.playerBeaconBlock), " N ", "GPG", "OBO", 'O', new ItemStack(PlayerBeacons.defiledSoulConductorBlock), 'P', new ItemStack(Item.eyeOfEnder), 'G', new ItemStack(Item.ingotGold), 'N', new ItemStack(Item.netherStar), 'B', new ItemStack(Block.beacon));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.beheaderItem), " P ", "SHS", 'P', new ItemStack(Item.eyeOfEnder), 'S', new ItemStack(Item.swordIron), 'H', new ItemStack(Item.helmetIron));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.speedCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Item.dyePowder, 1, 12), 'G', new ItemStack(Block.glass), 'C', new ItemStack(PlayerBeacons.crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.digCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Item.dyePowder, 1, 3), 'G', new ItemStack(Block.glass), 'C', new ItemStack(PlayerBeacons.crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.jumpCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Item.dyePowder, 1, 6), 'G', new ItemStack(Block.glass), 'C', new ItemStack(PlayerBeacons.crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.resCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Item.dyePowder, 1, 8), 'G', new ItemStack(Block.glass), 'C', new ItemStack(PlayerBeacons.crystalItem));
	}
}
