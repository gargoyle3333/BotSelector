package net.mike.bot;

import java.util.ArrayList;
import java.util.Iterator;

import net.mike.bot.entities.Entity;
import net.mike.bot.entities.EntityBot;
import net.mike.bot.event.Event;
import net.mike.bot.event.GlobalEventHandler;
import net.mike.bot.event.IEventHandler;

public class SimController implements IEventHandler {
	
	private static final int INITIAL_BOTS = 20;
	
	private ArrayList<EntityBot> botEntityList;

	public SimController() {
		GlobalEventHandler.subscribeEvent(this, Event.UPDATE_ENTITIES);
		GlobalEventHandler.subscribeEvent(this, Event.DRAW_ENTITIES);
		
		botEntityList = new ArrayList<EntityBot>();
		for (int i = 0; i < INITIAL_BOTS; i++) {
			botEntityList.add(new EntityBot());
		}
		
	}
	
	private void updateEntities() {
		Iterator<EntityBot> botEntityIterator = botEntityList.iterator();
		while (botEntityIterator.hasNext()) {
			// Update
			botEntityIterator.next().update();
		}
	}
	
	private void drawEntities() {
		Iterator<EntityBot> botEntityIterator = botEntityList.iterator();
		while (botEntityIterator.hasNext()) {
			// Update
			botEntityIterator.next().draw();
		}
	}
	
	@Override
	public void handleEvent(Event event, Object info) {
		switch (event) {
		case UPDATE_ENTITIES:
			updateEntities();
			break;
		case DRAW_ENTITIES:
			drawEntities();
			break;
		}
		
	}

}
