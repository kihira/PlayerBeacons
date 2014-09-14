package kihira.playerbeacons.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.common.buff.*;
import kihira.playerbeacons.common.command.CommandPlayerBeacons;
import kihira.playerbeacons.common.command.CommandPlayerHead;
import kihira.playerbeacons.common.corruption.*;
import kihira.playerbeacons.common.lib.ModBlocks;
import kihira.playerbeacons.common.lib.ModItems;
import kihira.playerbeacons.common.util.EventHandler;
import kihira.playerbeacons.proxy.ClientProxy;
import kihira.playerbeacons.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = PlayerBeacons.MOD_ID, dependencies = "after:Waila;required-after:foxlib@[0.1.0,)", version = "$version", useMetadata = true, guiFactory = "kihira.playerbeacons.client.PlayerBeaconsGuiFactory")
public class PlayerBeacons {

    public static final String MOD_ID = "PlayerBeacons";
    public static final String RESOURCE_PRE = MOD_ID.toLowerCase() + ":";
    public static final Logger logger = LogManager.getLogger(MOD_ID);
    public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
	public static final CreativeTabs tabPlayerBeacons = new CreativeTabs(MOD_ID) {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return ModItems.itemResearchDiary;
        }
    };

	public static Config config;

    private final EndermanAggroCorruption endermanAggroCorruption = new EndermanAggroCorruption();
    private final EndTeleportCorruption endTeleportCorruption = new EndTeleportCorruption();
    private final SlownessCorruption slownessCorruption = new SlownessCorruption();
    private final BatCorruption batCorruption = new BatCorruption();
    private final PanicCorruption panicCorruption = new PanicCorruption();

    public static final DamageSource damageBehead = new DamageSource("behead").setDamageBypassesArmor();

    @Mod.Instance
    public static PlayerBeacons instance;

	@SidedProxy(clientSide = "kihira.playerbeacons.proxy.ClientProxy", serverSide = "kihira.playerbeacons.proxy.CommonProxy")
	public static CommonProxy proxy;

    public static SpeedBuff speedbuff;
    public static JumpBuff jumpBuff;
    public static HealthBoostBuff healthBuff;
    public static HasteBuff hasteBuff;
    public static ResistanceBuff resistanceBuff;

    @Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		config = new Config(e.getSuggestedConfigurationFile());

        ModItems.init();
        ModBlocks.init();

		registerBuffs();
        registerRecipes();
		MinecraftForge.EVENT_BUS.register(new EventHandler());
        FMLCommonHandler.instance().bus().register(new FMLEventHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		proxy.registerRenderers();

		new EnchantmentDecapitation(config.decapitationEnchantmentID);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
        FMLInterModComms.sendMessage("Waila", "register", "kihira.playerbeacons.client.HUDPlayerBeacon.callbackRegister");
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandPlayerHead());
        e.registerServerCommand(new CommandPlayerBeacons());
	}

    @Mod.EventHandler
    public void serverShutdown(FMLServerStoppedEvent e) {
        FMLEventHandler.activeCorruptionEffects.clear();
        ClientProxy.playerCorruption = 0F;
    }

	private void registerBuffs() {
		speedbuff = new SpeedBuff();
		jumpBuff = new JumpBuff();
		hasteBuff = new HasteBuff();
		resistanceBuff = new ResistanceBuff();
        healthBuff = new HealthBoostBuff();
	}

	private void registerRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.blockDefiledSoulConductor, 6), "OOO", "MPM", "OOO", 'O', new ItemStack(Blocks.obsidian), 'P', new ItemStack(Items.ender_eye), 'M', new ItemStack(Blocks.mycelium));
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.blockDefiledSoulPylon, 3), "OPO", "G G", "OPO", 'O', new ItemStack(ModBlocks.blockDefiledSoulConductor), 'P', new ItemStack(Items.ender_eye), 'G', new ItemStack(Items.gold_ingot));
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.blockPlayerBeacon), "PNP", "GBG", "OOO", 'O', new ItemStack(ModBlocks.blockDefiledSoulConductor), 'P', new ItemStack(Items.ender_eye), 'G', new ItemStack(Items.gold_ingot), 'N', new ItemStack(Items.nether_star), 'B', new ItemStack(Blocks.beacon));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemLightBlueCrystal), "DGD", "GCG", "DGD", 'D', new ItemStack(Items.dye, 1, 12), 'G', new ItemStack(Blocks.glass), 'C', new ItemStack(ModItems.itemCrystal));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemBrownCrystal), "DGD", "GCG", "DGD", 'D', new ItemStack(Items.dye, 1, 3), 'G', new ItemStack(Blocks.glass), 'C', new ItemStack(ModItems.itemCrystal));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemGreenCrystal), "DGD", "GCG", "DGD", 'D', new ItemStack(Items.dye, 1, 10), 'G', new ItemStack(Blocks.glass), 'C', new ItemStack(ModItems.itemCrystal));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemRedCrystal), "DGD", "GCG", "DGD", 'D', new ItemStack(Items.dye, 1, 1), 'G', new ItemStack(Blocks.glass), 'C', new ItemStack(ModItems.itemCrystal));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemYellowCrystal), "DGD", "GCG", "DGD", 'D', new ItemStack(Items.dye, 1, 11), 'G', new ItemStack(Blocks.glass), 'C', new ItemStack(ModItems.itemCrystal));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemBeheader), "LIL", "IPI", "S S", 'P', new ItemStack(Items.ender_eye), 'S', new ItemStack(Items.iron_sword), 'L', new ItemStack(Items.leather), 'I', new ItemStack(Items.iron_ingot));
	}

}
