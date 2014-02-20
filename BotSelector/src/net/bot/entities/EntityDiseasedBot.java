package net.bot.entities;

import static net.bot.util.RandomUtil.rand;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

import net.bot.disease.Disease;
import net.bot.event.handler.EntityEventHandler;

public class EntityDiseasedBot extends AbstractEntityBotDecorator { //Decorator
	
	private AbstractEntityBot bot;
	
	private List<Disease> diseaseList;
	
	public EntityDiseasedBot(AbstractEntityBot bot) {
		this.bot = bot;
		diseaseList = new LinkedList<Disease>();
		diseaseList.add(new Disease(true, 1, 1, 1));
	}
	
	public EntityDiseasedBot(AbstractEntityBot bot, List<Disease> diseaseList) {
		this.bot = bot;
		this.diseaseList = diseaseList;
	}
	
	@Override
	public void update(double delta) {
		bot.update(delta);
		if (diseaseList.size() < 1) {
			EntityEventHandler.botDestroyed(this);
			EntityEventHandler.botCreated(bot);
		}
		for (Disease d : diseaseList) {
			d.update();
			if (d.checkFatality()) {
				bot.setState(State.FATAL);
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
	
	public AbstractEntityBot getBot() {
		return this.bot;
	}
	
	@Override
	public void draw() {
		bot.draw();
		glPushMatrix();
		
		double angle = 0;
		if (getVelocity().x == 0) {
			angle = getVelocity().y < 0 ? 180 : 0;
		} else if (getVelocity().x > 0) {
			angle = 90 - Math.toDegrees(Math.atan(getVelocity().y / getVelocity().x));
		} else {
			angle = -90 + Math.toDegrees(Math.atan(getVelocity().y / -getVelocity().x));
		}
		
		glTranslatef(getPosition().x, getPosition().y, 0);
		glRotated(angle, 0D, 0D, -1D);
		
		
		float size = getSize()/2;
		glBegin(GL_TRIANGLES);
		glColor3f(getColor().getBlue()/256F, getColor().getRed()/256F, getColor().getGreen()/256F);
		glVertex3f(0, size, 0);
		glVertex3f(size, -size, 0);
		glVertex3f(-size, -size, 0);
		glEnd();
		
		glPopMatrix();
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
		return true;
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
	
	@Override
	public void resolveContagiousDiseases(AbstractEntityBot bot) {
		if (bot.isDiseased()) {
			List<Disease> botDiseases = ((EntityDiseasedBot)bot).spreadDisease();
			List<Disease> newDiseases = new LinkedList<Disease>();
			// For each disease received from bot, check whether this bot already has that disease
			// Add to diseases if not
			for (Disease d: botDiseases) {
				boolean hasDisease = false;
				for (Disease e: this.diseaseList) {
					if (d.diseaseID() == e.diseaseID()) {
						hasDisease = true;
						System.out.println("hasDisease marker");
					}
				}
				if (!hasDisease) {
					newDiseases.add(d);
				}
			}
			this.diseaseList.addAll(newDiseases);
		}
	}
	
	public List<Disease> spreadDisease() { // Called from another bot to see if this bot will spread any diseases to it
		List<Disease> diseases = new LinkedList<Disease>();
		for (Disease d: this.diseaseList) {
			if (d.willSpread()) {
				diseases.add(d.clone());
			}
		}
		return diseases;
	}
}
