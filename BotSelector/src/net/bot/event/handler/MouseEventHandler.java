package net.bot.event.handler;

import java.util.ArrayList;
import java.util.List;

import net.bot.event.listener.IMouseEventListener;

public class MouseEventHandler {
	
	private static List<IMouseEventListener> mouseEventListeners = 
			new ArrayList<IMouseEventListener>();
	
	public static void addListener(IMouseEventListener listener) {
		mouseEventListeners.add(listener);
	}
	
	public static void removeListener(IMouseEventListener listener) {
		mouseEventListeners.add(listener);
	}
	
	public static void leftButtonPressed(float absX, float absY) {
		for (IMouseEventListener l : mouseEventListeners) {
			l.onLeftButtonPressed(absX, absY);
		}
	}
	public static void leftButtonReleased(float absX, float absY) {
		for (IMouseEventListener l : mouseEventListeners) {
			l.onLeftButtonReleased(absX, absY);
		}
	}
	public static void rightButtonPressed(float absX, float absY) {
		for (IMouseEventListener l : mouseEventListeners) {
			l.onRightButtonPressed(absX, absY);
		}
	}
	public static void rightButtonReleased(float absX, float absY) {
		for (IMouseEventListener l : mouseEventListeners) {
			l.onRightButtonReleased(absX, absY);
		}
	}
	public static void scrollWheelPressed(float absX, float absY) {
		for (IMouseEventListener l : mouseEventListeners) {
			l.onScrollWheelPressed(absX, absY);
		}
	}
	public static void scrollWheelReleased(float absX, float absY) {
		for (IMouseEventListener l : mouseEventListeners) {
			l.onScrollWheelReleased(absX, absY);
		}
	}
	public static void mouseMove(float dX, float dY, float absX, float absY) {
		for (IMouseEventListener l : mouseEventListeners) {
			l.onMouseMoved(dX, dY, absX, absY);
		}
	}

}
