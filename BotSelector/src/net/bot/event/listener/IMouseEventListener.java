package net.bot.event.listener;

public interface IMouseEventListener {
	
	void mouseClicked(float x, float y);
	void mouseMoved(float x, float y, float dX, float dY);
	void mouseDragged(float x, float y, float dX, float dY);
	void mouseReleased(float x, float y);

}
