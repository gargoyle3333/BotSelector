package net.mike.bot.entities;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public class EntityBot extends Entity {
	
	private static final int MAX_SPEED = 7;
	private static final int MIN_SPEED = 2;
	private static final float SPEED_MULTIPLIER = 1000F; // = 0.007 max, 0.002 min
	
	private static final int FRAMES_BEFORE_FOOD_DECREMENT = 60;
	private static final float FOOD_DECREMENT = 0.1F;
	
	public EntityBot() {
		super();
		Random r = new Random();
		mColor = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		mPosition = new Vector2f(r.nextFloat(), r.nextFloat());
		
		// Any neater way to do this?
		float xVel = (float) ((r.nextInt(MAX_SPEED-MIN_SPEED) + MIN_SPEED)/SPEED_MULTIPLIER);
		float yVel = (float) ((r.nextInt(MAX_SPEED-MIN_SPEED) + MIN_SPEED)/SPEED_MULTIPLIER);
		if (r.nextInt(10) % 2 == 0) xVel *= -1;
		if (r.nextInt(10) % 2 == 0) yVel *= -1;
		
		mFoodLevel = r.nextFloat();
		mVelocity = new Vector2f(xVel, yVel);
		mSize = foodToSize(mFoodLevel);
	}
	
	public EntityBot(Color color, Vector2f position, Vector2f velocity) {
		super();
		mColor = color;
		mPosition = position;
		mVelocity = velocity;
	}

	@Override
	public void update() {
		Vector2f.add(mPosition, mVelocity, mPosition);
		if (mPosition.x < 0 || mPosition.x > 1) {
			mVelocity.x *= -1;
		}
		if (mPosition.y < 0 || mPosition.y > 1) {
			mVelocity.y *= -1;
		}
		if (++mFramesAlive % FRAMES_BEFORE_FOOD_DECREMENT == 0) {
			mFoodLevel -= FOOD_DECREMENT;
		}
		if (mFoodLevel < 0) {
			mState = State.STARVED;
		}
		mSize = foodToSize(mFoodLevel);
	}

	@Override
	public void draw() {
		GL11.glPushMatrix();
		
		double angle = 0;
		if (mVelocity.x == 0) {
			angle = mVelocity.y < 0 ? 180 : 0;
		} else if (mVelocity.x > 0) {
			angle = 90 - Math.toDegrees(Math.atan(mVelocity.y / mVelocity.x));
		} else {
			angle = -90 + Math.toDegrees(Math.atan(mVelocity.y / -mVelocity.x));
		}
		
		GL11.glTranslatef(mPosition.x * Display.getWidth(), mPosition.y * Display.getHeight(), 0);
		GL11.glRotated(angle, 0D, 0D, -1D);
		GL11.glScaled((float)Display.getWidth(), (float)Display.getHeight(), 0);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glColor3f(mColor.getRed()/256F, mColor.getGreen()/256F, mColor.getBlue()/256F);
		GL11.glVertex3f(0, mSize, 0);
		GL11.glVertex3f(mSize, -mSize, 0);
		GL11.glVertex3f(-mSize, -mSize, 0);
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
	}
	
	public void consume(Entity food) {
		if (food.getState() != State.CONSUMED) {
			food.setState(State.CONSUMED);
			Vector2f.add(mVelocity, food.getVelocity(), mVelocity);
			mFoodLevel += food.getFoodLevel();
		}
	}
	
	private float foodToSize(float food) {
		return (float) (0.01 + food * 0.025);
	}
	
}
