package net.bot.event.listener;

public interface IMouseEventListener {
	
	void onLeftButtonPressed(float absX, float absY);
	void onLeftButtonReleased(float absX, float absY);
	
	void onRightButtonPressed(float absX, float absY);
	void onRightButtonReleased(float absX, float absY);
	
	void onScrollWheelPressed(float absX, float absY);
	void onScrollWheelReleased(float absX, float absY);
	
	void onMouseMoved(float dX, float dY, float absX, float absY);

}
