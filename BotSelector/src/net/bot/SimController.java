package net.bot;

import java.util.List;

import net.bot.entities.Entity;
import net.bot.entities.EntityBot;
import net.bot.entities.EntityDiseasedBot;
import net.bot.entities.EntityFoodSpeck;
import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.EntityEventHandler;
import net.bot.event.listener.IDisplayEventListener;
import net.bot.event.listener.IEntityEventListener;
import net.bot.input.KeyboardInput;

import org.lwjgl.util.vector.Vector2f;

public class SimController {
	
	private SimRegister mRegister;
	
	public SimController() {
		mRegister = new SimRegister();
		DisplayEventHandler.addListener(new IDisplayEventListener() {
			@Override
			public void onUpdate(double delta) {
				updateEntities(delta);
				drawEntities();
			}
		});
	}
	
	public void updateEntities(double delta) {
		
		// Check for age in food specks
		for (EntityFoodSpeck speck : mRegister.getFoodEntityList()) { 
			speck.update();
		}
		
		List<EntityBot> bots = mRegister.getBotEntityList();
		
		for (EntityBot bot : bots) {
			bot.update();
			if (!bot.isDiseased()) {
				EntityEventHandler.botDestroyed(bot);
				EntityEventHandler.botCreated(new EntityDiseasedBot(bot));
			}
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
				entity.consume(bot);
			} else if (bot.getColor().equals(entity.getColor())) {
				// Do nothing :-)
				return;
			} else {
				bot.consume(entity);
			}
		}
	}

	public void drawEntities() {
		for (EntityBot bot : mRegister.getBotEntityList()) {
			bot.draw();
		}
		for (EntityFoodSpeck speck : mRegister.getFoodEntityList()) {
			speck.draw();
		}
	}
	
	public static void main(String[] args) {
		new SimController();
		new KeyboardInput();
		MainDisplay display = new MainDisplay();
		display.run();
	}

}
