package net.bot.event.handler;

import java.util.ArrayList;
import java.util.List;

import net.bot.entities.AbstractEntityBot;
import net.bot.entities.EntityFoodSpeck;
import net.bot.event.listener.IEntityEventListener;

public class EntityEventHandler {
	
	private static List<IEntityEventListener> entityEventListeners = new ArrayList<IEntityEventListener>();
	
	public static void addListener(IEntityEventListener listener) {
		entityEventListeners.add(listener);
	}
	
	public static void removeListener(IEntityEventListener listener) {
		entityEventListeners.remove(entityEventListeners);
	}

	public static void botCreated(AbstractEntityBot bot) {
		for (IEntityEventListener l : entityEventListeners) {
			l.onBotCreated(bot);
		}
	}
	public static void botDestroyed(AbstractEntityBot bot) {
		for (IEntityEventListener l : entityEventListeners) {
			l.onBotDestroyed(bot);
		}
	}
	public static void foodCreated(EntityFoodSpeck speck) {
		for (IEntityEventListener l : entityEventListeners) {
			l.onFoodCreated(speck);
		}
	}
	public static void foodDestroyed(EntityFoodSpeck speck) {
		for (IEntityEventListener l : entityEventListeners) {
			l.onFoodDestroyed(speck);
		}
	}
	
}
