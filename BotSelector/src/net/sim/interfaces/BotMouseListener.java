package net.sim.interfaces;

public interface BotMouseListener {

	void leftButtonClicked(int x, int y);
	void leftButtonReleased(int x, int y);
	
	void rightButtonClicked(int x, int y);
	void rightButtonReleased(int x, int y);
	
	void leftDragged(int x, int y);
	void rightDragged(int x, int y);
	
}
