package net.mike.bot.event;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalEventHandler {

	private static HashMap<Integer, ArrayList<IEventHandler>> handlerArrays;
	
	protected GlobalEventHandler() {}
	
	static {
		handlerArrays = new HashMap<Integer, ArrayList<IEventHandler>>();
		
		for (Event e : Event.values()) {
			handlerArrays.put(e.ordinal(), new ArrayList<IEventHandler>());
		}
	}
	
	public static void subscribeEvent(IEventHandler handler, Event event) {
		handlerArrays.get(event.ordinal()).add(handler);
	}
	
	public static void unsubscribeEvent(IEventHandler handler, Event event) {
		handlerArrays.get(event.ordinal()).remove(handler);
	}
	
	public static void fireEvent(Event event, Object eventInfo) {
		for (IEventHandler handler : handlerArrays.get(event.ordinal())) {
			handler.handleEvent(event, eventInfo);
		}
	}
	
}
