package playerbeacons.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EnchantmentDecapitation extends Enchantment {

	protected EnchantmentDecapitation(int effectID) {
		super(effectID, 5, EnumEnchantmentType.weapon);
		setName("Decapitation");
	}

	public int getMaxLevel() {
		return 3;
	}

	public boolean canApplyTogether(Enchantment par1Enchantment) {
		return par1Enchantment != Enchantment.fortune && par1Enchantment != Enchantment.knockback;
	}

	public int getMinEnchantability(int par1) {
		return 15 + (par1 - 1) * 9;
	}

	public int getMaxEnchantability(int par1) {
		return super.getMinEnchantability(par1) + 50;
	}
}
