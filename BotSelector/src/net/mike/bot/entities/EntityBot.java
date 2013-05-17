package net.mike.bot.entities;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public class EntityBot extends Entity {
	
	public EntityBot() {
		super();
		Random r = new Random();
		mColor = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		mPosition = new Vector2f(r.nextFloat(), r.nextFloat());
		// We should make sure that we have a velocity greater than 0.05 either way
		// and less than 0.1
		float xVel = (float) ((r.nextInt(6) + 5)/100.0);
		float yVel = (float) ((r.nextInt(6) + 5)/100.0);
		mVelocity = new Vector2f(xVel, yVel);
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
			mVelocity.setX(-mVelocity.getX());
		}
		if (mPosition.y < 0 || mPosition.x > 1) {
			mVelocity.setY(-mVelocity.getY());
		}
	}

	@Override
	public void draw() {
		
		GL11.glPushMatrix();
		
		// Rotate around z-axis
		GL11.glRotatef(Vector2f.angle(mVelocity, Entity.ZERO_VECTOR), 0, 0, 1F);
		GL11.glTranslatef(mPosition.x * Display.getWidth(), mPosition.y * Display.getHeight(), 0);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glColor3f(mColor.getRed()/256F, mColor.getGreen()/256F, mColor.getBlue()/256F);
		GL11.glVertex3f(0, mSize, 0);
		GL11.glVertex3f(mSize, -mSize, 0);
		GL11.glVertex3f(-mSize, -mSize, 0);
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
	}
	
}
