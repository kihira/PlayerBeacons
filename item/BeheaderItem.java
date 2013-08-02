package playerbeacons.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;


public class BeheaderItem extends Item {

	public BeheaderItem(int id) {
		super(id);
	}

	//What does par4 do? Metadata?
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		super.addInformation(itemStack, entityPlayer, list, par4);

		list.add("Go on, try it on!");

	}
}
