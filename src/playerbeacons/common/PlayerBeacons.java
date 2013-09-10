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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import playerbeacons.block.BlockDefiledSoulConductor;
import playerbeacons.block.BlockDefiledSoulPylon;
import playerbeacons.block.BlockPlayerBeacon;
import playerbeacons.buff.*;
import playerbeacons.item.*;
import playerbeacons.proxy.CommonProxy;
import playerbeacons.tileentity.TileEntityDefiledSoulPylon;
import playerbeacons.tileentity.TileEntityPlayerBeacon;
import playerbeacons.util.BeaconDataHandler;
import playerbeacons.util.EventHandler;

import java.util.HashMap;

@Mod(modid = "PlayerBeacons", name = "Player Beacons", version = "1.1.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class PlayerBeacons {

	public static Config config;

	public static Block playerBeaconBlock;
	public static Block defiledSoulConductorBlock;
	public static Block defiledSoulPylonBlock;

	public static BeheaderItem beheaderItem;
	public static CrystalItem crystalItem;
	public static SpeedCrystalItem speedCrystalItem;
	public static DigCrystalItem digCrystalItem;
	public static JumpCrystalItem jumpCrystalItem;
	public static ResCrystalItem resCrystalItem;

	public static EnchantmentDecapitation enchantmentDecapitation;

	public static BeaconDataHandler beaconData;

	@SidedProxy(clientSide = "playerbeacons.proxy.ClientProxy", serverSide = "playerbeacons.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent e) {

		config = new Config(e.getSuggestedConfigurationFile());

		playerBeaconBlock = new BlockPlayerBeacon(config.playerBeaconBlockID);
		GameRegistry.registerBlock(playerBeaconBlock, "playerBeaconBlock");
		defiledSoulConductorBlock = new BlockDefiledSoulConductor(config.defiledSoulConductorBlockID);
		GameRegistry.registerBlock(defiledSoulConductorBlock, "defiledSoulConductorBlock");
		defiledSoulPylonBlock = new BlockDefiledSoulPylon(config.defiledSoulPylonBlockID);
		GameRegistry.registerBlock(defiledSoulPylonBlock, "defiledSoulPylonBlock");

		beheaderItem = new BeheaderItem(config.beheaderItemID);
		GameRegistry.registerItem(beheaderItem, "beheaderItem");
		crystalItem = new CrystalItem(config.crystalItemID);
		GameRegistry.registerItem(crystalItem, "crystalItem");
		speedCrystalItem = new SpeedCrystalItem(config.speedCrystalItemID);
		GameRegistry.registerItem(speedCrystalItem, "speedCrystalItem");
		digCrystalItem = new DigCrystalItem(config.digCrystalItemID);
		GameRegistry.registerItem(digCrystalItem, "digCrystalItem");
		jumpCrystalItem = new JumpCrystalItem(config.jumpCrystalItemID);
		GameRegistry.registerItem(jumpCrystalItem, "jumpCrystalItem");
		resCrystalItem = new ResCrystalItem(config.resCrystalItemID);
		GameRegistry.registerItem(resCrystalItem, "resCrystalItem");

		enchantmentDecapitation = new EnchantmentDecapitation(config.decapitationEnchantmentID);

		GameRegistry.registerTileEntity(TileEntityPlayerBeacon.class, "playerBeaconBlock");
		GameRegistry.registerTileEntity(TileEntityDefiledSoulPylon.class, "defiledSoulPylonBlock");

		proxy.registerRenderers();
		registerRecipes();
		registerBuffs();
		config.loadBuffs();
		addLocalization();
		MinecraftForge.EVENT_BUS.register(new EventHandler());

		ItemStack itemStack = makeResearchNotes();
		ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST);
		info.addItem(new WeightedRandomChestContent(itemStack, 1, 1, 5));
		info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
		info.addItem(new WeightedRandomChestContent(itemStack, 1, 1, 5));
		info = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST);
		info.addItem(new WeightedRandomChestContent(itemStack, 1, 1, 5));
		info = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST);
		info.addItem(new WeightedRandomChestContent(itemStack, 1, 1, 5));
		info = ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH);
		info.addItem(new WeightedRandomChestContent(itemStack, 1, 1, 5));
	}

	@Mod.EventHandler
	public void serverStart(FMLServerAboutToStartEvent e) {
		beaconData = new BeaconDataHandler();
		System.out.println("Loaded beacon data");
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandPlayerHead());
		e.registerServerCommand(new CommandPlayerBeacons());
		TickRegistry.registerScheduledTickHandler(new ServerTickHandler(), Side.SERVER);
	}

	private void addLocalization() {
		LanguageRegistry.instance().addStringLocalization("commands.playerhead.usage", "/playerhead <playername> | Playername is case sensitive!");
		LanguageRegistry.instance().addStringLocalization("commands.playerhead.success", "Given a playerhead (%1$s) to %2$s");
		LanguageRegistry.instance().addStringLocalization("commands.playerbeacon.alreadyExists", "The player %1$s already has a beacon bound in this dimension");
		LanguageRegistry.instance().addStringLocalization("commands.playerhead.notBeacon", "The block (%1$s, %2$s, %3$s) is not a Player Beacon!");
		LanguageRegistry.instance().addStringLocalization("commands.playerbeacon.setowner.success", "%1$s changed Player Beacon (%2$s, %3$s, %4$s) owner to %5$s");
		LanguageRegistry.instance().addStringLocalization("commands.playerbeacon.setowner.usage", "/pb setowner <username>");
		LanguageRegistry.instance().addStringLocalization("commands.playerbeacon.setcorruption.usage", "/pb setcorruption <float: corruption value> <boolean: should adjust corruption level>");
		LanguageRegistry.instance().addStringLocalization("commands.playerbeacon.setcorruption.success", "%1$s changed Player Beacon (%2$s, %3$s, %4$s) corruption to %5$s");
		LanguageRegistry.instance().addStringLocalization("death.attack.behead", "%1$s was beheaded");
		LanguageRegistry.instance().addStringLocalization("death.attack.behead.player", "%1$s was beheaded by %2$s");
		LanguageRegistry.instance().addStringLocalization("death.attack.behead.item", "%1$s was beheaded by %2$s with %3$s");
		LanguageRegistry.instance().addStringLocalization("enchantment.decapitation", "Decapitation");
		LanguageRegistry.addName(resCrystalItem, "Resistance Crystal");
		LanguageRegistry.addName(jumpCrystalItem, "Jump Crystal");
		LanguageRegistry.addName(digCrystalItem, "Dig Crystal");
		LanguageRegistry.addName(speedCrystalItem, "Speed Crystal");
		LanguageRegistry.addName(crystalItem, "Depleted Crystal");
		LanguageRegistry.addName(beheaderItem, "Beheader");
		LanguageRegistry.addName(defiledSoulPylonBlock, "Defiled Soul Pylon");
		LanguageRegistry.addName(defiledSoulConductorBlock, "Defiled Soul Conductor");
		LanguageRegistry.addName(playerBeaconBlock, "Player Beacon");
	}

	private void registerBuffs() {
		new SpeedBuff(speedCrystalItem);
		new JumpBuff(jumpCrystalItem);
		new DigBuff(digCrystalItem);
		new ResistanceBuff(resCrystalItem);
	}

	private void registerRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.defiledSoulConductorBlock, 4), "OPO", "MCM", "OPO", 'O', new ItemStack(Block.obsidian), 'P', new ItemStack(Item.eyeOfEnder), 'C', new ItemStack(PlayerBeacons.crystalItem), 'M', new ItemStack(Block.mycelium));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.defiledSoulPylonBlock, 2), "OPO", "G G", "OPO", 'O', new ItemStack(PlayerBeacons.defiledSoulConductorBlock), 'P', new ItemStack(Item.eyeOfEnder), 'G', new ItemStack(Item.ingotGold));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.playerBeaconBlock), "PNP", "GBG", "OOO", 'O', new ItemStack(PlayerBeacons.defiledSoulConductorBlock), 'P', new ItemStack(Item.eyeOfEnder), 'G', new ItemStack(Item.ingotGold), 'N', new ItemStack(Item.netherStar), 'B', new ItemStack(Block.beacon));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.speedCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Item.dyePowder, 1, 12), 'G', new ItemStack(Block.glass), 'C', new ItemStack(PlayerBeacons.crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.digCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Item.dyePowder, 1, 3), 'G', new ItemStack(Block.glass), 'C', new ItemStack(PlayerBeacons.crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.jumpCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Item.dyePowder, 1, 6), 'G', new ItemStack(Block.glass), 'C', new ItemStack(PlayerBeacons.crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.resCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Item.dyePowder, 1, 8), 'G', new ItemStack(Block.glass), 'C', new ItemStack(PlayerBeacons.crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.beheaderItem), "LIL", "IPI", "S S", 'P', new ItemStack(Item.eyeOfEnder), 'S', new ItemStack(Item.swordIron), 'L', new ItemStack(Item.leather), 'I', new ItemStack(Item.ingotIron));
	}

	private ItemStack makeResearchNotes() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		nbtTagCompound.setString("author", "Dr. Prof. §kNexans");
		nbtTagCompound.setString("title", "Research Notes");
		NBTTagList nbtTagList = new NBTTagList();
		nbtTagList.appendTag(new NBTTagString("1", "§lDay 1\n§rWe heard a rumour on the market place that there is a hidden altar which if anyone that unravels its secret, they will gain tremendous power. So we packed up everything to find this altar."));
		nbtTagList.appendTag(new NBTTagString("2", "§lDay 26\n§rAll this time we have been traveling, across oceans through deserts, and into deep jungles thinking we would find this illusive altar and unlock its power, but we are exhausted and our crew is dehydrated by heat. Will we ever find it?"));
		nbtTagList.appendTag(new NBTTagString("3", "§lDay 31\n§rI'm not wanting to be excited but we finally found it the hidden altar its just as the rumor described it but its not just one altar here, this underground city hold hundreds ranging from 1 to 4 steps high, with strange obelisk like structures in the corners."));
		nbtTagList.appendTag(new NBTTagString("4", "§lDay 32\n§rAfter doing some research on the altars we did a gruesome discovery, on the \"altars\" we found skulls and heads. Something inside me says to leave all this alone, but in the name of science we must push on."));
		nbtTagList.appendTag(new NBTTagString("5", "§lDay 51\n§rAfter digging further into a cave system behind the city we found a semi-active altar. This is a great find for us because we didn't had a breakthrough since we arrived, on 1st sight we notice that the \"obelisks\" have a strange influence on the altar."));
		nbtTagList.appendTag(new NBTTagString("6", "§lDay 51 - Cont\n§rAlso we noticed some coloured crystalline shapes in the \"obelisks\", what would they do?"));
		nbtTagList.appendTag(new NBTTagString("7", "§lDay 54\n§rAs we looked around for 3 days, we noticed a lot of broken crystals on the ground, but none would fit inside the \"obelisks\". I personally think we need a pure crystal."));
		nbtTagList.appendTag(new NBTTagString("8", "§lDay 54 - Cont\n§rIt also looks like there are a lot of green jewels laying around, that do not look like crystals in any shape or form. I might try something with those soon."));
		nbtTagList.appendTag(new NBTTagString("9", "§lDay 55\n§rYes I was right, these green jewels we found they interact with the altar. Creating a uniform crystal. As one of the men looked through the crystal, he found that there was some strange text near the altar, what would this mean?"));
		nbtTagList.appendTag(new NBTTagString("10", "§lDay 55 - Cont\n§rWe do notice the word corruption, lets hope its nothing serious."));
		nbtTagList.appendTag(new NBTTagString("11", "§lDay 61\n§rLast night we heard a horrific noise, so most of the crew didn't want to stay and left. I believe we should just find out what the altar is made from and we have to pack up and go if I want to make it home alive."));
		nbtTagList.appendTag(new NBTTagString("12", "§lDay 62\n§rLuckily there is another team working on finding out what the structure is made out of they say we can pack up and leave tomorrow. But as I wander around in one of the small houses, I found a strange device."));
		nbtTagList.appendTag(new NBTTagString("13","§lDay 62 - Cont\n§rThis confirms my thought since day 32 the heads on the altars are those of real Steve's and Stephanie's that lived a long time ago in this world. I must take this home with me."));
		nbtTagList.appendTag(new NBTTagString("14", "§lDay 63\n§rThe team, that was working on finding out what the structure was made out of, has suddenly disappeared without a trace, luckily they had already analyzed the whole structure."));
		nbtTagList.appendTag(new NBTTagString("15", "§lDay 63 - Cont\n§rIt seems the base of the altar is made from a special mix of, a purplish fungi looking grass, some obsidian, a crystal, and a catalyst that combines the powers of the Nether with the End."));
		nbtTagList.appendTag(new NBTTagString("16",	"§lDay 63 - Cont\n§rIt's also noted here that the smallest of the pyramids is a 3x3 square that is 1 high. When i turn the page I see they found out that, the \"obelisks\" are some kind of conductors, that are connected to the base and the altar."));
		nbtTagList.appendTag(new NBTTagString("17", "§lDay 63 - Cont\n§rThis is very interesting, they are made from gold, which was obvious, some of the baseblocks and the catalyst again. A small note pops out of the book they have written."));
		nbtTagList.appendTag(new NBTTagString("18", "§lDay 63 - Cont\n§rIt says: \" We should have never discovered this place we need to leave now before it's too late. On the back they quickly scribbled that the altar itself consists of, the same blocks as the base again, a radiant source of power,"));
		nbtTagList.appendTag(new NBTTagString("18", "§lDay 63 - Cont\n§rsome gold and the star that shines in hell. We will have to find a way to build it when we are back home."));
		nbtTagList.appendTag(new NBTTagString("19", "§lDay 63 - Night\n§rEveryone was right this place is cursed, I just felt a chill go down my spine while sleeping in this god forsaken place."));
		nbtTagList.appendTag(new NBTTagString("20", "§lDay 63 - Night Cont\n§rI'm running now with my bag and research notes, the undead have attacked, they seem to have heads on their heads, most of them are from the disappeared team, but wait ... is that my head?"));
		nbtTagList.appendTag(new NBTTagString("22", "§lDay 64\n§rI just escaped sudden death it seems like the undead wont follow me outside, thank you sun. Time to go home."));
		nbtTagList.appendTag(new NBTTagString("23", "§lDay 66\n§rHome at last time to spend the rest of my money developing this beacon with a team. I have to study it more, there must be a way to unlock its powers."));
		nbtTagList.appendTag(new NBTTagString("24", "§lDay 72\n§rThe replica is done and works, I will write history with this I also just noticed that, I still have this strange device in my bag, I wonder what would happen if I put this §kAdgsdbsdgsdgezavwdsv"));
		nbtTagList.appendTag(new NBTTagString("25", "§lDay 73\n§rI thought yesterday was a dream till I noticed my diary here and found a horrific truth my head was on the floor, this can't be, its impossible! A few hours after I re-collected my thoughts I thought, \"maybe this is the key to unlock the altars power?\"."));
		nbtTagList.appendTag(new NBTTagString("26", "§lDay 73 - Cont\n§rSo I placed the head on the altar and it seemed to interact with each other and i feel better than ever. I can jump higher run faster dig faster and I'm much more durable, this is great did I finally unlock its true potential?"));
		nbtTagList.appendTag(new NBTTagString("27", "§lDay 73 - Cont\n§rThis makes me wonder however, what were those crystals doing in the obelisks. Those are worries for later though."));
		nbtTagList.appendTag(new NBTTagString("28", "§lDay 74\n§rFor some reason I got pulled back from my presentation about the altar, to the altar itself. Strange. When looking through a crystal I see the corruption is now clearly visible and I have to get rid of it."));
		nbtTagList.appendTag(new NBTTagString("29", "§lDay 78\n§rI can't find out how to release the corruption. These are my last words in this world, find a way to balance it that is its greatest treasure. Good bye whoever reads this.\n...\nDr. Prof. §kNexans§r has left the game"));

		nbtTagCompound.setTag("pages", nbtTagList);
		ItemStack itemStack = new ItemStack(Item.writtenBook);
		itemStack.setTagCompound(nbtTagCompound);
		return itemStack;
	}
}
