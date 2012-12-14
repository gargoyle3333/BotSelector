package net.sim.classes;

import java.util.Random;

import org.lwjgl.opengl.*;;

public class Bot {
	
	private static final float MAX_SPEED = 2.0F, MAX_ROTATION = 0.05F;
	private static final int SIZE = 10;
	
	private int screenWidth, screenHeight;
	
	private double x, y, theta, delta;
	private BotController mBotController;
	private Random mRandom;
	private boolean selected, locked;
	private boolean movingForward, rotatingClockwise, rotatingAntiClockwise;
	
	public Bot(BotController botController, int x, int y, double d) {
		mBotController = botController;
		mBotController.register(this);
		this.x = x;
		this.y = y;
		this.theta = d;
		delta = 0;
		mRandom = new Random();
		screenWidth = Display.getWidth();
		screenHeight = Display.getHeight();
	}

	public void update() {
		if (!locked) {
			if (selected) {
				if (movingForward) {
					x += MAX_SPEED * Math.sin(theta);
					x = x > screenWidth ? screenWidth : x;
					x = x < 0 ? 0 : x;
					
					y += MAX_SPEED * Math.cos(theta);
					y = y > screenHeight ? screenHeight : y;
					y = y < 0 ? 0 : y;
				}
				if (rotatingClockwise) {
					theta = (theta + MAX_ROTATION);
					theta = theta > (Math.PI) ? (-1 * Math.PI) : theta;
				}
				if (rotatingAntiClockwise) {
					theta = (theta - MAX_ROTATION);
					theta = theta < (-1 * Math.PI) ? (Math.PI) : theta;
				}
			} else {
				x += MAX_SPEED * Math.sin(theta);
				if (x > screenWidth) {
					x = screenWidth;
					theta = -1 * theta;
				}
				if (x < 0) {
					x = 0;
					theta = -1 * theta;
				}
				
				y += MAX_SPEED * Math.cos(theta);
				if (y > screenHeight) {
					y = screenHeight;
					theta = Math.IEEEremainder(Math.PI - theta, 2* Math.PI);
				}
				if (y < 0) {
					y = 0;
					theta = Math.IEEEremainder(Math.PI - theta, 2* Math.PI);
				}
				y = y > screenHeight ? screenHeight : y;
				y = y < 0 ? 0 : y;
			}
		}
	}
	
	public void draw() {
		if (selected) GL11.glColor3f(1.0F, 0.0F, 0.0F);
		else GL11.glColor3f(1.0F, 1.0F, 1.0F);

		GL11.glPushMatrix();
		GL11.glTranslated(x,y,0);
		GL11.glRotated(Math.toDegrees(theta), 0, 0, -1);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
		
		GL11.glVertex2f(0f, (float)SIZE);
		GL11.glVertex2f((float)(SIZE * Math.sin(Math.toRadians(60))), (float)(-SIZE * Math.cos(Math.toRadians(60))));
		GL11.glVertex2f((float)(-SIZE * Math.sin(Math.toRadians(60))), (float)((-SIZE * Math.cos(Math.toRadians(60)))));
		
		GL11.glEnd();
		
		GL11.glPopMatrix();
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

	public void setMovingForward(boolean b) {
		movingForward = b;
	}

	public void setRotatingClockwise(boolean b) {
		if (b && !rotatingAntiClockwise) rotatingClockwise = true;
		else rotatingClockwise = false;
	}

	public void setRotatingAntiClockwise(boolean b) {
		if (b && !rotatingClockwise) rotatingAntiClockwise = true;
		else rotatingAntiClockwise = false;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getTheta() {
		return theta;
	}
	
	public double getSize() {
		return SIZE;
	}

}
