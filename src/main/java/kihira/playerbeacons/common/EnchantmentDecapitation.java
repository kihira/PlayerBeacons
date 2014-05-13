package kihira.playerbeacons.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

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
	public boolean canApplyTogether(Enchantment enchantment) {
		return enchantment != null && enchantment.effectId != Enchantment.fortune.effectId && enchantment.effectId != Enchantment.knockback.effectId && super.canApplyTogether(enchantment);
	}

    public boolean canApply(ItemStack itemStack) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, itemStack) == 0 && super.canApply(itemStack);
    }

    @Override
	public int getMinEnchantability(int par1) {
		return 15 + (par1 - 1) * 9;
	}

    @Override
	public int getMaxEnchantability(int par1) {
		return super.getMinEnchantability(par1) + 50;
	}
}
