package playerbeacons.common;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import java.util.EnumSet;

public class TickHandler implements ITickHandler{
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		//TODO Do most of the beacon work here ie applying effects?
		//TODO Should I do beheader stuff here? If so I then have to check each players inventory every tick instead of using onArmorTickUpdate which is already called every tick
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public TickType ticks() {
		return TickType.SERVER;
	}

	@Override
	public String getLabel() {
		return "Player Beacon";
	}
}
