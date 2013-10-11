package net.bot.entities;

public abstract class AbstractEntityBot extends Entity{
	public abstract void update();
	public abstract void draw();
	public abstract void consume(Entity food);
	public abstract void addForce(Entity entity);
	public abstract boolean isDiseased();
}
