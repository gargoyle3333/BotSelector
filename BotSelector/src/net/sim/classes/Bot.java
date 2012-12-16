package net.sim.classes;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

public class Bot {

	private static final float MAX_SPEED = 3.0F, MAX_ROTATION = 0.05F;

	private int screenWidth, screenHeight;

	private double x, y, theta;
	private BotController mBotController;
	private boolean selected, locked;
	private boolean movingForward, rotatingClockwise, rotatingAntiClockwise;
	private int size;
	private Random mRandom;
	private Color color;
	private float hue;

	public Bot(BotController botController, int x, int y, double d, int size) {
		mBotController = botController;
		mBotController.register(this);
		this.x = x;
		this.y = y;
		this.theta = d;
		this.size = size;
		screenWidth = Display.getWidth();
		screenHeight = Display.getHeight();
		mRandom = new Random();
		
		hue = mRandom.nextFloat();
		color = new Color();
		color.fromHSB(hue, 0.9f, 0.9f);
	}
	
	public void consume(Bot that) {
		int totalSize = this.size + that.size;
		float t = this.size / (float) totalSize;
		//interpolate colors
		this.color = new Color(
				(int) (this.color.getRed() * t + that.color.getRed() * (1 - t)),
				(int) (this.color.getGreen() * t + that.color.getGreen() * (1 - t)),
				(int) (this.color.getBlue() * t + that.color.getBlue() * (1 - t))
		);
		this.size = totalSize;
	}

	public void update() {
		if (!locked) {
			// If controller by the player
			if (selected) {
				if (movingForward) {

					// Calculate new x co-ordinate
					x += MAX_SPEED * Math.sin(theta);
					x = x > screenWidth ? screenWidth : x;
					x = x < 0 ? 0 : x;

					// Calculate new y co-ordinate
					y += MAX_SPEED * Math.cos(theta);
					y = y > screenHeight ? screenHeight : y;
					y = y < 0 ? 0 : y;
				}
				// Calculate new angle
				if (rotatingClockwise) {
					theta = (theta + MAX_ROTATION);
					theta = theta > Math.PI ? -Math.PI : theta;
				}
				if (rotatingAntiClockwise) {
					theta = (theta - MAX_ROTATION);
					theta = theta < -Math.PI ? Math.PI : theta;
				}
				// If automatically moving
			} else {

				// New x co-ord
				x += MAX_SPEED * Math.sin(theta);
				// Collision with right wall
				if (x > screenWidth) {
					x = screenWidth;
					theta = -theta;
				}
				// Collision with left wall
				if (x < 0) {
					x = 0;
					theta = -theta;
				}

				// New y co-ord
				y += MAX_SPEED * Math.cos(theta);
				// Collision with top wall
				if (y > screenHeight) {
					y = screenHeight;
					theta = Math.IEEEremainder(Math.PI - theta, 2 * Math.PI);
				}
				// Collision with bottom wall
				if (y < 0) {
					y = 0;
					theta = Math.IEEEremainder(Math.PI - theta, 2 * Math.PI);
				}
			}
		}
		// Check whether to spawn new bot
		// Old cloning method, when size > 11
		// if (size > 11 && !selected) { // clone!
		// new Bot(mBotController, (int)(x+12), (int)(y+12), theta+0.04F,
		// size/2);
		// size = size/2;
		// }
		// New cloning method, 1 in 100 chance of duplicating
		if (mRandom.nextInt(20) == 1 && size >= 10 && !selected) {
			_clone();
		}
	}
	
	private void _clone() {
		int newX = (int) (x + 2 * (mRandom.nextInt(2 * size) - size));
		int newY = (int) (y + 2 * (mRandom.nextInt(2 * size) - size));
		double newTheta = -theta + Math.toRadians(mRandom.nextInt(180) - 89);
		new Bot(mBotController, newX, newY, newTheta, size / 2);
		size = size / 2;
	}
	
	private void drawTetrahedron() {
		// https://en.wikipedia.org/wiki/Tetrahedron
		final double R_CIRCUM = Math.sqrt(3 / 8d) * size;
		final double R_FACE = Math.sqrt(3 / 4d) * size;
		// get points
		final double[] front = { 0, R_CIRCUM, 0 };
		final double[] top = { 0, -R_CIRCUM / 3, R_FACE };
		final double[] left = { -size / 2, -R_CIRCUM / 3, -R_FACE / 2 };
		final double[] right = { size / 2, -R_CIRCUM / 3, -R_FACE / 2 };

		GL11.glBegin(GL11.GL_TRIANGLES);
		
		GL11.glNormal3d(-right[0], -right[1], -right[2]);
		GL11.glVertex3d(front[0], front[1], front[2]);
		GL11.glVertex3d(top[0], top[1], top[2]);
		GL11.glVertex3d(left[0], left[1], left[2]);

		GL11.glNormal3d(-left[0], -left[1], -left[2]);
		GL11.glVertex3d(right[0], right[1], right[2]);
		GL11.glVertex3d(front[0], front[1], front[2]);
		GL11.glVertex3d(top[0], top[1], top[2]);

		GL11.glNormal3d(-top[0], -top[1], -top[2]);
		GL11.glVertex3d(left[0], left[1], left[2]);
		GL11.glVertex3d(right[0], right[1], right[2]);
		GL11.glVertex3d(front[0], front[1], front[2]);

		GL11.glNormal3d(-front[0], -front[1], -front[2]);
		GL11.glVertex3d(top[0], top[1], top[2]);
		GL11.glVertex3d(left[0], left[1], left[2]);
		GL11.glVertex3d(right[0], right[1], right[2]);
		
		GL11.glEnd();
	}
	
	private void drawPyramid() {
		final Vector3f front  = new Vector3f(    0,  size,     0);

		GL11.glBegin(GL11.GL_TRIANGLES);
		int n = getSideCount();
		Vector3f last = new Vector3f(0, -size,  size);
		for(int i = 1; i <= n; i++) {
			double angle = Math.PI * 2 * i / n;
			Vector3f next = new Vector3f(size * (float) Math.sin(angle), -size, size * (float) Math.cos(angle));

			doNormal(Utils.normalTo(front, last, next));
			doVector(front);
			doVector(last);
			doVector(next);
			
			last = next;
		}
		GL11.glEnd();

		doNormal(new Vector3f(0, -1, 0));
		GL11.glBegin(GL11.GL_QUADS);
		for(int i = 1; i <= n; i++) {
			double angle = Math.PI * 2 * i / n;
			Vector3f v = new Vector3f(size * (float) Math.sin(angle), -size, size * (float) Math.cos(angle));
			doVector(v);
		}
		GL11.glEnd();
	}

	private static void doNormal(Vector3f a) {
		GL11.glNormal3d(a.getX(), a.getY(), a.getZ());
	}
	private static void doVector(Vector3f a) {
		GL11.glVertex3d(a.getX(), a.getY(), a.getZ());
	}
	
	public int getSideCount() {
		return Math.max(3, size / 10);
	}
	
	public void draw() {
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, Utils.fBuffer4(color));
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, Utils.fBuffer4(color));

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		GL11.glRotated(Math.toDegrees(theta), 0, 0, -1);
		GL11.glRotated(Math.toDegrees(theta), 0, 1, 0);
		
		drawPyramid();
		
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
		if (b && !rotatingAntiClockwise)
			rotatingClockwise = true;
		else
			rotatingClockwise = false;
	}

	public void setRotatingAntiClockwise(boolean b) {
		if (b && !rotatingClockwise)
			rotatingAntiClockwise = true;
		else
			rotatingAntiClockwise = false;
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
		return size;
	}

	public void setTheta(double _theta) {
		theta = _theta;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void updateScreenSize() {
		screenWidth = Display.getWidth();
		screenHeight = Display.getHeight();
	}

}
