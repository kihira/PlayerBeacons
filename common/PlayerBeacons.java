package playerbeacons.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "PlayerBeacons", name = "Player Beacons", version = "0.1")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class PlayerBeacons {

	@Mod.EventHandler
	public void init(FMLPreInitializationEvent e) {

	   new Config(e.getSuggestedConfigurationFile());

	}
}
