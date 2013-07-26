package net.bot.event.handler;

import java.util.ArrayList;
import java.util.List;

import net.bot.event.listener.IKeyboardEventListener;
import net.bot.event.listener.IMouseEventListener;

public class MouseEventHandler {
	
	private static List<IMouseEventListener> mouseEventListeners = new ArrayList<IMouseEventListener>();
	private static List<IMouseEventListener> removeListeners = new ArrayList<IMouseEventListener>();
	private static List<IMouseEventListener> addListeners = new ArrayList<IMouseEventListener>();

	public static void addListener(IMouseEventListener listener) {
		addListeners.add(listener);
	}
	
	public static void removeListener(IMouseEventListener listener) {
		removeListeners.add(listener);
	}
	
	private static void addToListenerList() {
		for (IMouseEventListener l : addListeners) {
			mouseEventListeners.add(l);
		}
		addListeners.clear();
	}
	
	private static void removeFromListenerList() {
		for (IMouseEventListener l : removeListeners) {
			mouseEventListeners.remove(l);
		}
		removeListeners.clear();
	}
	
	// Actual events
	public static void mouseClicked(float x, float y) {
		addToListenerList();
		for (IMouseEventListener l : mouseEventListeners) {
			l.mouseClicked(x, y);
		}
		removeFromListenerList();
	}
	public static void mouseReleased(float x, float y) {
		addToListenerList();
		for (IMouseEventListener l : mouseEventListeners) {
			l.mouseReleased(x, y);
		}
		removeFromListenerList();
	}
	public static void mouseMoved(float x, float y, float dX, float dY) {
		addToListenerList();
		for (IMouseEventListener l : mouseEventListeners) {
			l.mouseMoved(x, y, dX, dY);
		}
		removeFromListenerList();
	}
	public static void mouseDragged(float x, float y, float dX, float dY) {
		addToListenerList();
		for (IMouseEventListener l : mouseEventListeners) {
			l.mouseDragged(x, y, dX, dY);
		}
		removeFromListenerList();
	}
	
}
