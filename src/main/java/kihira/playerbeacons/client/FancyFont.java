package kihira.playerbeacons.client;

import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.util.ResourceLocation;
import truetyper.FontLoader;
import truetyper.TrueTypeFont;

public class FancyFont {

    public static final TrueTypeFont fontSmall = FontLoader.createFont(new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "fonts/daniel.ttf"), 10F, false);
    public static final TrueTypeFont fontNormal = FontLoader.createFont(new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "fonts/daniel.ttf"), 16F, false);
    public static final TrueTypeFont fontLarge = FontLoader.createFont(new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "fonts/daniel.ttf"), 22F, false);
    public static final TrueTypeFont fontBlack = FontLoader.createFont(new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "fonts/Daniel-Black.otf"), 16F, true);
    public static final TrueTypeFont fontComicSans = FontLoader.loadSystemFont("Comic Sans MS Regular", 22F, true);
}
