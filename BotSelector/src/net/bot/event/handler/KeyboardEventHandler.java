package net.bot.event.handler;

import java.util.ArrayList;
import java.util.List;

import net.bot.event.listener.IKeyboardEventListener;

public class KeyboardEventHandler {
	
	private static List<IKeyboardEventListener> keyboardEventListeners = new ArrayList<IKeyboardEventListener>();
	
	public static void addListener(IKeyboardEventListener listener) {
		keyboardEventListeners.add(listener);
	}
	
	public static void removeListener(IKeyboardEventListener listener) {
		keyboardEventListeners.remove(keyboardEventListeners);
	}

	public static void keyPressed(int key) {
		for (IKeyboardEventListener l : keyboardEventListeners) {
			l.onKeyPressed(key);
		}
	}
	
	public static void keyReleased(int key) {
		for (IKeyboardEventListener l : keyboardEventListeners) {
			l.onKeyReleased(key);
		}
	}
	
}
