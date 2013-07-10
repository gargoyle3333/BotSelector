package net.bot.entities;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public abstract class Entity {
	
	// Gravitational constant
	protected static final double G = 0.3;
	
	protected State mState;
	protected Color mColor;
	protected Vector2f mPosition, mVelocity;
	protected int mFramesAlive = 0;
	protected float mSize, mFoodLevel;
	
	protected Vector2f mResolvedForce;
	
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
		CONSUMED
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
	
	public abstract void update();
	public abstract void draw();

}
