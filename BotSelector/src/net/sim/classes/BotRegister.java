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
