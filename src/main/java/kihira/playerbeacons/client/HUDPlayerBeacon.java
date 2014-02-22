package kihira.playerbeacons.client;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;
import kihira.playerbeacons.block.BlockDefiledSoulPylon;
import kihira.playerbeacons.block.BlockPlayerBeacon;
import kihira.playerbeacons.tileentity.TileEntityDefiledSoulPylon;
import kihira.playerbeacons.tileentity.TileEntityPlayerBeacon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HUDPlayerBeacon implements IWailaDataProvider {
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityPlayerBeacon) {
            TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) accessor.getTileEntity();
            if (config.getConfig("pb.showowner")) currenttip.add("Owner: " + tileEntityPlayerBeacon.getOwner());
            if (config.getConfig("pb.showcorruption")) currenttip.add("Corruption: " + tileEntityPlayerBeacon.getCorruption());
        }
        else if (accessor.getTileEntity() instanceof TileEntityDefiledSoulPylon) {
            TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) accessor.getTileEntity();
            if (config.getConfig("pb.showcrystaltime") && tileEntityDefiledSoulPylon.getStackInSlot(0) != null) {
                int timeLeft = (tileEntityDefiledSoulPylon.getStackInSlot(0).getMaxDamage() - tileEntityDefiledSoulPylon.getStackInSlot(0).getItemDamage()) * 2;
                int hours = ((timeLeft / 60) / 60) % 24;
                int minutes = (timeLeft / 60) % 60;
                int seconds = timeLeft % 60;
                currenttip.add("Time until crystal depletion: " + hours + ":" + minutes + ":" + seconds);
            }
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new HUDPlayerBeacon(), BlockPlayerBeacon.class);
        registrar.registerBodyProvider(new HUDPlayerBeacon(), BlockDefiledSoulPylon.class);

        registrar.addConfig("Player Beacons", "pb.showowner", "Show Owner");
        registrar.addConfig("Player Beacons", "pb.showcorruption", "Show Corruption");
        registrar.addConfig("Player Beacons", "pb.showcrystaltime", "Show time remaining for crystals");
    }
}
