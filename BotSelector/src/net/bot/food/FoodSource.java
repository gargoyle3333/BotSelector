package net.bot.food;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRectf;
import net.bot.entities.EntityFoodSpeck;
import net.bot.event.handler.EntityEventHandler;
import net.bot.event.handler.FoodSourceEventHandler;
import net.bot.util.RandomUtil;

import org.lwjgl.util.vector.Vector2f;

public class FoodSource {

	private static final float SIZE = 0.02f;

	private float mMaxRadius, mSpawnRate, mFoodLimit;
	private Vector2f mPosition;

	private int framesAlive = 0;

	public FoodSource(float maxRadius, float spawnRate, float foodLimit,
			Vector2f position) {
		mMaxRadius = maxRadius;
		mSpawnRate = spawnRate;
		mFoodLimit = foodLimit;
		mPosition = position;
		FoodSourceEventHandler.foodSourceCreated(this);
	}

	public FoodSource(float maxRadius, float spawnRate, float foodLimit) {
		mMaxRadius = maxRadius;
		mSpawnRate = spawnRate;
		mFoodLimit = foodLimit;
		mPosition = new Vector2f(RandomUtil.rand.nextFloat(),
				RandomUtil.rand.nextFloat());
		FoodSourceEventHandler.foodSourceCreated(this);
	}

	public void draw() {
		// For the moment, we will use four squares of red, green, blue, yellow.
		// A constant size will be used.
		glPushMatrix();

		glColor3f(1, 0, 0);
		glRectf(mPosition.x - SIZE / 2, mPosition.y + SIZE / 2, mPosition.x,
				mPosition.y);

		glColor3f(0, 1, 0);
		glRectf(mPosition.x, mPosition.y + SIZE / 2, mPosition.x + SIZE / 2,
				mPosition.y);

		glColor3f(0, 0, 1);
		glRectf(mPosition.x - SIZE / 2, mPosition.y - SIZE / 2, mPosition.x,
				mPosition.y);

		glColor3f(0.5f, 0.5f, 0.5f);
		glRectf(mPosition.x, mPosition.y, mPosition.x + SIZE / 2, mPosition.y
				- SIZE / 2);

		glPopMatrix();
	}

	public void update(double delta) {
		// Update the food source; produce new food etc.
		framesAlive++;
		if (framesAlive % mSpawnRate == 0) {
			// Time to spawn!
			EntityFoodSpeck speck = new EntityFoodSpeck();
			speck.setPosition(new Vector2f(
					mPosition.x + RandomUtil.rand.nextFloat() * 2 * mMaxRadius
							- mMaxRadius, mPosition.y
							+ RandomUtil.rand.nextFloat() * 2 * mMaxRadius
							- mMaxRadius));
			mFoodLimit -= speck.getSize();
			EntityEventHandler.foodCreated(speck);
			if (mFoodLimit < 0) {
				FoodSourceEventHandler.foodSourceDestroyed(this);
			}
		}
	}

}
