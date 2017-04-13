package com.elvarg.world.model.skills.woodcutting;

import com.elvarg.engine.task.Task;
import com.elvarg.engine.task.TaskManager;
import com.elvarg.world.World;
import com.elvarg.world.collision.region.RegionClipping;
import com.elvarg.world.entity.impl.object.GameObject;
import com.elvarg.world.entity.impl.player.Player;
import com.elvarg.world.model.*;

/**
 * Created by Steven on 4/13/2017.
 */

public class WoodCuttingLogic extends Task {

    /**
     * Attempts to chop a tree down
     *
     * @param player
     *            The player woodcutting
     * @param objectId
     *            The id of the object
     * @param position
     *
     *
     *
     */
    public static void attemptWoodcutting(Player player, int objectId, Position position) {

        GameObject object = new GameObject(objectId, position, 10, 0);
        WoodcuttingTreeData tree = WoodcuttingTreeData.forId(object.getId());
        if (tree == null) {
            return;
        }

        if (!meetsRequirements(player, tree, object)) {
            return;
        }

        WoodcuttingAxeData[] axes = new WoodcuttingAxeData[15];

        int d = 0;

        for (int s : AXES) {
            if (player.getEquipment().getItems()[3] != null && player.getEquipment().getItems()[3].getId() == s) {
                axes[d] = WoodcuttingAxeData.forId(s);
                d++;
                break;
            }
        }

        // if (axes == null) {
        if (d == 0) {
            for (Item i : player.getInventory().getItems()) {
                if (i != null) {
                    for (int c : AXES) {
                        if (i.getId() == c) {
                            axes[d] = WoodcuttingAxeData.forId(c);
                            d++;
                            break;
                        }
                    }
                }
            }
        }
        // }

        int skillLevel = 0;
        int anyLevelAxe = 0;
        int index = -1;
        int indexb = 0;

        for (WoodcuttingAxeData i : axes) {
            if (i != null) {
                if (meetsAxeRequirements(player, i)) {
                    if (index == -1 || i.getLevelRequired() > skillLevel) {
                        index = indexb;
                        skillLevel = i.getLevelRequired();
                    }
                }

                anyLevelAxe = i.getLevelRequired();
            }

            indexb++;
        }

        if (index == -1) {
            if (anyLevelAxe != 0) {
                player.getPacketSender().sendMessage("You need a woodcutting level of " + anyLevelAxe + " to use this axe.");
            } else {
                player.getPacketSender().sendMessage("Yo do not have an axe");
            }
            return;
        }

        WoodcuttingAxeData axe = axes[index];
        player.getPacketSender().sendMessage("You attempt to swing your axe at the tree");
        player.performAnimation(axe.getAnimation());
        //TODO: direction is not working
        player.setDirection(Direction.EAST);
        //Place new task: TODO: The fuck?
        TaskManager.submit(new WoodCuttingLogic(player, objectId, tree, object, axe));
    }

    /**
     * Gets if the player meets the requirements to chop the tree with the axe
     *
     * @param player
     *            The player chopping the tree
     * @param data
     *            The data for the axe the player is wielding
     * @return
     */
    private static boolean meetsAxeRequirements(Player player, WoodcuttingAxeData data) {
        if (data == null) {
            player.getPacketSender().sendMessage("You do not have a hatchet");
            return false;
        }
        if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < data.getLevelRequired()) {
            return false;
        }
        return true;
    }

    /**
     * Gets if the player meets the requirements to chop the tree
     *
     * @param player
     *            The player chopping the tree
     * @param data
     *            The tree data
     * @param object
     *            The tree object
     * @return
     */
    private static boolean meetsRequirements(Player player, WoodcuttingTreeData data, GameObject object) {
        if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < data.getLevelRequired()) {
            player.getPacketSender().sendMessage("You need a woodcutting level of " + data.getLevelRequired() + " to cut this tree.");
            return false;
        }
        //Check if object even exists
        if(RegionClipping.objectExists(object.getId(), object.getPosition())) {
            return false;
        }
        //Full?
        if (player.getInventory().getFreeSlots() == 0) {
            player.performAnimation(new Animation(-1,0));
            player.getPacketSender().sendMessage("You don't have enough inventory space left");
            return false;
        }
        return true;
    }

    /**
     * Constructs a new player instance
     */
    private Player player;

    /**
     * The tree the player is chopping
     */
    private GameObject object;

    /**
     * The woodcutting axe data for the axe the player is using
     */
    private WoodcuttingAxeData axe;
    /**
     * The woodcutting tree data for the tree the player is chopping
     */
    private WoodcuttingTreeData tree;
    /**
     * The id of the tree the player is chopping
     */
    private final int treeId;

    /**
     * The animation cycle for the chopping animation
     */
    private int animationCycle;

    private int pos;

    /**
     * An array of normal tree ids
     */
    private final int[] NORMAL_TREES = { 1278, 1276 };

    /**
     * An array of axes starting from the best to the worst
     */
    private static final int[] AXES = { 13661, 6739, 1359, 1357, 1355, 1361, 1353, 1349, 1351 };

    /**
     * Constructs a new woodcutting task
     *
     * @param player
     * @param treeId
     * @param tree
     * @param object
     * @param axe
     */
    public WoodCuttingLogic(Player player, int treeId, WoodcuttingTreeData tree, GameObject object, WoodcuttingAxeData axe) {
        super(1, false);
        this.player = player;
        this.object = object;
        this.tree = tree;
        this.axe = axe;
        this.treeId = treeId;
    }

    /**
     * Sends the animation to swing the axe
     */
    private void animate() {

        player.getPacketSender().sendSound(472, 5, 0);

        //TODO: figure out what it does
        if (++animationCycle == 1) {
            player.performAnimation(axe.getAnimation());
            animationCycle = 0;
        }
    }

    @Override
    public void execute() {
        if (!meetsRequirements(player, tree, object)) {
            stop();
            return;
        }

        if (pos == 3) {
            if ((successfulAttemptChance()) && (handleTreeChopping())) {
                stop();
                return;
            }

            pos = 0;
        } else {
            pos += 1;
        }

        animate();
    }

    /**
     * Handles giving a log after cutting a tree
     */
    private void handleGivingLogs() {
        player.getInventory().add(new Item(tree.getReward(), 1));
        player.getSkillManager().addExperience(Skill.WOODCUTTING, tree.getExperience());

    }

    /**
     * Handles chopping a tree down
     *
     * @return
     */
    private boolean handleTreeChopping() {
        if (isNormalTree()) {
            successfulAttempt();
            return true;
        }

      /*  if (Utility.randomNumber(9 + (World.getActivePlayers() / 50)) == 1) {
            successfulAttempt();
            return true;
        }*/

        handleGivingLogs();

        return false;
    }

    /**
     * Gets if the tree is a normal tree or not
     *
     * @return
     */
    private boolean isNormalTree() {
        for (int i : NORMAL_TREES) {
            if (i == treeId) {
                return true;
            }
        }
        return false;
    }



    /**
     * Handles chopping a tree down
     */
    private void successfulAttempt() {
        player.getPacketSender().sendSound(1312, 5, 0);
        player.getPacketSender().sendMessage("You successfully chop down the tree.");
        player.getInventory().add(new Item(tree.getReward(), 1));
        player.getSkillManager().addExperience(Skill.WOODCUTTING, tree.getExperience());
        player.performAnimation(new Animation(65535));

        //TODO: achivement????????????

        GameObject replacement = new GameObject(tree.getReplacement(), object.getPosition(), 10, 0);


        if (replacement != null) {
            RegionClipping.addObject(replacement);
            RegionClipping.removeObject(replacement);
            TaskManager.submit(new StumpTask(object, treeId, tree.getRespawnTimer()));
        }
    }

    /**
     * Gets if the chop was a successful attempt
     *
     * @return
     */
    private boolean successfulAttemptChance() {
        return isSuccess(player, Skill.WOODCUTTING, tree.getLevelRequired(), axe.getLevelRequired());
    }

    public final boolean isSuccess(Player p, Skill skillId, int levelRequired, int toolLevelRequired) {
        double level = (99 + toolLevelRequired)/2;
        double req = levelRequired;
        double successChance = Math.ceil((level * 50.0D - req * 15.0D) / req / 3.0D * 4.0D);
        int roll = 0;//Utility.randomNumber(99);

        if (successChance >= roll) {
            return true;
        }

        return false;
    }
}
