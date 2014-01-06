package net.bot.disease;

import net.bot.util.RandomUtil;

public class Disease implements Cloneable{
	
	private static long ID = 0L;
	private long diseaseID; // Cloned diseases will have the same ID - used to make sure bots cannot have more than one of the same disease.
	private final static int FATALITYNUMBER = 1000;
	private boolean contagious;
	private double probabilityOfSpread;
	private int fatalityLevel;
	private double probabilityOfIncrease;
	
	public Disease() {
		diseaseID = ID;
		ID++;
		contagious = RandomUtil.rand.nextBoolean();
		probabilityOfSpread = RandomUtil.rand.nextDouble();
		fatalityLevel = 0; //current level of seriousness. If it reaches FATALITYNUMBER, the bot should be terminated
		probabilityOfIncrease = RandomUtil.rand.nextDouble();
	}
	
	public Disease(boolean contagious, double probabilityOfSpread, int fatalityLevel, double probabilityOfIncrease) {
		diseaseID = ID;
		ID++;
		this.contagious = contagious;
		this.probabilityOfSpread = probabilityOfSpread;
		this.fatalityLevel = fatalityLevel;
		this.probabilityOfIncrease = probabilityOfIncrease;
	}
	
	// For use in the clone method (creates a new Disease without incrementing ID)
	private Disease(long diseaseID, boolean contagious, double probabilityOfSpread, int fatalityLevel, double probabilityOfIncrease) {
		this.diseaseID = diseaseID;
		this.contagious = contagious;
		this.probabilityOfSpread = probabilityOfSpread;
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
	
	public boolean willSpread() {
		return ((RandomUtil.rand.nextDouble() < this.probabilityOfSpread) && this.contagious);
	}
	
	public long diseaseID() {
		return this.diseaseID;
	}
	
	@Override
	public Disease clone() {
		return new Disease(this.diseaseID,this.contagious,this.probabilityOfSpread,this.fatalityLevel,this.probabilityOfIncrease);
	}
}
