package net.bot.event.listener;

import net.bot.food.FoodSource;

public interface IFoodSourceEventListener {

	void onFoodSourceCreated(FoodSource source);
	void onFoodSourceDestroyed(FoodSource source);
	
	
}
