package net.bot.event.handler;

import java.util.ArrayList;
import java.util.List;

import net.bot.event.listener.IDisplayEventListener;

public class DisplayEventHandler {

	private static List<IDisplayEventListener> displayEventListeners = new ArrayList<IDisplayEventListener>();
	
	public static void addListener(IDisplayEventListener listener) {
		displayEventListeners.add(listener);
	}
	
	public static void removeListener(IDisplayEventListener listener) {
		displayEventListeners.remove(listener);
	}
	
	public static void update(double delta) {
		for (IDisplayEventListener l : displayEventListeners) {
			l.onUpdate(delta);
		}
	}
	
}
