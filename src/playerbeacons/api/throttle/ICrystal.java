package playerbeacons.api.throttle;

/**
 * This is implemented by items/blocks that can go inside the standard pylon
 */
public interface ICrystal extends IThrottle {

	public double[] getRGBA();
}
