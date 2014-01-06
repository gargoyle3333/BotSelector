package net.bot.entities;

public abstract class AbstractEntityBot extends Entity{
	public abstract void update(double delta);
	public abstract void draw();
	public abstract void consume(Entity food);
	public abstract void addForce(Entity entity);
	public abstract boolean isDiseased();
	public abstract void resolveContagiousDiseases(AbstractEntityBot bot);
	// To resolve whether or not the AbstractEntityBot will pass on any diseases to the current bot
}
