package playerbeacons.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class CrystalItem extends Item {

	protected float corruptionValue;

	public CrystalItem(int id) {
		super(id);
		//This equals one day in real time. Change it depending on how fast we calculate bad stuff
		setMaxDamage(43200);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabMaterials);
		func_111206_d("playerbeacon:grayXtal");
		setUnlocalizedName("Depleted Crystal");
		corruptionValue = 5f;
	}

	public float getCorruptionValue() {
		return corruptionValue;
	}

	/**
	 * The potion associated with this crystal
	 * @return Potion the potion type
	 */
	public Potion getBuffType() {
		return null;
	}

	/**
	 * The maximum level of buff that can be applied
	 * @return int max buff level
	 */
	public int getBuffMax() {
		return 0;
	}

	/**
	 * The amount of beacon levels required per buff level.
	 * @return int beacon levels required
	 */
	public int getBeaconLevelsPerBuffLevel() {
		return 1;
	}
}
