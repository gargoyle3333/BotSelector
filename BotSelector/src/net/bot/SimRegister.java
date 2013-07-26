package net.bot;

import java.util.ArrayList;
import java.util.List;

import net.bot.entities.EntityBot;
import net.bot.entities.EntityFoodSpeck;
import net.bot.event.handler.EntityEventHandler;
import net.bot.event.listener.IEntityEventListener;
import static net.bot.util.SimRegisterConstants.*;

public class SimRegister {
	
	private List<EntityBot> mBotEntityList;
	private List<EntityFoodSpeck> mFoodEntityList;
	
	private IEntityEventListener mEntityListener;
	
	public SimRegister() {
		
		mBotEntityList = new ArrayList<EntityBot>();
		for (int i = 0; i < INITIAL_BOT_ENTITIES; i++) {
			mBotEntityList.add(new EntityBot());
		}
		
		mFoodEntityList = new ArrayList<EntityFoodSpeck>();
		for (int i = 0; i < FOOD_SPECKS; i++) {
			mFoodEntityList.add(new EntityFoodSpeck());
		}
		
		mEntityListener = new IEntityEventListener() {
			@Override
			public void onFoodDestroyed() {
				// When one speck is destroyed, add another to keep a constant supply
				mFoodEntityList.add(new EntityFoodSpeck());
			}
			@Override
			public void onFoodCreated(EntityFoodSpeck speck) {}
			@Override
			public void onBotDestroyed(EntityBot bot) {}
			@Override
			public void onBotCreated(EntityBot bot) {}
		};
		
		EntityEventHandler.addListener(mEntityListener);
		
	}

	public List<EntityBot> getBotEntityList() {
		return mBotEntityList;
	}

	public List<EntityFoodSpeck> getFoodEntityList() {
		return mFoodEntityList;
	}
	
	public void cleanup() {
		EntityEventHandler.removeListener(mEntityListener);
	}

}
