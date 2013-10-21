package playerbeacons.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import playerbeacons.buff.Buff;
import playerbeacons.common.PlayerBeacons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewCrystalItem extends Item {

	protected float corruptionValue;
	private HashMap<String, Icon> crystalIcon = new HashMap<String, Icon>();
	public static List<String> crystalList = new ArrayList<String>();

	public NewCrystalItem(int id) {
		super(id);
		//This equals one day in real time. Change it depending on how fast we calculate bad stuff
		setMaxDamage(43200);
		setMaxStackSize(1);
		setCreativeTab(PlayerBeacons.tabPlayerBeacons);
		setUnlocalizedName("crystalItem");
		corruptionValue = 10f;
		crystalList.add("lightblue");
		crystalList.add("brown");
		crystalList.add("black");
		crystalList.add("green");
	}

	public static ItemStack makeCrystal(String name) {
		ItemStack itemStack = new ItemStack(PlayerBeacons.newCrystalItem);
		NBTTagCompound tagCompound = new NBTTagCompound();
		NBTTagCompound tagCompound1 = new NBTTagCompound();
		tagCompound1.setString("CrystalName" ,"depleted");
		tagCompound.setCompoundTag("PlayerBeacons", tagCompound1);
		itemStack.setTagCompound(tagCompound);
		return itemStack;
	}

	public static List getBuffs(String crystalName) {
		List list = new ArrayList<String>();
		if (crystalName.equals("lightblue")) list.add("speed");
		else if (crystalName.equals("green")) list.add("jump");
		else if (crystalName.equals("brown")) list.add("dig");
		else if (crystalName.equals("black")) list.add("resistance");
		else list = null;
		return list;
	}

	public static float getCorruptionReduction(String name) {
		return 10f;
	}

	public static String getSimpleCrystalName(ItemStack itemStack) {
		if (itemStack.getItem() instanceof NewCrystalItem && itemStack.hasTagCompound()) {
			NBTTagCompound tagCompound = itemStack.getTagCompound();
			tagCompound = tagCompound.getCompoundTag("PlayerBeacons");
			return tagCompound.getString("CrystalName");
		}
		return null;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		String crystalName = getSimpleCrystalName(par1ItemStack);
		List buffList = getBuffs(crystalName);
		if (buffList != null) {
			par3List.add("Throttles Buffs: ");
			for (Object obj:buffList) {
				par3List.add(Buff.buffs.get(obj.toString()).getName());
			}
		}
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		NBTTagCompound tagCompound1 = new NBTTagCompound();
		tagCompound1.setString("CrystalName", "depleted");
		tagCompound.setCompoundTag("PlayerBeacons", tagCompound1);
		ItemStack stack = new ItemStack(par1, 1, 0);
		stack.setTagCompound(tagCompound);
		par3List.add(stack);
		//TODO Make a global list
		for (Object buff : crystalList) {
			stack = new ItemStack(par1, 1, 0);
			tagCompound = new NBTTagCompound();
			tagCompound1 = new NBTTagCompound();
			tagCompound1.setString("CrystalName", buff.toString());
			tagCompound.setCompoundTag("PlayerBeacons", tagCompound1);
			stack.setTagCompound(tagCompound);
			par3List.add(stack);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		tagCompound = tagCompound.getCompoundTag("PlayerBeacons");
		return "item.crystalitem." + tagCompound.getString("CrystalName");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public int getRenderPasses(int metadata) {
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(ItemStack stack, int pass) {
		if (crystalIcon.get(getSimpleCrystalName(stack)) == null) {
			return crystalIcon.get("depleted");
		}
		return crystalIcon.get(getSimpleCrystalName(stack));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		//TODO A list of crystals <String, NewCrystalItem>? then loop here
		crystalIcon.put("lightblue", iconRegister.registerIcon("playerbeacon:lightblueXtal"));
		crystalIcon.put("green", iconRegister.registerIcon("playerbeacon:greenXtal"));
		crystalIcon.put("brown", iconRegister.registerIcon("playerbeacon:brownXtal"));
		//TODO Make black crystal. Maybe use similar system to potions?
		crystalIcon.put("black", iconRegister.registerIcon("playerbeacon:redXtal"));
		crystalIcon.put("depleted", iconRegister.registerIcon("playerbeacon:grayXtal"));
	}
}