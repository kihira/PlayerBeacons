package playerbeacons.common;

import net.minecraft.util.DamageSource;

/**
 * Created with IntelliJ IDEA.
 * User: Kihira
 * Date: 02/08/13
 * Time: 16:27
 * To change this template use File | Settings | File Templates.
 */
public class DamageBehead extends DamageSource {

	public DamageBehead() {
		super("behead");
		setDamageBypassesArmor();
		setDamageAllowedInCreativeMode();
	}
}
