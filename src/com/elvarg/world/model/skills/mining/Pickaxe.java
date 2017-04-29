package com.elvarg.world.model.skills.mining;

import com.elvarg.helper.EquipmentConstants;
import com.elvarg.world.entity.impl.player.Player;
import com.elvarg.world.model.Animation;
import com.elvarg.world.model.Item;
import com.elvarg.world.model.Skill;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by steve on 4/15/2017.
 */
public enum Pickaxe {
    BRONZE_PICKAXE(1265, 1, 8, new Animation(625)),
    IRON_PICKAXE(1267, 1, 7, new Animation(626)),
    STEEL_PICKAXE(1269, 6, 6, new Animation(627)),
    MITHRIL_PICKAXE(1273, 21, 5, new Animation(629)),
    ADAMANT_PICKAXE(1271, 31, 4, new Animation(628)),
    RUNE_PICKAXE(1275, 41, 3, new Animation(624)),
    DRAGON_PICKAXE(11920, 61, 2, new Animation(6758)),
    DRAGON_PICKAXE_OR(12797, 61, 1, new Animation(335));

    private final int item;
    private final int level;
    private final int weight;
    private final Animation animation;

    private static final HashMap<Integer, Pickaxe> PICKAXES = new HashMap<>();

    public static void declare() {
        for (Pickaxe pickaxe : values()) {
            PICKAXES.put(pickaxe.item, pickaxe);
        }
    }

    Pickaxe(int item, int level, int weight, Animation animation) {
        this.item = item;
        this.level = level;
        this.animation = animation;
        this.weight = weight;
    }

    public int getItem() {
        return item;
    }

    public int getLevel() {
        return level;
    }

    public Animation getAnimation() {
        return animation;
    }

    public int getWeight() {
        return weight;
    }

    /**
     * Get pickaxe to use
     * @param player
     * @return
     */
    public static Pickaxe get(Player player) {
        Pickaxe highest = null;

        Queue<Pickaxe> picks = new PriorityQueue<>((first, second) -> second.getLevel() - first.getLevel());

        if (player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT] != null) {
            highest = PICKAXES.get(player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT].getId());

            //If item in slot is actually a pickaxe
            if (highest != null) {
                picks.add(highest);
                highest = null;
            }
        }

        for (Item item : player.getInventory().getItems()) {
            if (item == null) {
                continue;
            }

            Pickaxe pick = PICKAXES.get(item.getId());

            if (pick == null) {
                continue;
            }

            picks.add(pick);
        }

        Pickaxe pick = picks.poll();

        if (pick == null) {
            return null;
        }

        while (player.getSkillManager().getCurrentLevel(Skill.MINING) < pick.getLevel()) {
            if (highest == null) {
                highest = pick;
            }

            pick = picks.poll();
        };

        return pick;
    }
}