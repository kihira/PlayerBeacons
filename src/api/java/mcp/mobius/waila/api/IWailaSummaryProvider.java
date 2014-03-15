package mcp.mobius.waila.api;

import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;

public abstract interface IWailaSummaryProvider {
    public abstract LinkedHashMap<String, String> getSummary(ItemStack paramItemStack, LinkedHashMap<String, String> paramLinkedHashMap, IWailaConfigHandler paramIWailaConfigHandler);
}