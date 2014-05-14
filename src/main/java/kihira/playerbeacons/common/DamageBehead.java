package kihira.playerbeacons.common;

import net.minecraft.util.DamageSource;

public class DamageBehead extends DamageSource {

	public DamageBehead() {
		super("behead");
		this.setDamageBypassesArmor();
	}
}
