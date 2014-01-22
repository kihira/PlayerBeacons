package playerbeacons.client;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;
import playerbeacons.block.BlockPlayerBeacon;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

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
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new HUDPlayerBeacon(), BlockPlayerBeacon.class);

        registrar.addConfig("Player Beacons", "pb.showowner", "Show Owner");
        registrar.addConfig("Player Beacons", "pb.showcorruption", "Show Corruption");
    }
}
