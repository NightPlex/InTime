package com.elvarg.engine.task;

import com.elvarg.world.entity.Entity;
import com.elvarg.world.model.Animation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class TaskManager {

	private final static Queue<Task> pendingTasks = new LinkedList<>();

	private final static List<Task> activeTasks = new LinkedList<>();

	private TaskManager() {
		throw new UnsupportedOperationException(
				"This class cannot be instantiated!");
	}

	public static void sequence() {
		try {
			Task t;
			while ((t = pendingTasks.poll()) != null) {
				if (t.isRunning()) {
					activeTasks.add(t);
				}
			}

			Iterator<Task> it = activeTasks.iterator();

			while (it.hasNext()) {
				t = it.next();
				if (!t.tick())
					it.remove();
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void submit(Task task) {
		if(!task.isRunning())
			return;
		if (task.isImmediate()) {
			task.execute();
		}
		pendingTasks.add(task);
	}

	public static void cancelTasks(Object key) {
		try {
			pendingTasks.stream().filter(t -> t.getKey().equals(key)).forEach(t -> t.stop());
			activeTasks.stream().filter(t -> t.getKey().equals(key)).forEach(t -> t.stop());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void onMovement(Entity e) {
		List<Task> active = activeTasks;


		if (active != null) {
			for (Iterator<Task> i = active.iterator(); i.hasNext();) {
				Task t = i.next();
				if (t.getBreakType() == Task.BreakType.ON_MOVE) {
					t.stop();
					e.performAnimation(new Animation(-1,0));
					i.remove();
				}
			}
		}
	}

	public static int getTaskAmount() {
		return (pendingTasks.size() + activeTasks.size());
	}
}
