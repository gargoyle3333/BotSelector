package net.mike.bot.entities;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public abstract class Entity {
	
	public static final Vector2f ZERO_VECTOR = new Vector2f(0, 0);
	
	protected State mState;
	protected Color mColor;
	protected Vector2f mPosition, mVelocity;
	protected int mSize;
	
	public Entity() {
		mState = State.ALIVE;
		mColor = new Color(1, 1, 1);
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
