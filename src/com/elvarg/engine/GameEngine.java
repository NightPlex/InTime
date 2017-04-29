package com.elvarg.engine;

import com.elvarg.engine.task.TaskManager;
import com.elvarg.world.World;
import com.elvarg.world.content.clan.ClanChatManager;
import com.elvarg.world.model.skills.mining.Ore;
import com.elvarg.world.model.skills.mining.Pickaxe;
import com.elvarg.world.model.skills.woodcutting.WoodcuttingAxeData;

/**
 * The engine which processes the game.
 * Also contains a logic service which can 
 * be used for asynchronous tasks such as file writing.
 * 
 * @author lare96
 * @author Professor Oak
 */
public final class GameEngine implements Runnable {
	
	@Override
	public void run() {
		try {
			
			TaskManager.sequence();
			World.sequence();
			//TODO: Find better place
			WoodcuttingAxeData.declare();
			Ore.declare();
			Pickaxe.declare();
					
		} catch (Throwable e) {
			e.printStackTrace();
			World.savePlayers();
			ClanChatManager.save();
		}
	}
}