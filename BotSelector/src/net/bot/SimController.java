package net.bot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.bot.entities.Entity;
import net.bot.entities.EntityBot;
import net.bot.entities.EntityFoodSpeck;
import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.EntityEventHandler;
import net.bot.event.listener.IDisplayEventListener;
import net.bot.event.listener.IEntityEventListener;

import org.lwjgl.util.vector.Vector2f;

public class SimController {
	
	private SimRegister mRegister;
	private List<EntityBot> botsToAdd;
	
	private IDisplayEventListener mDisplayListener;
	private IEntityEventListener mEntityListener;
	
	public SimController() {
		mRegister = new SimRegister();
		botsToAdd = new ArrayList<EntityBot>();
		
		// Create listeners
		mDisplayListener = new IDisplayEventListener() {
			@Override
			public void onUpdate(double delta) {
				updateEntities(delta);
				drawEntities();				
			}
		};
		mEntityListener = new IEntityEventListener() {
			
			@Override
			public void onFoodDestroyed() {}
			
			@Override
			public void onFoodCreated(EntityFoodSpeck speck) {}
			
			@Override
			public void onBotDestroyed(EntityBot bot) {}
			
			@Override
			public void onBotCreated(EntityBot bot) {
				botsToAdd.add(bot);
			}
		};
		
		// Add listeners to handlers
		DisplayEventHandler.addListener(mDisplayListener);
		EntityEventHandler.addListener(mEntityListener);
		
	}
	
	public void updateEntities(double delta) {
		
		// Check for age in food specks
		Iterator<EntityFoodSpeck> foodEntityIterator = mRegister.getFoodEntityList().iterator();
		while (foodEntityIterator.hasNext()) {
			foodEntityIterator.next().update();
		}
		
		List<EntityBot> bots = mRegister.getBotEntityList();
		// Update
		for (EntityBot bot : bots) {
			bot.update();
		}
		
		// Sort out collisions
		for (int i = 0; i < bots.size(); i++) {
			EntityBot bot = bots.get(i);
			for (int j = 0; j < bots.size(); j++) {
				if (j > i) {
					collideOrConsume(bot, bots.get(j));
				}
				if (j != i) {
					// Add forces for acceleration
					bot.addForce(bots.get(j));
				}
			}
			for (EntityFoodSpeck speck : mRegister.getFoodEntityList()) {
				// Check for collision here
				collideOrConsume(bot, speck);
				bot.addForce(speck);
			}
		}
		
		// Remove dead specks
		int specksDead = 0;
		foodEntityIterator = mRegister.getFoodEntityList().iterator();
		while (foodEntityIterator.hasNext()) {
			Entity entity = foodEntityIterator.next();
			if(!entity.isAlive()) {
				specksDead++;
				foodEntityIterator.remove();
			}
		}
		// Send events. Couldn't be done before as event causes modification to list.
		for (int i = 0; i < specksDead; i++) {
			EntityEventHandler.foodDestroyed();
		}
		
		// Remove dead bots
		Iterator<EntityBot> botIterator = bots.iterator();
		while (botIterator.hasNext()) {
			if (!botIterator.next().isAlive()) {
				botIterator.remove();
			}
		}
		
		// Add new bots
		for (EntityBot bot : botsToAdd) {
			bots.add(bot);
		}
		botsToAdd.clear();
		
	}
	
	private void collideOrConsume(EntityBot bot, Entity entity) {
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
				
			} else if (bot.getSize() < entity.getSize()) {
				if (entity instanceof EntityBot) {
					if (entity.getColor().equals(bot.getColor())) {
						
					} else {
						((EntityBot)entity).consume(bot);
					}
				} else {
					if (compare.x < entity.getSize()) {
						// Left or right face
						bot.getVelocity().x *= -1;
					} else {
						bot.getVelocity().y *= -1;
					}
				}
			} else if (bot.getColor().equals(entity.getColor())) {
				// Do nothing :-)
				return;
			} else {
				bot.consume(entity);
			}
		}
	}

	public void drawEntities() {
		Iterator<EntityBot> botEntityIterator = mRegister.getBotEntityList().iterator();
		while (botEntityIterator.hasNext()) {
			// Draw
			botEntityIterator.next().draw();
		}
		
		Iterator<EntityFoodSpeck> foodEntityIterator = mRegister.getFoodEntityList().iterator();
		while (foodEntityIterator.hasNext()) {
			foodEntityIterator.next().draw();
		}
	}
	
	public void cleanup() {
		// Clean up objects
		mRegister.cleanup();
		// Clean up event listeners
		DisplayEventHandler.removeListener(mDisplayListener);
		EntityEventHandler.removeListener(mEntityListener);
	}
	
}
