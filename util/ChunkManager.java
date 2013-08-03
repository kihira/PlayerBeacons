package playerbeacons.util;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

import java.util.List;

public class ChunkManager implements ForgeChunkManager.LoadingCallback{
	@Override
	public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
		System.out.println("Loading tickets");
		for (ForgeChunkManager.Ticket ticket : tickets) {
			int x = ticket.getModData().getInteger("x");
			int y = ticket.getModData().getInteger("y");
			int z = ticket.getModData().getInteger("z");
			TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) world.getBlockTileEntity(x, y, z);
			tileEntityPlayerBeacon.useTicket(ticket);
			System.out.println(ticket.toString());
		}
	}
}
