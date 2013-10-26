package net.bot.genome;

import net.bot.util.RandomUtil;

import org.lwjgl.util.Color;

public class Genome implements Cloneable {
	
	public Gender mGender;
	public Chromosome chromosomeA, chromosomeB;
	
	private double mMaxSpeed, mMaxAcc, mDiseaseResistance;
	private Color mColor;
	
	public Gender getGender() {
		return mGender;
	}

	public void setGender(Gender gender) {
		mGender = gender;
	}

	public double getMaxSpeed() {
		return mMaxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		mMaxSpeed = maxSpeed;
	}

	public double getMaxAcc() {
		return mMaxAcc;
	}

	public void setMaxAcc(double maxAcc) {
		mMaxAcc = maxAcc;
	}

	public double getDiseaseResistance() {
		return mDiseaseResistance;
	}

	public void setDiseaseResistance(double diseaseResistance) {
		mDiseaseResistance = diseaseResistance;
	}
	
	public Color getColor() {
		return mColor;
	}
	
	public void setColor(Color color) {
		mColor = color;
	}
	
	public enum Gender {
		MALE,
		FEMALE
	}
	
	public Genome(Chromosome A, Chromosome B) {
		mGender = A.getType() != B.getType() ? Gender.MALE : Gender.FEMALE;
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
		// TODO change this so it actually works for non-double values
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
				mMaxSpeed = Math.max(speedA, speedB);
				break;
			case MAX_ACC:
				double accA = (double)listA[i].getValue(), accB = (double)listB[i].getValue();
				mMaxAcc = Math.max(accA, accB);
				break;
			case DISEASE_RESISTANCE:
				double diseaseA = (double)listA[i].getValue(), diseaseB = (double)listB[i].getValue();
				mDiseaseResistance = Math.max(diseaseA, diseaseB);
				break;
			case COLOR:
				mColor = (Color) listA[i].getValue();
				break;
			default:
				// ??
				System.out.println("Undefined allele type in Genome");
			}
			
		}
	}
	
	public Chromosome getRandomChromosome() {
		return RandomUtil.rand.nextBoolean() ? chromosomeA.clone() : chromosomeB.clone();
	}
	
	public Genome clone() {
		return new Genome(chromosomeA.clone(), chromosomeB.clone());
	}
	
}
