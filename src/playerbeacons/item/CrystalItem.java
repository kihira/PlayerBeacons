package playerbeacons.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import playerbeacons.common.PlayerBeacons;

import java.util.List;

public class CrystalItem extends Item {

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
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (!par2World.isRemote) {
			ItemStack itemStack = new ItemStack(PlayerBeacons.newCrystalItem);
			NBTTagCompound tagCompound = new NBTTagCompound();
			NBTTagCompound tagCompound1 = new NBTTagCompound();
			if (par1ItemStack.itemID == PlayerBeacons.digCrystalItem.itemID) tagCompound1.setString("CrystalName", "brown");
			else if (par1ItemStack.itemID == PlayerBeacons.jumpCrystalItem.itemID) tagCompound1.setString("CrystalName", "green");
			else if (par1ItemStack.itemID == PlayerBeacons.speedCrystalItem.itemID) tagCompound1.setString("CrystalName", "lightblue");
			else if (par1ItemStack.itemID == PlayerBeacons.resCrystalItem.itemID) tagCompound1.setString("CrystalName", "black");
			else tagCompound1.setString("CrystalName" ,"depleted");
			tagCompound.setCompoundTag("PlayerBeacons", tagCompound1);
			itemStack.setTagCompound(tagCompound);
			par3EntityPlayer.setCurrentItemOrArmor(0, itemStack);
		}
		return par1ItemStack;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add("DEPRECIATED: Right click whilst holding to update");
	}
}
