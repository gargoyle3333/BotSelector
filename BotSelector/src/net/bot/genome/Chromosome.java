package net.bot.genome;

public class Chromosome implements Cloneable {
	
	private Allele[] mAlleleList = new Allele[ChromosomeType.values().length];
	
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
	
	public Chromosome clone() {
		Chromosome c = new Chromosome(mType);
		Allele[] list = new Allele[ChromosomeType.values().length];
		for (int i = 0; i < ChromosomeType.values().length; i++) {
			list[i] = new Allele(mAlleleList[i].getType(), mAlleleList[i].getValue());
		}
		c.setAlleleList(list);
		return c;
	}
	
}
