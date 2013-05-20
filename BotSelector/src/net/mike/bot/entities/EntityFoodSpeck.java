package net.mike.bot.entities;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class EntityFoodSpeck extends Entity {
	
	private static final int MIN_FOOD_SIZE = 2;
	private static final int MAX_FOOD_SIZE = 8;
	private static final float SIZE_MULTIPLIER = 1000;
	
	private static final int MIN_FRAMES_ALIVE = 60000;
	private static final int MAX_FRAMES_ALIVE = 600000;
	
	private int mFramesBeforeDeath;
	
	public EntityFoodSpeck() {
		super();
		Random r = new Random();
		mSize = (r.nextInt(MAX_FOOD_SIZE - MIN_FOOD_SIZE) + MIN_FOOD_SIZE) / SIZE_MULTIPLIER;
		mFramesBeforeDeath = r.nextInt(MAX_FRAMES_ALIVE - MIN_FRAMES_ALIVE) + MIN_FRAMES_ALIVE;
		mPosition = new Vector2f(r.nextFloat(), r.nextFloat());
		mVelocity = new Vector2f(0,0); 
		mFoodLevel = 0.01F;
	}
	
	@Override
	public void update() {
		if (++mFramesAlive == mFramesBeforeDeath) {
			mState = State.STARVED;
		}
	}

	@Override
	public void draw() {
		
		GL11.glPushMatrix();
		
		GL11.glColor3f(mColor.getRed()/256F, mColor.getGreen()/256F, mColor.getBlue()/256F);
		GL11.glTranslatef(mPosition.x * Display.getWidth(), mPosition.y * Display.getHeight(), 0);
		
		GL11.glBegin(GL11.GL_QUADS);
		float temp = mSize/2 * Display.getWidth();
		GL11.glVertex2f(temp, temp);
		GL11.glVertex2f(temp, -temp);
		GL11.glVertex2f(-temp, -temp);
		GL11.glVertex2f(-temp, temp);
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
	}

}
