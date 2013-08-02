package net.bot.event.handler;

import java.util.ArrayList;
import java.util.List;

import net.bot.event.listener.IDisplayEventListener;

public class DisplayEventHandler {

	private static List<IDisplayEventListener> displayEventListeners = new ArrayList<IDisplayEventListener>();
	private static List<IDisplayEventListener> removeListeners = new ArrayList<IDisplayEventListener>();
	private static List<IDisplayEventListener> addListeners = new ArrayList<IDisplayEventListener>();

	public static void addListener(IDisplayEventListener listener) {
		addListeners.add(listener);
	}
	
	public static void removeListener(IDisplayEventListener listener) {
		removeListeners.add(listener);
	}
	
	public static void clearListeners() {
		removeListeners.addAll(displayEventListeners);
	}
	
	public static void update(double delta) {
		addToListenerList();
		for (IDisplayEventListener l : displayEventListeners) {
			l.onUpdate(delta);
		}
		removeFromListenerList();
	}
	
	private static void addToListenerList() {
		for (IDisplayEventListener l : addListeners) {
			displayEventListeners.add(l);
		}
		addListeners.clear();
	}
	
	private static void removeFromListenerList() {
		for (IDisplayEventListener l : removeListeners) {
			displayEventListeners.remove(l);
		}
		removeListeners.clear();
	}
	
}
