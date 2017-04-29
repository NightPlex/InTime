package com.elvarg.world.model.skills.mining;

import java.util.HashMap;

/**
 * Created by steve on 4/15/2017.
 */
public enum Ore {
    COPPER("Copper ore", new int[] { 13708, 13709 }, 1, 17, new int[] { 436 }, 10081, 6, 4),
    TIN("Tin ore", new int[] { 13712, 13713 }, 1, 17, new int[] { 438 }, 10081, 6, 4),
    IRON("Iron ore", new int[] { 13710, 13711 }, 15, 35, new int[] { 440 }, 10081, 12, 7),
    SILVER_ORE("Silver ore", new int[] { 13716, 13717 }, 20, 40, new int[] { 442 }, 10081, 15, 10),
    COAL_ORE("Coal ore", new int[] { 13706, 13714 }, 30, 50, new int[] { 453 }, 10081, 12, 10),
    GOLD_ORE("Gold ore", new int[] { 13707, 13715 }, 40, 65, new int[] { 444 }, 10081, 15, 10),
    MITHRIL_ORE("Mithril ore", new int[] { 7492, 7459 }, 55, 80, new int[] { 447 }, 10081, 15, 13),
    ADAMANT_ORE("Adamant ore", new int[] { 13720, 14168 }, 70, 95, new int[] { 449 }, 10081, 15, 16),
    RUNITE_ORE("Runite ore", new int[] { 14175 }, 85, 125, new int[] { 451 }, 10081, 15, 18),
    ESSENCE("Essence", new int[] { 14912, 2491 }, 30, 10, new int[] { 1436 }, -1, -1, -1),
    GEM_ROCK("Gem Rock", new int[] { 14856, 14855, 14854 }, 40, 65, new int[] { 1625, 1627, 1629, 1623, 1621, 1619, 1617 }, 10081, 135, 140);

    private final String name;
    private int[] objects;
    private final int level;
    private final int exp;
    private final int[] ore;
    private final int replacement;
    private final int respawn;
    private final int immunity;

    private static final HashMap<Integer, Ore> ORES = new HashMap<>();

    public static void declare() {
        for (Ore ore : values()) {
            for (int object : ore.objects) {
                ORES.put(object, ore);
            }
        }
    }

    Ore(String name, int[] objects, int level, int exp, int[] ore, int replacement, int respawn, int immunity) {
        this.name = name;
        this.objects = objects;
        this.level = level;
        this.exp = exp;
        this.ore = ore;
        this.replacement = replacement;
        this.respawn = respawn;
        this.immunity = immunity;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int[] getOre() {
        return ore;
    }

    public int getReplacement() {
        return replacement;
    }

    public int getRespawn() {
        return respawn;
    }

    public int getImmunity() {
        return immunity;
    }

    public static Ore get(int id) {
        return ORES.get(id);
    }
}