package net.mike.blah;

import java.util.Random;

import org.lwjgl.opengl.*;;

public class Bot {
	
	private static final float MAX_SPEED = 0.3F, MAX_ROTATION = 3;
	
	private double x, y, theta;
	private BotController mBotController;
	private Random mRandom;
	private boolean selected, locked;
	
	public Bot(BotController botController, int x, int y, int theta) {
		mBotController = botController;
		mBotController.register(this);
		this.x = x;
		this.y = y;
		this.theta = theta;
		mRandom = new Random();
	}

	public void update() {
		//TODO update fields
		if (!locked) {
			x += MAX_SPEED * Math.sin(theta);
			y += MAX_SPEED * Math.cos(theta);
			if (!selected) theta += mRandom.nextInt((int)(2 * MAX_ROTATION)+1)-MAX_ROTATION;
		}
	}
	
	public void draw() {
		// TODO draw
		int size = 10;
		GL11.glBegin(GL11.GL_TRIANGLES);
		if (selected) GL11.glColor3f(1.0F, 0.0F, 0.0F);
		GL11.glVertex2f((float)x, (float)(y + (1.3 * size)));
		GL11.glVertex2f((float)(x + (size * Math.sin(30))), (float)(y - (size * Math.cos(30))));
		GL11.glVertex2f((float)(x - (size * Math.sin(30))), (float)(y - (size * Math.cos(30))));
		GL11.glEnd();
	}
	
	public void select() {
		selected = true;
	}
	
	public void deselect() {
		selected = false;
	}
	
	public void lock() {
		locked = true;
	}
	
	public void unlock() {
		locked = false;
	}

	public void move(int x2, int y2) {
		x = x2;
		y = y2;
	}

}
