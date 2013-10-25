package playerbeacons.api.throttle;

import java.util.ArrayList;
import java.util.List;

public class Throttle {

	public static List<IThrottle> throttleList = new ArrayList<IThrottle>();

	public static void registerThrottle(IThrottle throttle) {
		if (!throttleList.contains(throttle)) throttleList.add(throttle);
		else System.out.println("[PlayerBeacons] That throttle has already been registered: " + throttle);
	}
}
