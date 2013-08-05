package playerbeacons.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EnchantmentDecapitation extends Enchantment {

	protected EnchantmentDecapitation(int effectID) {
		super(effectID, 5, EnumEnchantmentType.weapon);
		setName("decapitation");
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean canApplyTogether(Enchantment par1Enchantment) {
		return par1Enchantment != Enchantment.fortune && par1Enchantment != Enchantment.knockback;
	}
}
