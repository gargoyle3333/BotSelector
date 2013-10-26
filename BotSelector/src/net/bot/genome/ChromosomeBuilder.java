package net.bot.genome;

import net.bot.genome.Allele.Type;
import net.bot.genome.Chromosome.ChromosomeType;
import net.bot.util.RandomUtil;

public class ChromosomeBuilder {
	
	private static final double MAX_SPEED_MINIMUM = 0.0001F, MAX_SPEED_MAXIMUM = 0.0010F;
	// TODO find better values for these
	private static final double MAX_ACC_MINIMUM = 0.0002F, MAX_ACC_MAXIMUM = 0.0005F;
	private static final double DISEASE_RESISTANCE_MINIMUM = 0.0002F, DISEASE_RESISTANCE_MAXIMUM = 0.0005F;
	
	public Allele[] alleleList = new Allele[Type.values().length];
	public ChromosomeType chromosomeType;
	
	public ChromosomeBuilder() {
		// Do nothing
	}
	
	public Chromosome build() {
		for (Allele al : alleleList) {
			if (al == null) {
				return null;
			}
		}
		if (chromosomeType == null) {
			return null;
		}
		Chromosome ch = new Chromosome(chromosomeType);
		ch.setAlleleList(alleleList);
		return ch;
	}
	
	public void setType(ChromosomeType type) {
		chromosomeType = type;
	}
	
	public void setMaxSpeed(double speed) {
		alleleList[Allele.Type.MAX_SPEED.ordinal()] = new Allele(Allele.Type.MAX_SPEED, speed);
	}
	
	public void setMaxAcc(double acc) {
		alleleList[Allele.Type.MAX_ACC.ordinal()] = new Allele(Allele.Type.MAX_ACC, acc);
	}
	
	public void setDiseaseResistance(double resistance) {
		alleleList[Allele.Type.DISEASE_RESISTANCE.ordinal()] = new Allele(Allele.Type.DISEASE_RESISTANCE, resistance);
	}
	
	public void setAttribute(Allele.Type type, Object value) {
		alleleList[type.ordinal()] = new Allele(type, value);
	}
	
	public static Chromosome generateRandomChromosome() {
		
		Type[] values = Type.values();
		Allele[] chromosome = new Allele[values.length];
		
		for (Type t : values) {
			chromosome[t.ordinal()] = generateRandomOfType(t);
		}
		
		Chromosome.ChromosomeType type = RandomUtil.rand.nextBoolean() ? ChromosomeType.X : ChromosomeType.Y;
		Chromosome ch = new Chromosome(type);
		ch.setAlleleList(chromosome);
		return ch;
		
	}

	private static Allele generateRandomOfType(Type t) {
		switch (t) {
		case MAX_SPEED:
			return generateRandomMaxSpeed();
		case MAX_ACC:
			return generateRandomMaxAcc();
		case DISEASE_RESISTANCE:
			return generateRandomDiseaseResistance();
		default:
			System.err.println("Unknown allele type in ChromosomeBuilder");
		}
		return null;
	}
	
	private static Allele generateRandomMaxSpeed() {
		return new Allele(Type.MAX_SPEED, 
				RandomUtil.rand.nextFloat() *
				(MAX_SPEED_MAXIMUM - MAX_SPEED_MINIMUM) +
				MAX_SPEED_MINIMUM);
	}
	
	private static Allele generateRandomMaxAcc() {
		return new Allele(Type.MAX_ACC, 
				RandomUtil.rand.nextDouble() *
				(MAX_ACC_MAXIMUM - MAX_ACC_MINIMUM) +
				MAX_ACC_MINIMUM);
	}
	
	private static Allele generateRandomDiseaseResistance() {
		return new Allele(Type.DISEASE_RESISTANCE, 
				RandomUtil.rand.nextDouble() *
				(DISEASE_RESISTANCE_MAXIMUM - DISEASE_RESISTANCE_MINIMUM) +
				DISEASE_RESISTANCE_MINIMUM);
	}

}
