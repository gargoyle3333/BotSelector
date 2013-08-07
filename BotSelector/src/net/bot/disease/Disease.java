package net.bot.disease;

import net.bot.util.RandomUtil;

public class Disease {
	
	private final static int FATALITYNUMBER = 1500;
	private boolean contagious;
	private int fatalityLevel;
	private double probabilityOfIncrease;
	
	public Disease() {
		contagious = RandomUtil.rand.nextBoolean();
		fatalityLevel = 0; //current level of seriousness. If it reaches FATALITYNUMBER, the bot should be terminated
		probabilityOfIncrease = RandomUtil.rand.nextDouble();
	}
	
	public Disease(boolean contagious, int fatalityLevel, double probabilityOfIncrease) {
		this.contagious = contagious;
		this.fatalityLevel = fatalityLevel;
		this.probabilityOfIncrease = probabilityOfIncrease;
	}
	
	public void update() {
		if (RandomUtil.rand.nextDouble() < probabilityOfIncrease) {
			fatalityLevel++;
		}
	}
	
	public boolean checkFatality() {
		update();
		return (fatalityLevel >= FATALITYNUMBER);
	}
	
	public boolean isContagious() {
		return contagious;
	}
}
