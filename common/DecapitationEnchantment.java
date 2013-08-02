package playerbeacons.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class DecapitationEnchantment extends Enchantment {

	protected DecapitationEnchantment(int effectID, int weight, EnumEnchantmentType enchantmentType) {
		super(effectID, weight, enchantmentType);
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
