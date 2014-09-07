package kihira.playerbeacons.common.lib;

import cpw.mods.fml.common.registry.GameRegistry;
import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.item.BeheaderItem;
import kihira.playerbeacons.common.item.PlayerBaconItem;
import kihira.playerbeacons.common.item.ResearchDiaryItem;
import kihira.playerbeacons.common.item.crystal.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import java.util.List;

@GameRegistry.ObjectHolder(PlayerBeacons.MOD_ID)
public class ModItems {
    public static final Item itemBeheader = new BeheaderItem();
    public static final Item itemPlayerBacon = new PlayerBaconItem();
    public static final Item itemResearchDiary = new ResearchDiaryItem();
    public static final Item itemLightBlueCrystal = new LightBlueCrystalItem();
    public static final Item itemBrownCrystal = new BrownCrystalItem();
    public static final Item itemGreenCrystal = new GreenCrystalItem();
    public static final Item itemRedCrystal = new RedCrystalItem();
    public static final Item itemYellowCrystal = new YellowCrystalItem();
    public static final Item itemCrystal = new CrystalItem() {
        @Override
        public float doEffects(EntityPlayer player, AbstractBeacon beacon, int crystalCount) {
            return 0;
        }

        @Override
        public List<Buff> getAffectedBuffs() {
            return null;
        }
    };

    public static void init() {
        registerMisc();
        registerCrystals();
    }

    private static void registerMisc() {
        GameRegistry.registerItem(itemBeheader, Names.BEHEADER);
        GameRegistry.registerItem(itemPlayerBacon, Names.PLAYER_BACON);
        GameRegistry.registerItem(itemResearchDiary, Names.RESEARCH_DIARY);
    }

    private static void registerCrystals() {
        GameRegistry.registerItem(itemCrystal, Names.CRYSTAL);
        GameRegistry.registerItem(itemBrownCrystal, Names.CRYSTAL_BROWN);
        GameRegistry.registerItem(itemGreenCrystal, Names.CRYSTAL_GREEN);
        GameRegistry.registerItem(itemRedCrystal, Names.CRYSTAL_RED);
        GameRegistry.registerItem(itemLightBlueCrystal, Names.CRYSTAL_LIGHT_BLUE);
        GameRegistry.registerItem(itemYellowCrystal, Names.CRYSTAL_YELLOW);
    }

    public static class Names {
        public static final String BEHEADER = "itemBeheader";
        public static final String PLAYER_BACON = "itemPlayerBacon";
        public static final String RESEARCH_DIARY = "itemResearchDiary";
        public static final String CRYSTAL = "itemCrystal";
        public static final String CRYSTAL_BROWN = "itemBrownCrystal";
        public static final String CRYSTAL_GREEN = "itemGreenCrystal";
        public static final String CRYSTAL_RED = "itemRedCrystal";
        public static final String CRYSTAL_LIGHT_BLUE = "itemLightBlueCrystal";
        public static final String CRYSTAL_YELLOW = "itemYellowCrystal";

        public static String getTextureName(String name) {
            return PlayerBeacons.MOD_ID + ":" + name;
        }
    }
}
