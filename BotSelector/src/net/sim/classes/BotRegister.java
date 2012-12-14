package net.sim.classes;

import java.util.ArrayList;

/**
 * A model for storing bots in. 
 * @author mrh2
 *
 */
public class BotRegister {
	
	private ArrayList<Bot> botList;
	private int selected;
	
	public BotRegister() {
		botList = new ArrayList<Bot>();
	}
	
	/**
	 * To be called as a new bot is constructed, and never otherwise.
	 * Else, the bot will be duplicated in its updates and movement.
	 * @param newBot
	 */
	public void registerBot(Bot newBot) {
		botList.add(newBot);
	}

	public void update() {
		// TODO detect collisions
		for (int i = 0; i < botList.size()-1; i++) {
			for (int j = i + 1; j < botList.size(); j++) {
				if ((Math.pow(botList.get(i).getX() - botList.get(j).getX(), 2) + Math
						.pow(botList.get(i).getY() - botList.get(j).getY(), 2)) < Math
						.pow(botList.get(i).getSize()
								+ botList.get(j).getSize(), 2)) {
					System.out.println("Collision detected");
					
					double angle1 = botList.get(i).getTheta();
					double angle2 = botList.get(j).getTheta();
					double collisionAngle = 90-((angle1 + angle2)/2);
					botList.get(i).setTheta((angle1 + collisionAngle)/2);
					botList.get(j).setTheta((angle2 + collisionAngle)/2);
				}
			}
		}
		// TODO bounce off each other
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
			botList.get(i).select();
			selected = i;
		}
		
	}

	public void setSelectedMovingForward(boolean b) {
		botList.get(selected).setMovingForward(b);
	}

	public void setSelectedRotatingClockwise(boolean b) {
		botList.get(selected).setRotatingClockwise(b);
	}

	public void setSelectedRotatingAntiClockwise(boolean b) {
		botList.get(selected).setRotatingAntiClockwise(b);
	}

}
