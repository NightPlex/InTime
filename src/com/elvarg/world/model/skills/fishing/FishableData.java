package com.elvarg.world.model.skills.fishing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steven on 4/16/2017.
 */
public class FishableData {

    private static final int LOBSTER_POT = 301;
    private static final int SMALL_FISHING_NET = 303;
    private static final int BIG_FISHING_NET = 305;
    private static final int FISHING_ROD = 307;
    private static final int FLY_FISHING_ROD = 309;
    private static final int HARPOON = 311;
    private static final int FISHING_BAIT = 313;
    private static final int DARK_FISHING_BAIT = 11940;

    public enum Fishable {
        SHRIMP(317, SMALL_FISHING_NET, 1, 10, -1),
        CRAYFISH(13435, 13431, 1, 10, -1),
        KARAMBWANJI(3150, SMALL_FISHING_NET, 5, 5, -1),
        SARDINE(327, FISHING_ROD, 5, 20, FISHING_BAIT),


        HERRING(345, FISHING_ROD, 10, 30, FISHING_BAIT),
        ANCHOVIES(321, SMALL_FISHING_NET, 15, 40, -1),
        MACKEREL(353, BIG_FISHING_NET, 16, 20, -1),
        TROUT(335, FLY_FISHING_ROD, 20, 50, 314),
        COD(341, BIG_FISHING_NET, 23, 45, -1),
        PIKE(349, FISHING_ROD, 25, 60, FISHING_BAIT),
        SLIMY_EEL(3379, FISHING_ROD, 28, 65, FISHING_BAIT),
        SALMON(331, FLY_FISHING_ROD, 30, 70, 314),
        FROG_SPAWN(5004, SMALL_FISHING_NET, 33, 75, -1),
        TUNA(359, HARPOON, 35, 80, -1),
        CAVE_EEL(5001, FISHING_ROD, 38, 80, FISHING_BAIT),
        LOBSTER(377, LOBSTER_POT, 40, 90, -1),
        BASS(363, BIG_FISHING_NET, 46, 100, -1),
        SWORD_FISH(371, HARPOON, 50, 100, -1),
        LAVA_EEL(2148, FISHING_ROD, 53, 30, FISHING_BAIT),
        MONK_FISH(7944, SMALL_FISHING_NET, 62, 110, -1),
        KARAMBWAN(3142, 3157, 65, 100, -1),
        SHARK(383, HARPOON, 76, 125, -1),
        SEA_TURTLE(395, -1, 79, 38, -1),
        MANTA_RAY(389, HARPOON, 81, 155, -1),
        DARK_CRAB(11934, 301, 85, 205, DARK_FISHING_BAIT);

        public static final void declare() {
            for (Fishable fishes : values())
                fish.put(Integer.valueOf(fishes.getRawFishId()), fishes);
        }

        private short rawFishId;
        private short toolId;
        private short levelRequired;
        private short baitRequired;
        private int experienceGain;

        private static Map<Integer, Fishable> fish = new HashMap<>();

        public static Fishable forId(int rawFishId) {
            return fish.get(Integer.valueOf(rawFishId));
        }

        private Fishable(int rawFishId, int toolId, int levelRequired, int experienceGain, int baitRequired) {
            this.rawFishId = ((short) rawFishId);
            this.toolId = ((short) toolId);
            this.levelRequired = ((short) levelRequired);
            this.experienceGain = experienceGain;
            this.baitRequired = ((short) baitRequired);
        }

        public short getBaitRequired() {
            return baitRequired;
        }

        public int getExperience() {
            return experienceGain;
        }

        public short getRawFishId() {
            return rawFishId;
        }

        public short getRequiredLevel() {
            return levelRequired;
        }

        public short getToolId() {
            return toolId;
        }
    }
}