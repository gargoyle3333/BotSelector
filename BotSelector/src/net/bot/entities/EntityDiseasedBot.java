package net.bot.entities;

import java.util.ArrayList;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

import net.bot.disease.Disease;
import net.bot.event.handler.EntityEventHandler;

public class EntityDiseasedBot extends EntityBot { //Decorator
	
	private EntityBot bot;
	
	private ArrayList<Disease> diseaseList;
	
	public EntityDiseasedBot(EntityBot bot) {
		this.bot = bot;
		bot.setDiseased(true);
		diseaseList = new ArrayList<Disease>();
		diseaseList.add(new Disease(true, 1, 1));
	}
	
	@Override
	public void update() {
		bot.update();
		if (diseaseList.size() < 1) {
			EntityEventHandler.botDestroyed(this);
			EntityEventHandler.botCreated(bot);
		}
		for (Disease d : diseaseList) {
			d.update();
			if (d.checkFatality()) {
				bot.mState = State.FATAL;
			}
		}
//		testBotAndClean(this);
	}
	
	public void addDisease(Disease disease) {
		diseaseList.add(disease);
	}
	
	public boolean isClean() {
		return diseaseList.isEmpty();
	}
	
	public EntityBot getBot() {
		return this.bot;
	}
	
	@Override
	public void draw() {
		bot.draw();
	}
	
	@Override
	public void consume(Entity food) {
		bot.consume(food);
	}
	
	@Override
	public void addForce(Entity entity) {
		bot.addForce(entity);
	}
	
	@Override
	public boolean isDiseased() {
		return bot.isDiseased();
	}
	
	@Override
	public void setDiseased(boolean isDiseased) {
		bot.setDiseased(isDiseased);
	}
	
	@Override
	public Vector2f getPosition() {
		return bot.getPosition();
	}
	
	@Override
	public void setPosition(Vector2f position) {
		bot.setPosition(position);
	}
	
	@Override
	public Vector2f getVelocity() {
		return bot.getVelocity();
	}

	@Override
	public void setVelocity(Vector2f velocity) {
		bot.setVelocity(velocity);
	}

	@Override
	public float getSize() {
		return bot.getSize();
	}

	@Override
	public Color getColor() {
		return bot.getColor();
	}

	@Override
	public void setColor(Color color) {
		bot.setColor(color);
	}

	@Override
	public void setSize(float size) {
		bot.setSize(size);
	}
	
	@Override
	public float getFoodLevel() {
		return bot.getFoodLevel();
	}
	
	@Override
	public boolean isAlive() {
		return bot.isAlive();
	}
	
	@Override
	public State getState() {
		return bot.getState();
	}
	
	@Override
	public void setState(State newState) {
		bot.setState(newState);
	}
}
