package net.bot.event.listener;

import net.bot.entities.EntityBot;
import net.bot.entities.EntityFoodSpeck;

public interface IEntityEventListener {

	void onBotCreated(EntityBot bot);
	void onBotDestroyed(EntityBot bot);
	
	void onFoodCreated(EntityFoodSpeck speck);
	void onFoodDestroyed(EntityFoodSpeck speck);
	
}
