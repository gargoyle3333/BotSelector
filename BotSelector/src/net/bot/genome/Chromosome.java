package net.bot.genome;

import java.io.Serializable;

public class Chromosome implements Cloneable, Serializable {
	
	/**
	 * Generated Serial Version, to be updated whenever this class is
	 */
	private static final long serialVersionUID = -1309006321170290973L;

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
		Allele[] list = new Allele[Allele.Type.values().length];
		for (int i = 0; i < Allele.Type.values().length; i++) {
			list[i] = new Allele(mAlleleList[i].getType(), mAlleleList[i].getValue());
		}
		c.setAlleleList(list);
		return c;
	}
	
}
