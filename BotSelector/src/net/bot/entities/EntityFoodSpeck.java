package net.bot.entities;

import static net.bot.util.RandomUtil.rand;

import static org.lwjgl.opengl.GL11.*;
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
		mSize = (rand.nextInt(MAX_FOOD_SIZE - MIN_FOOD_SIZE) + MIN_FOOD_SIZE) / SIZE_MULTIPLIER;
		mFramesBeforeDeath = rand.nextInt(MAX_FRAMES_ALIVE - MIN_FRAMES_ALIVE) + MIN_FRAMES_ALIVE;
		mPosition = new Vector2f(rand.nextFloat(), rand.nextFloat());
		mVelocity = new Vector2f(0,0); 
		mFoodLevel = mSize * 10;
	}
	
	@Override
	public void update() {
		if (++mFramesAlive == mFramesBeforeDeath) {
			mState = State.STARVED;
		}
	}

	@Override
	public void draw() {
		
		glPushMatrix();
		
		glColor3f(mColor.getRed()/256F, mColor.getGreen()/256F, mColor.getBlue()/256F);
		glTranslatef(mPosition.x, mPosition.y, 0);
		
		glBegin(GL_QUADS);
		float temp = mSize/2;
		glVertex2f(temp, temp);
		glVertex2f(temp, -temp);
		glVertex2f(-temp, -temp);
		glVertex2f(-temp, temp);
		glEnd();
		
		glPopMatrix();
		
	}

}
