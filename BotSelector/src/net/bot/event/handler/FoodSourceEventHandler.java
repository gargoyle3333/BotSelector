package net.bot.event.handler;

import java.util.ArrayList;
import java.util.List;

import net.bot.event.listener.IFoodSourceEventListener;
import net.bot.food.FoodSource;

public class FoodSourceEventHandler {
	
	private static List<IFoodSourceEventListener> foodSourceEventListeners = new ArrayList<IFoodSourceEventListener>();
	
	public static void addListener(IFoodSourceEventListener listener) {
		foodSourceEventListeners.add(listener);
	}
	
	public static void removeListener(IFoodSourceEventListener listener) {
		foodSourceEventListeners.remove(foodSourceEventListeners);
	}
	
	public static void foodSourceCreated(FoodSource source) {
		for (IFoodSourceEventListener l : foodSourceEventListeners) {
			l.onFoodSourceCreated(source);
		}
	}

	public static void foodSourceDestroyed(FoodSource source) {
		for (IFoodSourceEventListener l : foodSourceEventListeners) {
			l.onFoodSourceDestroyed(source);
		}
	}
	
}
