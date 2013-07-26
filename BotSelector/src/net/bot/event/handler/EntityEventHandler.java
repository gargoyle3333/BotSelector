package net.bot.event.handler;

import java.util.ArrayList;
import java.util.List;

import net.bot.entities.EntityBot;
import net.bot.entities.EntityFoodSpeck;
import net.bot.event.listener.IEntityEventListener;

public class EntityEventHandler {
	
	private static List<IEntityEventListener> entityEventListeners = new ArrayList<IEntityEventListener>();
	private static List<IEntityEventListener> removeListeners = new ArrayList<IEntityEventListener>();
	private static List<IEntityEventListener> addListeners = new ArrayList<IEntityEventListener>();

	
	public static void addListener(IEntityEventListener listener) {
		addListeners.add(listener);
	}
	
	public static void removeListener(IEntityEventListener listener) {
		removeListeners.add(listener);
	}

	public static void botCreated(EntityBot bot) {
		addToListenerList();
		for (IEntityEventListener l : entityEventListeners) {
			l.onBotCreated(bot);
		}
		removeFromListenerList();
	}
	public static void botDestroyed(EntityBot bot) {
		addToListenerList();
		for (IEntityEventListener l : entityEventListeners) {
			l.onBotDestroyed(bot);
		}
		removeFromListenerList();
	}
	public static void foodCreated(EntityFoodSpeck speck) {
		addToListenerList();
		for (IEntityEventListener l : entityEventListeners) {
			l.onFoodCreated(speck);
		}
		removeFromListenerList();
	}
	public static void foodDestroyed() {
		addToListenerList();
		for (IEntityEventListener l : entityEventListeners) {
			l.onFoodDestroyed();
		}
		removeFromListenerList();
	}
	private static void removeFromListenerList() {
		for (IEntityEventListener l : removeListeners) {
			entityEventListeners.remove(l);
		}
		removeListeners.clear();
	}
	private static void addToListenerList() {
		for (IEntityEventListener l : addListeners) {
			entityEventListeners.add(l);
		}
		addListeners.clear();
	}
}
