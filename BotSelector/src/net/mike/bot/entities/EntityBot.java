package net.mike.bot.entities;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public class EntityBot extends Entity {
	
	private static final int MAX_SPEED_MULTIPLIED = 7;
	private static final int MIN_SPEED_MULTIPLIED = 2;
	private static final float SPEED_MULTIPLIER = 1000F; // = 0.007 max, 0.002 min
	
	public EntityBot() {
		super();
		Random r = new Random();
		mColor = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		mPosition = new Vector2f(r.nextFloat(), r.nextFloat());
		
		// Any neater way to do this?
		float xVel = (float) ((r.nextInt(MAX_SPEED_MULTIPLIED-MIN_SPEED_MULTIPLIED) + MIN_SPEED_MULTIPLIED)/SPEED_MULTIPLIER);
		float yVel = (float) ((r.nextInt(MAX_SPEED_MULTIPLIED-MIN_SPEED_MULTIPLIED) + MIN_SPEED_MULTIPLIED)/SPEED_MULTIPLIER);
		if (r.nextInt(10) % 2 == 0) xVel *= -1;
		if (r.nextInt(10) % 2 == 0) yVel *= -1;
		
		mVelocity = new Vector2f(xVel, yVel);
		mSize = 10;
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
	}

	@Override
	public void draw() {
		GL11.glPushMatrix();
		
		// TODO neaten this up!
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
		
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glColor3f(mColor.getRed()/256F, mColor.getGreen()/256F, mColor.getBlue()/256F);
		GL11.glVertex3f(0, mSize, 0);
		GL11.glVertex3f(mSize, -mSize, 0);
		GL11.glVertex3f(-mSize, -mSize, 0);
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
	}
	
}
