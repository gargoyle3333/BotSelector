package net.sim.classes;

import java.util.ArrayList;

/**
 * A model for storing bots in.
 * 
 * @author mrh2
 * 
 */
public class SimRegister {

	private ArrayList<Bot> botList, botsToAdd, botsToRemove;
	private ArrayList<FoodSpeck> speckList, specksToAdd, specksToRemove;
	
	private Bot selected;

	public SimRegister() {
		botList = new ArrayList<Bot>();
		botsToAdd = new ArrayList<Bot>();
		botsToRemove = new ArrayList<Bot>();
		selected = null;
		speckList = new ArrayList<FoodSpeck>();
		specksToAdd = new ArrayList<FoodSpeck>();
		specksToRemove = new ArrayList<FoodSpeck>();
	}

	/**
	 * To be called as a new bot is constructed, and never otherwise. Else, the
	 * bot will be duplicated in its updates and movement.
	 * 
	 * @param newBot
	 */
	public void registerBot(Bot newBot) {
		botsToAdd.add(newBot);
	}

	public void update() {
		updateBots();
		updateFood();
	}
	
	private void updateFood() {
		speckList.removeAll(specksToRemove);
		specksToRemove.clear();
		speckList.addAll(specksToAdd);
		specksToAdd.clear();
		for (FoodSpeck speck : speckList) {
			speck.update();
			speck.draw();
		}
	}

	private void updateBots() {
		if (botsToAdd.size() > 0) {
			botList.addAll(botsToAdd);
			botsToAdd.clear();
		}
		for (int i = 0; i < botList.size() - 1; i++) {
			for (int j = i + 1; j < botList.size(); j++) {
				Bot a = botList.get(i), b = botList.get(j);
				double dx = a.getX() - b.getX(), dy = a.getY() - b.getY();
				double sizeA = a.getSize(), sizeB = b.getSize();
				if (dx * dx + dy * dy < (sizeA + sizeB) * (sizeA + sizeB)) {
					if (sizeA > sizeB) {
						botsToRemove.add(b);
						a.consume(b);
					} else if (sizeA < sizeB) {
						botsToRemove.add(a);
						b.consume(a);
					} else {
						double angle1 = a.getTheta();
						double angle2 = b.getTheta();
						double collisionAngle = Math.PI
								- ((angle1 + angle2) / 2);
						if (a != selected)
							a.setTheta(2 * collisionAngle + angle1);
						if (b != selected)
							b.setTheta(2 * collisionAngle + angle2);
					}
				}
			}
		}

		botList.removeAll(botsToRemove);
		botsToRemove.clear();

		for (Bot bot : botList) {
			bot.update();
			bot.draw();
		}
	}

	public Bot getBot(int p) {
		return botList.get(p);
	}

	public void selectBot(int i) {
		if (i < botList.size()) {
			selected = botList.get(i);
			selected.select();
		}
	}

	public void setSelectedMovingForward(boolean b) {
		selected.setMovingForward(b);
	}

	public void setSelectedRotatingClockwise(boolean b) {
		selected.setRotatingClockwise(b);
	}

	public void setSelectedRotatingAntiClockwise(boolean b) {
		selected.setRotatingAntiClockwise(b);
	}

	public void pickupBotNearest(int x, int y) {
		Bot nearestBot = null;
		double nearestDistance = Double.POSITIVE_INFINITY;
		for (Bot bot : botList) {
			double currentXDistSquared = Math.pow(bot.getX() - x, 2);
			double currentYDistSquared = Math.pow(bot.getY() - y, 2);
			if (currentXDistSquared + currentYDistSquared < nearestDistance) {
				nearestDistance = currentXDistSquared + currentYDistSquared;
				nearestBot = bot;
			}
		}
		if (selected != null)
			selected.deselect();
		
		//This will NullPointer if the list is empty
		selected = nearestBot;
		selected.select();
		selected.lock();
		selected.setX(x);
		selected.setY(y);
	}

	public void release() {
		selected.unlock();
	}

	public void dragSelected(int x, int y) {
		selected.setX(x);
		selected.setY(y);
	}

	public void updateScreenSize() {
		for (Bot bot : botList) {
			bot.updateScreenSize();
		}
		
	}

	public void registerSpeck(FoodSpeck speck) {
		specksToAdd.add(speck);
	}
}
