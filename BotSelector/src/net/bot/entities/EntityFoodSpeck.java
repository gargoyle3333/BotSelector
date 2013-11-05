package net.bot.entities;

import static net.bot.util.RandomUtil.rand;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public class EntityFoodSpeck extends Entity {
	
	private static final int MIN_FOOD_SIZE = 2;
	private static final int MAX_FOOD_SIZE = 8;
	private static final float SIZE_MULTIPLIER = 1000;
	
	public EntityFoodSpeck() {
		super();
		setSize((rand.nextInt(MAX_FOOD_SIZE - MIN_FOOD_SIZE) + MIN_FOOD_SIZE) / SIZE_MULTIPLIER);
		setPosition(new Vector2f(rand.nextFloat(), rand.nextFloat()));
		setVelocity(new Vector2f(0,0));
		setFoodLevel(getSize() * 10);
	}
	
	@Override
	public void update(double delta) {
		// No logic required
	}

	@Override
	public void draw() {
		
		glPushMatrix();
		
		Vector2f position = getPosition();
		glColor3f(1F, 1F, 1F);
		glTranslatef(position.x, position.y, 0);
		
		glBegin(GL_QUADS);
		float temp = getSize()/2;
		glVertex2f(temp, temp);
		glVertex2f(temp, -temp);
		glVertex2f(-temp, -temp);
		glVertex2f(-temp, temp);
		glEnd();
		
		glPopMatrix();
		
	}
	
	public Color getColor() {
		return new Color(255, 255, 255);
	}

	@Override
	public void consume(Entity entity) {
		// Do nothing
	}

}
