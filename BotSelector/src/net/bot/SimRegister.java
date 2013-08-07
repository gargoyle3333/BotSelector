package net.bot;

import java.util.ArrayList;
import java.util.List;

import net.bot.entities.EntityBot;
import net.bot.entities.EntityFoodSpeck;
import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.EntityEventHandler;
import net.bot.event.listener.IDisplayEventListener;
import net.bot.event.listener.IEntityEventListener;
import static net.bot.util.SimRegisterConstants.*;

public class SimRegister {
	
	private List<EntityBot> mBotEntityList, mBotEntityToAdd, mBotEntityToRemove;
	private List<EntityFoodSpeck> mFoodEntityList, mFoodEntityToAdd, mFoodEntityToRemove;
	
	public SimRegister() {
		
		mBotEntityList = new ArrayList<EntityBot>();
		for (int i = 0; i < INITIAL_BOT_ENTITIES; i++) {
			mBotEntityList.add(new EntityBot());
		}
		
		mFoodEntityList = new ArrayList<EntityFoodSpeck>();
		for (int i = 0; i < FOOD_SPECKS; i++) {
			mFoodEntityList.add(new EntityFoodSpeck());
		}
		
		mBotEntityToAdd = new ArrayList<EntityBot>();
		mBotEntityToRemove = new ArrayList<EntityBot>();

		mFoodEntityToAdd = new ArrayList<EntityFoodSpeck>();		
		mFoodEntityToRemove = new ArrayList<EntityFoodSpeck>();
		
		EntityEventHandler.addListener(new IEntityEventListener() {
			@Override
			public void onFoodDestroyed(EntityFoodSpeck speck) {
				mFoodEntityToRemove.add(speck);
			}
			@Override
			public void onFoodCreated(EntityFoodSpeck speck) {
				mFoodEntityToAdd.add(speck);
			}
			@Override
			public void onBotDestroyed(EntityBot bot) {
				mBotEntityToRemove.add(bot);
			}
			@Override
			public void onBotCreated(EntityBot bot) {
				mBotEntityToAdd.add(bot);
			}
		});
		
		DisplayEventHandler.addListener(new IDisplayEventListener() {
			@Override
			public void onUpdate(double delta) {
				// Complete our add/remove operations
				
				// Food
				// Special operation when removing, in that we need to replace lost food
				for (int i = 0; i < mFoodEntityToRemove.size(); i++) {
					mFoodEntityList.remove(mFoodEntityToRemove.get(i));
					EntityEventHandler.foodCreated(new EntityFoodSpeck());
				}
				mFoodEntityToRemove.clear();
				mFoodEntityList.addAll(mFoodEntityToAdd);
				mFoodEntityToAdd.clear();
				
				// Bots
//				mBotEntityList.removeAll(mBotEntityToRemove);
				for (EntityBot bot : mBotEntityToRemove) {
					mBotEntityList.remove(bot);
				}
				mBotEntityToRemove.clear();
				mBotEntityList.addAll(mBotEntityToAdd);
				mBotEntityToAdd.clear();
			}
		});
		
	}

	public List<EntityBot> getBotEntityList() {
		return mBotEntityList;
	}

	public List<EntityFoodSpeck> getFoodEntityList() {
		return mFoodEntityList;
	}

}
