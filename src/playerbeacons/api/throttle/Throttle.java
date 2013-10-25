package playerbeacons.api.throttle;

import java.util.HashMap;

public class Throttle {

	public static HashMap<String, IThrottle> throttleHashMap = new HashMap<String, IThrottle>();

	public static void registerThrottle(String simpleName, IThrottle throttle) {
		if (!throttleHashMap.containsKey(simpleName)) throttleHashMap.put(simpleName, throttle);
		else throw new IllegalArgumentException("[PlayerBeacons] A throttle has already been registered with the name: " + simpleName);
	}
}
