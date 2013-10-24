package net.bot.genome;

import net.bot.util.RandomUtil;

public class Genome {
	
	public Gender gender;
	public Chromosome chromosomeA, chromosomeB;
	
	public double maxSpeed, maxAcc, diseaseResistance;
	
	public enum Gender {
		MALE,
		FEMALE
	}
	
	public Genome(Chromosome A, Chromosome B) {
		gender = A.getType() != B.getType() ? Gender.MALE : Gender.FEMALE;
		chromosomeA = A;
		chromosomeB = B;
		
		calculateAttributes();
		
	}
	
	public void mutate() {
		Chromosome ch;
		if (RandomUtil.rand.nextBoolean()) {
			ch = chromosomeA;
		} else {
			ch = chromosomeB;
		}
		ch.getAlleleList()[RandomUtil.rand.nextInt(ch.getAlleleList().length)].setValue(RandomUtil.rand.nextDouble());
		calculateAttributes();
	}
	
	private void calculateAttributes() {
		Allele[] listA = chromosomeA.getAlleleList(), listB = chromosomeB.getAlleleList();
		for (int i = 0; i < listA.length; i++) {
			
			if (listA[i].getType() != listB[i].getType()) {
				// TODO fail nicely. Shouldn't reach here, though.
				return;
			}
			
			switch (listA[i].getType()) {
			case MAX_SPEED:
				double speedA = (double)listA[i].getValue(), speedB = (double)listB[i].getValue();
				maxSpeed = speedA > speedB ? speedA : speedB;
				break;
			case MAX_ACC:
				double accA = (double)listA[i].getValue(), accB = (double)listB[i].getValue();
				maxAcc = accA > accB ? accA : accB;
				break;
			case DISEASE_RESISTANCE:
				double diseaseA = (double)listA[i].getValue(), diseaseB = (double)listB[i].getValue();
				diseaseResistance = diseaseA > diseaseB ? diseaseA : diseaseB;
				break;
			default:
				// ??
				System.out.println("Undefined allele type in Genome");
			}
			
		}
	}
	
	// This class to work out all the speed etc.?
	// Add method to get a chromosome for mating

}
