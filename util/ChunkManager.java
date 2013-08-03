package playerbeacons.util;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.List;

public class ChunkManager implements ForgeChunkManager.LoadingCallback{
	@Override
	public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
		//TODO
		System.out.println("Loading tickets");
		List<ForgeChunkManager.Ticket> tickets2 = tickets;
		for (ForgeChunkManager.Ticket aTickets2 : tickets2) {
			System.out.println(aTickets2.toString());
		}
	}
}
