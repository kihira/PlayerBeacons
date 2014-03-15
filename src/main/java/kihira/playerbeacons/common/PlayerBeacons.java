package kihira.playerbeacons.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import kihira.playerbeacons.api.throttle.Throttle;
import kihira.playerbeacons.block.*;
import kihira.playerbeacons.buff.DigBuff;
import kihira.playerbeacons.buff.JumpBuff;
import kihira.playerbeacons.buff.ResistanceBuff;
import kihira.playerbeacons.buff.SpeedBuff;
import kihira.playerbeacons.item.*;
import kihira.playerbeacons.potion.CorruptionPotion;
import kihira.playerbeacons.proxy.CommonProxy;
import kihira.playerbeacons.tileentity.TileEntityDefiledSoulPylon;
import kihira.playerbeacons.tileentity.TileEntityPlayerBeacon;
import kihira.playerbeacons.util.BeaconDataHandler;
import kihira.playerbeacons.util.EventHandler;
import kihira.playerbeacons.util.ThaumcraftHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;

@Mod(modid = "PlayerBeacons", dependencies = "after:Thaumcraft;after:Waila;")
public class PlayerBeacons {

	public static final CreativeTabPlayerBeacons tabPlayerBeacons = new CreativeTabPlayerBeacons();
	public static Config config;
	public static BeaconDataHandler beaconData;
	public static final Logger logger = LogManager.getLogger("PlayerBeacons");

	public static final Block playerBeaconBlock = new BlockPlayerBeacon();
	public static final Block defiledSoulConductorBlock = new BlockDefiledSoulConductor();
	public static final Block defiledSoulPylonBlock = new BlockDefiledSoulPylon();

	public static final BeheaderItem beheaderItem = new BeheaderItem();
	public static final CrystalItem crystalItem = new CrystalItem();
	public static final LightBlueCrystalItem lightBlueCrystalItem = new LightBlueCrystalItem();
	public static final BrownCrystalItem brownCrystalItem = new BrownCrystalItem();
	public static final GreenCrystalItem greenCrystalItem = new GreenCrystalItem();
	public static final RedCrystalItem redCrystalItem = new RedCrystalItem();

	public static boolean isChristmas = false;

	@SidedProxy(clientSide = "kihira.playerbeacons.proxy.ClientProxy", serverSide = "kihira.playerbeacons.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent e) {

		config = new Config(e.getSuggestedConfigurationFile());

		GameRegistry.registerBlock(playerBeaconBlock, "playerBeaconBlock");
		GameRegistry.registerBlock(defiledSoulConductorBlock, DefiledSoulConductorItemBlock.class, "defiledSoulConductorBlock");
		GameRegistry.registerBlock(defiledSoulPylonBlock, DefiledSoulPylonItemBlock.class, "defiledSoulPylonBlock");

		GameRegistry.registerItem(beheaderItem, "beheaderItem");
		GameRegistry.registerItem(crystalItem, "crystalItem");
		GameRegistry.registerItem(lightBlueCrystalItem, "lightBlueCrystalItem");
		GameRegistry.registerItem(brownCrystalItem, "brownCrystalItem");
		GameRegistry.registerItem(greenCrystalItem, "greenCrystalItem");
		GameRegistry.registerItem(redCrystalItem, "redCrystalItem");

		GameRegistry.registerTileEntity(TileEntityPlayerBeacon.class, "playerBeaconBlock");
		GameRegistry.registerTileEntity(TileEntityDefiledSoulPylon.class, "defiledSoulPylonBlock");

		registerThrottles();
		registerBuffs();
		config.loadBuffs();
		MinecraftForge.EVENT_BUS.register(new EventHandler());
        FMLCommonHandler.instance().bus().register(new ServerTickHandler());
		proxy.registerRenderers();

		new EnchantmentDecapitation(config.decapitationEnchantmentID);
        new CorruptionPotion(config.corruptionPotionID);

		if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 25 && Calendar.getInstance().get(Calendar.MONTH) == 12) isChristmas = true;

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
    public void init(FMLInitializationEvent e) {
        FMLInterModComms.sendMessage("Waila", "register", "playerbeacons.client.HUDPlayerBeacon.callbackRegister");
    }

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		//Thaumcraft Integration
		if (Loader.isModLoaded("Thaumcraft") && config.enableThaumcraft) {
			logger.info("Thaumcraft detected, enabling integration");
			new ThaumcraftHandler();
		}
		else {
			registerRecipes();
		}
	}

	@Mod.EventHandler
	public void serverStart(FMLServerAboutToStartEvent e) {
		beaconData = new BeaconDataHandler();
		logger.info("Loaded beacon data");
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandPlayerHead());
		e.registerServerCommand(new CommandPlayerBeacons());
	}

	private void registerThrottles() {
		Throttle.registerThrottle(lightBlueCrystalItem);
		Throttle.registerThrottle(greenCrystalItem);
		Throttle.registerThrottle(redCrystalItem);
		Throttle.registerThrottle(brownCrystalItem);
	}

	private void registerBuffs() {
		new SpeedBuff();
		new JumpBuff();
		new DigBuff();
		new ResistanceBuff();
	}

	private void registerRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(defiledSoulConductorBlock, 4), "OOO", "MPM", "OOO", 'O', new ItemStack(Blocks.obsidian), 'P', new ItemStack(Items.ender_eye), 'M', new ItemStack(Blocks.mycelium));
		GameRegistry.addShapedRecipe(new ItemStack(defiledSoulPylonBlock, 2), "OPO", "G G", "OPO", 'O', new ItemStack(defiledSoulConductorBlock), 'P', new ItemStack(Items.ender_eye), 'G', new ItemStack(Items.gold_ingot));
		GameRegistry.addShapedRecipe(new ItemStack(playerBeaconBlock), "PNP", "GBG", "OOO", 'O', new ItemStack(defiledSoulConductorBlock), 'P', new ItemStack(Items.ender_eye), 'G', new ItemStack(Items.gold_ingot), 'N', new ItemStack(Items.nether_star), 'B', new ItemStack(Blocks.beacon));
		GameRegistry.addShapedRecipe(new ItemStack(lightBlueCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Items.dye, 1, 12), 'G', new ItemStack(Blocks.glass), 'C', new ItemStack(crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(brownCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Items.dye, 1, 3), 'G', new ItemStack(Blocks.glass), 'C', new ItemStack(crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(greenCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Items.dye, 1, 10), 'G', new ItemStack(Blocks.glass), 'C', new ItemStack(crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(redCrystalItem), "DGD", "GCG", "DGD", 'D', new ItemStack(Items.dye, 1, 1), 'G', new ItemStack(Blocks.glass), 'C', new ItemStack(crystalItem));
		GameRegistry.addShapedRecipe(new ItemStack(beheaderItem), "LIL", "IPI", "S S", 'P', new ItemStack(Items.ender_eye), 'S', new ItemStack(Items.iron_sword), 'L', new ItemStack(Items.leather), 'I', new ItemStack(Items.iron_ingot));
	}

	private ItemStack makeResearchNotes() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		nbtTagCompound.setString("author", "Dr. Prof. \u00a7kNexans");
		nbtTagCompound.setString("title", "Research Notes");
		NBTTagList nbtTagList = new NBTTagList();
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 1\n\u00a7rWe heard a rumour on the market place that there is a hidden altar which if anyone that unravels its secret, they will gain tremendous power. So we packed up everything to find this altar."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 26\n\u00a7rAll this time we have been traveling, across oceans through deserts, and into deep jungles thinking we would find this illusive altar and unlock its power, but we are exhausted and our crew is dehydrated by heat. Will we ever find it?"));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 31\n\u00a7rI'm not wanting to be excited but we finally found it the hidden altar its just as the rumor described it but its not just one altar here, this underground city hold hundreds ranging from 1 to 4 steps high, with strange obelisk like structures in the corners."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 32\n\u00a7rAfter doing some research on the altars we did a gruesome discovery, on the \"altars\" we found skulls and heads. Something inside me says to leave all this alone, but in the name of science we must push on."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 51\n\u00a7rAfter digging further into a cave system behind the city we found a semi-active altar. This is a great find for us because we didn't had a breakthrough since we arrived, on 1st sight we notice that the \"obelisks\" have a strange influence on the altar."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 51 - Cont\n\u00a7rAlso we noticed some coloured crystalline shapes in the \"obelisks\", what would they do?"));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 54\n\u00a7rAs we looked around for 3 days, we noticed a lot of broken crystals on the ground, but none would fit inside the \"obelisks\". I personally think we need a pure crystal."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 54 - Cont\n\u00a7rIt also looks like there are a lot of green jewels laying around, that do not look like crystals in any shape or form. I might try something with those soon."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 55\n\u00a7rYes I was right, these green jewels we found they interact with the altar. Creating a uniform crystal. As one of the men looked through the crystal, he found that there was some strange text near the altar, what would this mean?"));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 55 - Cont\n\u00a7rWe do notice the word corruption, lets hope its nothing serious."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 61\n\u00a7rLast night we heard a horrific noise, so most of the crew didn't want to stay and left. I believe we should just find out what the altar is made from and we have to pack up and go if I want to make it home alive."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 62\n\u00a7rLuckily there is another team working on finding out what the structure is made out of they say we can pack up and leave tomorrow. But as I wander around in one of the small houses, I found a strange device."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 62 - Cont\n\u00a7rThis confirms my thought since day 32 the heads on the altars are those of real Steve's and Stephanie's that lived a long time ago in this world. I must take this home with me."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 63\n\u00a7rThe team, that was working on finding out what the structure was made out of, has suddenly disappeared without a trace, luckily they had already analyzed the whole structure."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 63 - Cont\n\u00a7rIt seems the base of the altar is made from a special mix of, a purplish fungi looking grass, some obsidian, a crystal, and a catalyst that combines the powers of the Nether with the End."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 63 - Cont\n\u00a7rIt's also noted here that the smallest of the pyramids is a 3x3 square that is 1 high. When i turn the page I see they found out that, the \"obelisks\" are some kind of conductors, that are connected to the base and the altar."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 63 - Cont\n\u00a7rThis is very interesting, they are made from gold, which was obvious, some of the baseblocks and the catalyst again. A small note pops out of the book they have written."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 63 - Cont\n\u00a7rIt says: \" We should have never discovered this place we need to leave now before it's too late. On the back they quickly scribbled that the altar itself consists of, the same blocks as the base again, a radiant source of power,"));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 63 - Cont\n\u00a7rsome gold and the star that shines in hell. We will have to find a way to build it when we are back home."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 63 - Night\n\u00a7rEveryone was right this place is cursed, I just felt a chill go down my spine while sleeping in this god forsaken place."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 63 - Night Cont\n\u00a7rI'm running now with my bag and research notes, the undead have attacked, they seem to have heads on their heads, most of them are from the disappeared team, but wait ... is that my head?"));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 64\n\u00a7rI just escaped sudden death it seems like the undead wont follow me outside, thank you sun. Time to go home."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 66\n\u00a7rHome at last time to spend the rest of my money developing this beacon with a team. I have to study it more, there must be a way to unlock its powers."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 72\n\u00a7rThe replica is done and works, I will write history with this I also just noticed that, I still have this strange device in my bag, I wonder what would happen if I put this \u00a7kAdgsdbsdgsdgezavwdsv"));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 73\n\u00a7rI thought yesterday was a dream till I noticed my diary here and found a horrific truth my head was on the floor, this can't be, its impossible! A few hours after I re-collected my thoughts I thought, \"maybe this is the key to unlock the altars power?\"."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 73 - Cont\n\u00a7rSo I placed the head on the altar and it seemed to interact with each other and i feel better than ever. I can jump higher run faster dig faster and I'm much more durable, this is great did I finally unlock its true potential?"));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 73 - Cont\n\u00a7rThis makes me wonder however, what were those crystals doing in the obelisks. Those are worries for later though."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 74\n\u00a7rFor some reason I got pulled back from my presentation about the altar, to the altar itself. Strange. When looking through a crystal I see the corruption is now clearly visible and I have to get rid of it."));
		nbtTagList.appendTag(new NBTTagString("\u00a7lDay 78\n\u00a7rI can't find out how to release the corruption. These are my last words in this world, find a way to balance it that is its greatest treasure. Good bye whoever reads this.\n...\nDr. Prof. \u00a7kNexans\u00a7r has left the game"));

		nbtTagCompound.setTag("pages", nbtTagList);
		ItemStack itemStack = new ItemStack(Items.written_book);
		itemStack.setTagCompound(nbtTagCompound);
		return itemStack;
	}
}
