package net.bot.event.listener;

import net.bot.entities.AbstractEntityBot;
import net.bot.entities.EntityFoodSpeck;

public interface IEntityEventListener {

	void onBotCreated(AbstractEntityBot bot);
	void onBotDestroyed(AbstractEntityBot bot);
	
	void onFoodCreated(EntityFoodSpeck speck);
	void onFoodDestroyed(EntityFoodSpeck speck);
	
}
