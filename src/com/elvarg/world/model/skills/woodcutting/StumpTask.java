package com.elvarg.world.model.skills.woodcutting;

import com.elvarg.engine.task.Task;
import com.elvarg.world.collision.region.RegionClipping;
import com.elvarg.world.entity.impl.object.GameObject;

/**
 * Created by Steven on 4/13/2017.
 */

public class StumpTask extends Task {
    private GameObject object;
    private final int treeId;

    public StumpTask(GameObject object, int treeId, int delay) {
        super(delay);
        this.treeId = treeId;
        this.object = object;
    }

    @Override
    public void execute() {
        RegionClipping.removeObject(object);

        RegionClipping.addObject(new GameObject(treeId, object.getPosition(), 10, 0));
        stop();
    }

}
