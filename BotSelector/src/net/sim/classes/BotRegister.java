package net.sim.classes;

import java.util.ArrayList;

/**
 * A model for storing bots in. 
 * @author mrh2
 *
 */
public class BotRegister {
	
	private ArrayList<Bot> botList;
	
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

}
