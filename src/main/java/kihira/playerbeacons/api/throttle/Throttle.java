package kihira.playerbeacons.api.throttle;

import kihira.playerbeacons.common.PlayerBeacons;

import java.util.ArrayList;
import java.util.List;

public class Throttle {

	public static List<IThrottle> throttleList = new ArrayList<IThrottle>();

	public static void registerThrottle(IThrottle throttle) {
		if (!throttleList.contains(throttle)) throttleList.add(throttle);
		else PlayerBeacons.logger.warning("That throttle has already been registered: " + throttle);
	}
}
