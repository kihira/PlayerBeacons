package playerbeacons.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import playerbeacons.api.throttle.ICrystal;
import playerbeacons.api.buff.Buff;
import playerbeacons.common.PlayerBeacons;

import java.util.List;

public class CrystalItem extends Item implements ICrystal {

	public CrystalItem(int id) {
		super(id);
		//This equals one day in real time. Change it depending on how fast we calculate bad stuff
		setMaxDamage(43200);
		setMaxStackSize(1);
		setCreativeTab(PlayerBeacons.tabPlayerBeacons);
		setTextureName("playerbeacon:grayXtal");
		setUnlocalizedName("crystalitem");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		List buffList = getAffectedBuffs();
		if (buffList != null) {
			par3List.add("Throttles Buffs: ");
			for (Object obj:buffList) {
				par3List.add(Buff.buffs.get(obj.toString()).getName());
			}
		}
	}

	@Override
	public float getCorruptionThrottle(Buff buff, int beaconLevel, int throttleCount) {
		return 10f * throttleCount;
	}

	@Override
	public float getCorruptionReduction(float currentCorruption, int currentCorruptionLevel, int beaconLevel) {
		return 0;
	}

	@Override
	public List<String> getAffectedBuffs() {
		return null;
	}
}
