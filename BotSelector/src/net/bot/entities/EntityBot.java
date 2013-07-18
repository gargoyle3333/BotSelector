package net.bot.entities;

import net.bot.event.handler.EntityEventHandler;
import static net.bot.util.RandomUtil.rand;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public class EntityBot extends Entity {
	
	private static final int MAX_SPAWN_SPEED = 7;
	private static final int MIN_SPAWN_SPEED = 2;
	private static final float SPEED_MULTIPLIER = 1000F; // = 0.007 max, 0.002 min
	private static final float MAX_SPEED = 0.003F;
	
	private static final int FRAMES_BEFORE_FOOD_DECREMENT = 60;
	private static final float FOOD_DECREMENT = 0.005F;
	
	private static final float OFFSPRING_PROPORTION = 0.3F;
	private static final float OFFSPRING_MIN_FOOD = 0.2F;
	private static final float OFFSPRING_MAX_FOOD = 50F;
	
	private static final float MAXIMUM_FORCE_DISTANCE = 0.3F;
	
	public EntityBot() {
		super();
		mColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		mPosition = new Vector2f(rand.nextFloat(), rand.nextFloat());
		
		// Any neater way to do this?
		float xVel = (float) ((rand.nextInt(MAX_SPAWN_SPEED-MIN_SPAWN_SPEED) + MIN_SPAWN_SPEED)/SPEED_MULTIPLIER);
		float yVel = (float) ((rand.nextInt(MAX_SPAWN_SPEED-MIN_SPAWN_SPEED) + MIN_SPAWN_SPEED)/SPEED_MULTIPLIER);
		if (rand.nextFloat() >= 0.5) xVel *= -1;
		if (rand.nextFloat() >= 0.5) yVel *= -1;
		
		mFoodLevel = rand.nextFloat();
		mVelocity = new Vector2f(xVel, yVel);
		mSize = foodToSize(mFoodLevel);
		mResolvedForce = new Vector2f(0,0);
		
	}
	
	public EntityBot(Color color, Vector2f position, Vector2f velocity, float foodLevel) {
		super();
		mColor = color;
		mPosition = position;
		mVelocity = velocity;
		mFoodLevel = foodLevel;
		mSize = foodToSize(foodLevel);
		mResolvedForce = new Vector2f(0,0);
	}

	@Override
	public void update() {
		// New position
		Vector2f.add(mPosition, mVelocity, mPosition);
		Vector2f.add(mVelocity, (Vector2f) mResolvedForce.scale((float) (1.0/mSize)), mVelocity);
		// Bounce off walls
		if (mPosition.x + mVelocity.x < 0 || mPosition.x + mVelocity.x > 1) {
			mVelocity.x *= -1;
		}
		if (mPosition.y + mVelocity.y < 0 || mPosition.y + mVelocity.y > 1) {
			mVelocity.y *= -1;
		}
		// Too old?
		if (++mFramesAlive % FRAMES_BEFORE_FOOD_DECREMENT == 0) {
			mFoodLevel -= FOOD_DECREMENT;
		}
		if (mFoodLevel < 0) {
			mState = State.STARVED;
		}
		
		// The bigger the bot, the more likely it is to spawn offspring
		if (rand.nextFloat() < chanceOfSpawn(mFoodLevel, OFFSPRING_MIN_FOOD, OFFSPRING_MAX_FOOD)) {
			spawnClone();
		}
		
		float l;
		if ((l = mVelocity.length()) > MAX_SPEED) {
			mVelocity.set((mVelocity.x/l)*MAX_SPEED, (mVelocity.y/l)*MAX_SPEED);
		}
		
		mSize = foodToSize(mFoodLevel);
		mResolvedForce = new Vector2f(0,0);
	}


	private float chanceOfSpawn(float currentFoodLevel, float minFoodLevel,
			float maxFoodLevel) {
		return (currentFoodLevel-minFoodLevel)/(maxFoodLevel-minFoodLevel);
	}

	@Override
	public void draw() {
		glPushMatrix();
		
		double angle = 0;
		if (mVelocity.x == 0) {
			angle = mVelocity.y < 0 ? 180 : 0;
		} else if (mVelocity.x > 0) {
			angle = 90 - Math.toDegrees(Math.atan(mVelocity.y / mVelocity.x));
		} else {
			angle = -90 + Math.toDegrees(Math.atan(mVelocity.y / -mVelocity.x));
		}
		
		glTranslatef(mPosition.x, mPosition.y, 0);
		glRotated(angle, 0D, 0D, -1D);
		
		glBegin(GL_TRIANGLES);
		glColor3f(mColor.getRed()/256F, mColor.getGreen()/256F, mColor.getBlue()/256F);
		glVertex3f(0, mSize, 0);
		glVertex3f(mSize, -mSize, 0);
		glVertex3f(-mSize, -mSize, 0);
		glEnd();
		
		glPopMatrix();
		
	}
	
	public void consume(Entity food) {
		if (food.getState() != State.CONSUMED) {
			food.setState(State.CONSUMED); // Set food to eaten
			mFoodLevel += food.getFoodLevel(); // Eat food
			
			// Update momentum using masses, instead of assuming same mass
			mVelocity.scale(mSize);
			food.getVelocity().scale(food.getSize());
			Vector2f.add(mVelocity, food.getVelocity(), mVelocity);
			mVelocity.scale((float) (1.0/(mSize + food.getSize())));
			
		}
	}
	
	private float foodToSize(float food) {
		// y = mx + c
		return (float) (0.01 + food * 0.025);
	}
	
	/**
	 * Spawns a clone based on the parent's attributes. This method will scale down the parent bot and change its
	 * momentum as the new bot is spawned.
	 * 
	 * @param color The parent bot's colour.
	 * @param position The parent bot's position vector.
	 * @param velocity The parent bot's velocity vector.
	 * @param foodLevel The parent bot's food level.
	 * @param offspringProportion The size of the offspring as a proportion of the parent bot.
	 */
	private void spawnClone() {
		float offspringFoodLevel = OFFSPRING_PROPORTION * mFoodLevel;
		/*
		 * Algorithm:
		 * Generate offspring vector 
		 * 	0.8x to 1.5x original vector speed
		 * 	-90<theta<90 rotation of original vector
		 * Alter original velocity to conserve momentum
		 * v1 = (m1u1-m3v2)/(m1-m3)
		 */
		
		// Generate theta: -PI/2 < THETA < PI/2
		float theta = (float) (Math.PI * (rand.nextFloat() - 0.5));
		// Use trig to get new velocity. Should be the same speed as the parent
		float x = (float) (mVelocity.x * Math.cos(theta) - mVelocity.y * Math.sin(theta));
		float y = (float) (mVelocity.x * Math.sin(theta) + mVelocity.y * Math.cos(theta));
		
		Vector2f offspringVelocity = new Vector2f(x, y);
		
		// 0.8x to 1.5x vector allowed, so calculate the scale, then scale the new velocity
		float lowerBound = 0.8F, upperBound = 1.5F;
		float scale = rand.nextFloat() * (upperBound-lowerBound) + lowerBound;
		offspringVelocity.scale(scale);
		
		// Copy the parent velocity so we can scale without messing things up
		Vector2f velocityCopy = new Vector2f(mVelocity.x, mVelocity.y);
		// Scale to new size, m1u1
		velocityCopy.scale(foodToSize(mFoodLevel));
		
		// Calculate m3v2
		offspringVelocity.scale(foodToSize(offspringFoodLevel));
		Vector2f newMomentum = new Vector2f();
		// m1u1-m3v2
		Vector2f.sub(velocityCopy, offspringVelocity, newMomentum);
		// Undo scale from before 
		offspringVelocity.scale((float) (1.0/(foodToSize(offspringFoodLevel))));
		
		// Divide by m1-m3
		newMomentum.scale((float) (1.0/(foodToSize(mFoodLevel)-OFFSPRING_PROPORTION)));
		mVelocity = newMomentum;
		
		// Reset food level of parent
		mFoodLevel -= offspringFoodLevel;
		
		Vector2f offspringPosition = new Vector2f(mPosition.x, mPosition.y);
		
		// Shift the little babby out of the way of big mummy by backtracking 60 frames of velocity
		offspringVelocity.scale(60F);
		Vector2f.sub(offspringPosition, offspringVelocity, offspringPosition);
		offspringVelocity.scale((float) (1/60.0));
		
		Color offspringColor = new Color(mColor.getRed(), mColor.getGreen(), mColor.getBlue());
		
		EntityBot offspring = new EntityBot(offspringColor, offspringPosition, offspringVelocity, offspringFoodLevel);
		EntityEventHandler.botCreated(offspring);
	}
	
	/**
	 * Given an entity and a constant G, calculates and adds force acting upon the bot.
	 * Also negates the force if the other entity is larger.
	 * @param entity
	 */
	public void addForce(Entity entity) {
		// Check distance
		Vector2f distance = new Vector2f(entity.getPosition().x - mPosition.x, entity.getPosition().y - mPosition.y);
		if (distance.length() > MAXIMUM_FORCE_DISTANCE) return;
		double force = (G * (mSize * entity.getSize()))/distance.lengthSquared();
		double theta = Vector2f.angle(mPosition, entity.getPosition());
		Vector2f resolved = new Vector2f((float)(force * Math.sin(theta)), (float)(force * Math.cos(theta)));
		
		// Run from larger entity or same species.
		if (entity.getSize() > mSize || entity.getColor().equals(mColor)) {
			resolved.negate();
		}
		Vector2f.add(mResolvedForce, resolved, mResolvedForce);
	}
	
}
