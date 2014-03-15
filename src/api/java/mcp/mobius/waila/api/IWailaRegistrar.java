package mcp.mobius.waila.api;

public abstract interface IWailaRegistrar {
    public abstract void addConfig(String paramString1, String paramString2, String paramString3);

    public abstract void addConfigRemote(String paramString1, String paramString2, String paramString3);

    public abstract void addConfig(String paramString1, String paramString2);

    public abstract void addConfigRemote(String paramString1, String paramString2);

    public abstract void registerStackProvider(IWailaDataProvider paramIWailaDataProvider, Class paramClass);

    public abstract void registerHeadProvider(IWailaDataProvider paramIWailaDataProvider, Class paramClass);

    public abstract void registerBodyProvider(IWailaDataProvider paramIWailaDataProvider, Class paramClass);

    public abstract void registerTailProvider(IWailaDataProvider paramIWailaDataProvider, Class paramClass);

    public abstract void registerBlockDecorator(IWailaBlockDecorator paramIWailaBlockDecorator, Class paramClass);

    public abstract void registerDocTextFile(String paramString);

    public abstract void registerShortDataProvider(IWailaSummaryProvider paramIWailaSummaryProvider, Class paramClass);
}