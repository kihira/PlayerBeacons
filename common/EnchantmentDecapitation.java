package playerbeacons.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EnchantmentDecapitation extends Enchantment {

	protected EnchantmentDecapitation(int effectID, int weight, EnumEnchantmentType enchantmentType) {
		super(effectID, weight, enchantmentType);
		setName("decapitation");
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean canApplyTogether(Enchantment par1Enchantment) {
		if (par1Enchantment == Enchantment.fortune) return false;
		else if (par1Enchantment == Enchantment.knockback) return false;
		else return true;
	}
}
