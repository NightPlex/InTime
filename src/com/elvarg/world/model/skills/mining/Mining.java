package com.elvarg.world.model.skills.mining;

import com.elvarg.GameConstants;
import com.elvarg.Utility;
import com.elvarg.engine.task.Task;
import com.elvarg.engine.task.TaskManager;
import com.elvarg.world.entity.impl.object.GameObject;
import com.elvarg.world.entity.impl.player.Player;
import com.elvarg.world.model.Animation;
import com.elvarg.world.model.Position;
import com.elvarg.world.model.Skill;


/**
 * Created by Steven on 4/15/2017.
 */
public class Mining{

    public static boolean clickRock(Player player, int objId, Position position) {
        GameObject gameObject = new GameObject(objId, position, 10, 0);

        if (gameObject == null) {
            return false;
        }

        Ore ore = Ore.get(gameObject.getId());

        if (ore == null) {
            return false;
        }

        if (player.getSkillManager().getCurrentLevel(Skill.MINING) < ore.getLevel()) {
            player.getPacketSender().sendMessage("You need a Mining level of " + ore.getLevel() + " to mine that ore.");
            return false;
        }

        Pickaxe pickaxe = Pickaxe.get(player);

        if (pickaxe == null) {
            player.getPacketSender().sendMessage("You do not have a pickaxe");

            return false;
        }

        if (player.getSkillManager().getCurrentLevel(Skill.MINING) < pickaxe.getLevel()) {
            player.getPacketSender().sendMessage("You need a Mining level of " + pickaxe.getLevel() + " to use that pickaxe.");
            return false;
        }

        // TODO: wildy ?
        /*if (player. || player.getCombat().getAttacking() != null) {
            player.send(new SendMessage("You can't do that right now!"));
            return false;
        }*/

        if (player.getInventory().isFull()) {
            player.performAnimation(new Animation(-1, 0));
            player.getPacketSender().sendMessage("Your inventory is full");
            return false;
        }

        player.getPacketSender().sendMessage("You swing your pick at the rock.");

        int ticks = ore.getImmunity() == -1 ? 2 : ore.getImmunity() - (int) ( (player.getSkillManager().getCurrentLevel(Skill.MINING) - ore.getLevel()) * 2 / (double) pickaxe.getWeight());
        int gemTick = ore.getImmunity();


        if (ticks < 1) {
            ticks = 1;
        }

        int time = ore.getName().equalsIgnoreCase("gem rock") ? gemTick : ticks;
        System.out.println("Ticks: " + ticks);
        System.out.println("Time: " + time);


        TaskManager.submit(new Task(1,false, Task.BreakType.ON_MOVE) {
            int ticks = 0;

            @Override
            public void execute() {
                if (player.getInventory().getFreeSlots() == 0) {
                    player.getPacketSender().sendMessage("Your inventory is full!");
                    stop();
                    return;
                }

                if (ticks++ == time/* /DEAD_ORES.contains(new Locations.Location(object.getX(), object.getY(), object.getZ()))*/) {
                    if(ore == Ore.ESSENCE) {
                        player.getInventory().add(ore.getOre()[0], Utility.randomNumber(2));
                        player.getSkillManager().addExperience(Skill.MINING, ore.getExp() * GameConstants.EXP_MULTIPLIER);
                        //TODO: acheivement
                        ticks = 0;
                        if (player.getInventory().getFreeSlots() == 0) {
                            player.getPacketSender().sendMessage("Your inventory is full!");
                            stop();
                        }
                        return;
                    }

                    if (ore != null) {
                        player.getInventory().add(ore.getOre()[0], 1);
                        player.getSkillManager().addExperience(Skill.MINING, ore.getExp() * GameConstants.EXP_MULTIPLIER);
                        //TODO: acheivement
                        ticks = 0;
                        if (player.getInventory().getFreeSlots() == 0) {
                            player.performAnimation(new Animation(-1, 0));
                            player.getPacketSender().sendMessage("Your inventory is full!");
                            stop();
                        }
                        return;
                    } else {
                        stop();
                        return;
                    }
                }

                player.performAnimation(pickaxe.getAnimation());
            }

        });

        return true;
    }
}