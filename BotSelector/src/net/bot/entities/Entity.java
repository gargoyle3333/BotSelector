package net.bot.entities;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public abstract class Entity {
	
	// Gravitational constant
	private static final double G = 0.00003;
	
	private State mState;
	private Color mColor;
	private Vector2f mPosition, mVelocity;
	private int mFramesAlive = 0;
	private float mSize, mFoodLevel;
	
	public void setFoodLevel(float foodLevel) {
		this.mFoodLevel = foodLevel;
	}

	public Vector2f getPosition() {
		return mPosition;
	}

	public void setPosition(Vector2f position) {
		this.mPosition = position;
	}

	public Vector2f getVelocity() {
		return mVelocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.mVelocity = velocity;
	}

	public float getSize() {
		return mSize;
	}

	public Color getColor() {
		return mColor;
	}

	public void setColor(Color color) {
		this.mColor = color;
	}

	public void setSize(float size) {
		this.mSize = size;
	}
	
	public float getFoodLevel() {
		return mFoodLevel;
	}
	
	
	public Entity() {
		mState = State.ALIVE;
		mColor = new Color(255, 255, 255);
	}
	
	public enum State {
		ALIVE,
		STARVED,
		CONSUMED,
		FATAL
	}
	
	public boolean isAlive() {
		return mState == State.ALIVE;
	}
	
	public State getState() {
		return mState;
	}
	
	public void setState(State newState) {
		mState = newState;
	}
	
	public abstract void update(double delta);
	public abstract void draw();
	public abstract void consume(Entity entity);

}
