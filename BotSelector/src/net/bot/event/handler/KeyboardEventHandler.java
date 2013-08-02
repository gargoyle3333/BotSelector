package net.bot.event.handler;

import java.util.ArrayList;
import java.util.List;

import net.bot.event.listener.IKeyboardEventListener;

public class KeyboardEventHandler {
	
	private static List<IKeyboardEventListener> keyboardEventListeners = new ArrayList<IKeyboardEventListener>();
	private static List<IKeyboardEventListener> removeListeners = new ArrayList<IKeyboardEventListener>();
	private static List<IKeyboardEventListener> addListeners = new ArrayList<IKeyboardEventListener>();

	
	public static void addListener(IKeyboardEventListener listener) {
		addListeners.add(listener);
	}
	
	public static void removeListener(IKeyboardEventListener listener) {
		removeListeners.add(listener);
	}

	public static void clearListeners() {
		removeListeners.addAll(keyboardEventListeners);
	}
	
	public static void keyPressed(int key) {
		addToListenerList();
		for (IKeyboardEventListener l : keyboardEventListeners) {
			l.onKeyPressed(key);
		}
		removeFromListenerList();
	}
	
	public static void keyReleased(int key) {
		addToListenerList();
		for (IKeyboardEventListener l : keyboardEventListeners) {
			l.onKeyReleased(key);
		}
		removeFromListenerList();
	}
	
	private static void addToListenerList() {
		for (IKeyboardEventListener l : addListeners) {
			keyboardEventListeners.add(l);
		}
		addListeners.clear();
	}
	
	private static void removeFromListenerList() {
		for (IKeyboardEventListener l : removeListeners) {
			keyboardEventListeners.remove(l);
		}
		removeListeners.clear();
	}
	
}
