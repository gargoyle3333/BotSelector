package net.bot;

import static net.bot.util.SimRegisterConstants.FOOD_SPECKS;
import static net.bot.util.SimRegisterConstants.INITIAL_BOT_ENTITIES;

import java.util.ArrayList;
import java.util.List;

import net.bot.entities.AbstractEntityBot;
import net.bot.entities.Entity;
import net.bot.entities.Entity.State;
import net.bot.entities.EntityBot;
import net.bot.entities.EntityDiseasedBot;
import net.bot.entities.EntityFoodSpeck;
import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.EntityEventHandler;
import net.bot.event.handler.FoodSourceEventHandler;
import net.bot.event.listener.IDisplayEventListener;
import net.bot.event.listener.IEntityEventListener;
import net.bot.event.listener.IFoodSourceEventListener;
import net.bot.food.FoodSource;
import net.bot.input.KeyboardInput;
import net.bot.util.RandomUtil;

import org.lwjgl.util.vector.Vector2f;

public class MainModel {
	
	private List<AbstractEntityBot> mBotEntityList, mBotsToAdd, mBotsToRemove;
	private List<EntityFoodSpeck> mFoodEntityList, mFoodToAdd, mFoodToRemove;
	private List<FoodSource> mFoodSourceList, mFoodSourceToAdd, mFoodSourceToRemove;
	
	public MainModel() {
		
		DisplayEventHandler.addListener(new IDisplayEventListener() {
			@Override
			public void onUpdate(double delta) {
				updateEntities(delta);
				drawEntities();
				updateFoodSources(delta);
				drawFoodSources();
			}
			
		});
		
		// 
		
		mBotEntityList = new ArrayList<AbstractEntityBot>();
		for (int i = 0; i < INITIAL_BOT_ENTITIES; i++) {
			mBotEntityList.add(new EntityBot());
		}
		
		mFoodEntityList = new ArrayList<EntityFoodSpeck>();
		for (int i = 0; i < FOOD_SPECKS; i++) {
			mFoodEntityList.add(new EntityFoodSpeck());
		}
		
		mBotsToAdd = new ArrayList<AbstractEntityBot>();
		mBotsToRemove = new ArrayList<AbstractEntityBot>();

		mFoodToAdd = new ArrayList<EntityFoodSpeck>();		
		mFoodToRemove = new ArrayList<EntityFoodSpeck>();
			EntityEventHandler.addListener(new IEntityEventListener() {
			@Override
			public void onFoodDestroyed(EntityFoodSpeck speck) {
				mFoodEntityList.remove(speck);
			}
			@Override
			public void onFoodCreated(EntityFoodSpeck speck) {
				mFoodEntityList.add(speck);
			}
			@Override
			public void onBotDestroyed(AbstractEntityBot bot) {
				mBotEntityList.remove(bot);
			}
			@Override
			public void onBotCreated(AbstractEntityBot bot) {
				mBotEntityList.add(bot);
			}
		});
			
		mFoodSourceList = new ArrayList<FoodSource>();
		for (int i = 0; i < 5; i++) {
			mFoodSourceList.add(new FoodSource(0.1f, 20, RandomUtil.rand.nextFloat() * 15f));
		}
		
		mFoodSourceToAdd = new ArrayList<FoodSource>();
		mFoodSourceToRemove = new ArrayList<FoodSource>();
		FoodSourceEventHandler.addListener(new IFoodSourceEventListener() {
			
			@Override
			public void onFoodSourceDestroyed(FoodSource source) {
				mFoodSourceToRemove.add(source);
			}
			
			@Override
			public void onFoodSourceCreated(FoodSource source) {
				mFoodSourceToAdd.add(source);
			}
		});
		
	}
	
	public void updateEntities(double delta) {
		
		// Check for age in food specks
		for (EntityFoodSpeck speck : mFoodEntityList) { 
			speck.update(delta);
		}
		
		// TODO remove after disease testing
	/*	for (AbstractEntityBot bot: mBotEntityList) {
			if (!bot.isDiseased()) {
				EntityDiseasedBot newBot = new EntityDiseasedBot(bot);
				mBotsToAdd.add(newBot);
				mBotsToRemove.add(bot);
			}
		} */
		
		// Sort out collisions
		for (int i = 0; i < mBotEntityList.size(); i++) {
			AbstractEntityBot bot = mBotEntityList.get(i);
			bot.update(delta);
			

			for (int j = 0; j < mBotEntityList.size(); j++) {
				if (j > i) {
					collideOrConsume(bot, mBotEntityList.get(j));
				}
				if (j != i) {
					// Add forces for acceleration
					bot.addForce(mBotEntityList.get(j));
				}
			}
			for (EntityFoodSpeck speck : mFoodEntityList) {
				// Check for collision here
				collideOrConsume(bot, speck);
				bot.addForce(speck);
			}
		}
		
		for (AbstractEntityBot bot : mBotEntityList) {
			if (bot.getState() != State.ALIVE) {
				mBotsToRemove.add(bot);
			}
		}
		mBotEntityList.removeAll(mBotsToRemove);
		mBotsToRemove.clear();
		mBotEntityList.addAll(mBotsToAdd);
		mBotsToAdd.clear();
		
		for (EntityFoodSpeck food : mFoodEntityList) {
			if (food.getState() == State.CONSUMED) {
				mFoodToRemove.add(food);
//				mFoodToAdd.add(new EntityFoodSpeck());
			}
		}
		mFoodEntityList.removeAll(mFoodToRemove);
		mFoodToRemove.clear();
		mFoodEntityList.addAll(mFoodToAdd);
		mFoodToAdd.clear();
		
	}
	
	private void updateFoodSources(double delta) {
		for (FoodSource source : mFoodSourceList) {
			source.update(delta);
		}
		mFoodSourceList.removeAll(mFoodSourceToRemove);
		mFoodSourceToRemove.clear();
		mFoodSourceList.addAll(mFoodSourceToAdd);
		mFoodSourceToAdd.clear();
	}
	
	private void drawFoodSources() {
		for (FoodSource source : mFoodSourceList) {
			source.draw();
		}
	}
	
	private void collideOrConsume(AbstractEntityBot bot, Entity entity) {
		Vector2f compare = new Vector2f();
		Vector2f.sub(bot.getPosition(), entity.getPosition(), compare);
		if (compare.length() <= bot.getSize() + entity.getSize()) {
			// Collision!!
			if (bot.getSize() == entity.getSize()) {
				// Same size or same colour, so bounce off
				Vector2f newBot = new Vector2f();
				Vector2f newEntity = new Vector2f();
				float massA = bot.getSize(), massB = entity.getSize();
				
				Vector2f velA = new Vector2f(bot.getVelocity().x, bot.getVelocity().y);
				Vector2f velB = new Vector2f(entity.getVelocity().x, entity.getVelocity().y);
				
				velA.scale((float)((massA - massB)/(massA+massB)));
				velB.scale((float)((massB * 2)/(massA+massB)));
				Vector2f.add(velA, velB, newBot);
				
				velA = new Vector2f(bot.getVelocity().x, bot.getVelocity().y);
				velB = new Vector2f(entity.getVelocity().x, entity.getVelocity().y);
				
				velB.scale((float)((massB - massA)/(massA+massB)));
				velA.scale((float)((massA * 2)/(massA+massB)));
				Vector2f.add(velA, velB, newEntity);
				
				bot.setVelocity(newBot);
				entity.setVelocity(newEntity);
				if (entity instanceof AbstractEntityBot) {
					bot.resolveContagiousDiseases((AbstractEntityBot) entity);
				}
			} else if (bot.getColor().equals(entity.getColor())) {
				if (entity instanceof AbstractEntityBot) {
					bot.resolveContagiousDiseases((AbstractEntityBot) entity);
				}
				return;
			} else if (bot.getSize() < entity.getSize()) {
				entity.consume(bot);
			} else if (bot.getSize() > entity.getSize()) {
				bot.consume(entity);
			}
//			else if (bot.getSize() < entity.getSize()) {
//				entity.consume(bot);
//			} else if (bot.getColor().equals(entity.getColor())) {
//				// Do nothing :-)
//				return;
//			} else {
//				bot.consume(entity);
//			}
		}
	}

	public void drawEntities() {
		for (AbstractEntityBot bot : mBotEntityList) {
			bot.draw();
		}
		for (EntityFoodSpeck speck : mFoodEntityList) {
			speck.draw();
		}
	}
	
	public static void main(String[] args) {
		new MainModel();
		new KeyboardInput();
		MainDisplay display = new MainDisplay();
		display.run();
	}

}
