package net.mike.bot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mike.bot.entities.Entity;
import net.mike.bot.entities.EntityBot;
import net.mike.bot.entities.EntityFoodSpeck;
import net.mike.bot.event.Event;
import net.mike.bot.event.GlobalEventHandler;
import net.mike.bot.event.IEventHandler;

import org.lwjgl.util.vector.Vector2f;

public class SimController implements IEventHandler {
	
	private SimRegister mRegister;
	
	private List<EntityBot> botsToAdd;
	
	public SimController() {
		GlobalEventHandler.subscribeEvent(this, Event.UPDATE_ENTITIES);
		GlobalEventHandler.subscribeEvent(this, Event.DRAW_ENTITIES);
		GlobalEventHandler.subscribeEvent(this, Event.ENTITY_BOT_CREATED);
		
		mRegister = new SimRegister();
		botsToAdd = new ArrayList<EntityBot>();
		
	}
	
	private void updateEntities() {
		
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
			for (int j = i + 1; j < bots.size(); j++) {
				collideOrConsume(bot, bots.get(j));
			}
			for (EntityFoodSpeck speck : mRegister.getFoodEntityList()) {
				// Check for collision here
				collideOrConsume(bot, speck);
			}
		}
		
		// Remove dead specks
		int specksDestroyed = 0;
		foodEntityIterator = mRegister.getFoodEntityList().iterator();
		while (foodEntityIterator.hasNext()) {
			Entity entity = foodEntityIterator.next();
			if(!entity.isAlive()) {
				foodEntityIterator.remove();
				specksDestroyed++;
			}
		}
		
		for (int i = 0; i < specksDestroyed; i++) {
			GlobalEventHandler.fireEvent(Event.ENTITY_FOOD_SPECK_DESTROYED, null);
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
			boolean checkColor = bot.getColor().equals(entity.getColor());
			if (bot.getSize() == entity.getSize() || bot.getColor().equals(entity.getColor())) {
				// Same size, so bounce off
				// TODO bounce off :)
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
				
			} else {
				bot.consume(entity);
			}
		}
	}

	private void drawEntities() {
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
	
	@Override
	public void handleEvent(Event event, Object info) {
		switch (event) {
		case UPDATE_ENTITIES:
			updateEntities();
			break;
		case DRAW_ENTITIES:
			drawEntities();
			break;
		case ENTITY_BOT_CREATED:
			botsToAdd.add((EntityBot) info);
			break;
		default:
			System.err.println("Unexpected event received in SimController: " + event);
			break;
		}
		
	}

}
