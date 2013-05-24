package net.mike.bot;

import java.util.ArrayList;
import java.util.List;

import net.mike.bot.entities.EntityBot;
import net.mike.bot.entities.EntityFoodSpeck;
import net.mike.bot.event.Event;
import net.mike.bot.event.GlobalEventHandler;
import net.mike.bot.event.IEventHandler;

public class SimRegister implements IEventHandler {
	
	private static final int INITIAL_BOT_ENTITIES = 30;
	private static final int FOOD_SPECKS = 30;
	
	private List<EntityBot> mBotEntityList;
	private List<EntityFoodSpeck> mFoodEntityList;
	
	public SimRegister() {
		
		GlobalEventHandler.subscribeEvent(this, Event.ENTITY_FOOD_SPECK_DESTROYED);
		
		mBotEntityList = new ArrayList<EntityBot>();
		for (int i = 0; i < INITIAL_BOT_ENTITIES; i++) {
			mBotEntityList.add(new EntityBot());
		}
		
		mFoodEntityList = new ArrayList<EntityFoodSpeck>();
		for (int i = 0; i < FOOD_SPECKS; i++) {
			mFoodEntityList.add(new EntityFoodSpeck());
		}
		
	}

	public List<EntityBot> getBotEntityList() {
		return mBotEntityList;
	}

	public List<EntityFoodSpeck> getFoodEntityList() {
		return mFoodEntityList;
	}

	@Override
	public void handleEvent(Event event, Object info) {
		switch (event) {
		case ENTITY_FOOD_SPECK_DESTROYED:
			mFoodEntityList.add(new EntityFoodSpeck());
			break;
		default:
			System.err.println("Unexpected event received in SimRegister: " + event);
			break;
		}
	}

}
