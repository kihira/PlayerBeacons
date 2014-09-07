package kihira.playerbeacons.common.lib;

import cpw.mods.fml.common.registry.GameRegistry;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.block.BlockDefiledSoulConductor;
import kihira.playerbeacons.common.block.BlockDefiledSoulPylon;
import kihira.playerbeacons.common.block.BlockPlayerBeacon;
import kihira.playerbeacons.common.item.itemblock.DefiledSoulConductorItemBlock;
import kihira.playerbeacons.common.item.itemblock.DefiledSoulPylonItemBlock;
import kihira.playerbeacons.common.tileentity.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.ItemBlock;

@GameRegistry.ObjectHolder(PlayerBeacons.MOD_ID)
public final class ModBlocks {

    public static final Block blockDefiledSoulPylon = new BlockDefiledSoulPylon();
    public static final Block blockDefiledSoulConductor = new BlockDefiledSoulConductor();
    public static final Block blockPlayerBeacon = new BlockPlayerBeacon();

    public static void init() {
        registerBlock(blockPlayerBeacon, Names.PLAYER_BEACON);
        registerBlock(blockDefiledSoulConductor, DefiledSoulConductorItemBlock.class, Names.SOUL_CONDUCTOR);
        registerBlock(blockDefiledSoulPylon, DefiledSoulPylonItemBlock.class, Names.SOUL_PYLON);

        GameRegistry.registerTileEntity(TileEntityMultiBlock.class, PlayerBeacons.MOD_ID + ":multiBlock");
    }

    private static void registerBlock(Block block, String name) {
        registerBlock(block, null, name);
    }

    private static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name) {
        GameRegistry.registerBlock(block, itemclass, name);
        if (block instanceof ITileEntityProvider) {
            GameRegistry.registerTileEntity(((ITileEntityProvider) block).createNewTileEntity(null, 0).getClass(), Names.getTileEntityName(name));
        }
    }

    public static class Names {
        public static final String PLAYER_BEACON = "blockPlayerBeacon";
        public static final String SOUL_CONDUCTOR = "blockDefiledSoulConductor";
        public static final String SOUL_PYLON = "blockDefiledSoulPylon";

        public static String getTextureName(String name) {
            return PlayerBeacons.MOD_ID + ":" + name;
        }

        public static String getTileEntityName(String name) {
            return PlayerBeacons.MOD_ID + ":tile." + name;
        }
    }

}
