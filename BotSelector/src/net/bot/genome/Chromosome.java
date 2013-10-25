package net.bot.genome;

public class Chromosome {
	
	private Allele[] mAlleleList = new Allele[Genome.Gender.values().length];
	private int allelesDefined = 0;
	
	private ChromosomeType mType;
	
	public enum ChromosomeType {
		X,
		Y
	}

	public Chromosome(ChromosomeType type) {
		mType = type;
	}
	
	public ChromosomeType getType() {
		return mType;
	}
	
	public Allele[] getAlleleList() {
		return mAlleleList;
	}
	
	public void setAlleleList(Allele[] list) {
		mAlleleList = list;
	}
	
}
