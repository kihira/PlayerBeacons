package mcp.mobius.waila.api;

import net.minecraft.item.ItemStack;

public abstract interface IWailaBlockDecorator {
    public abstract void decorateBlock(ItemStack paramItemStack, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler);
}
