package net.bot.genome;

import java.io.Serializable;

public class Allele implements Serializable {

	private Type mType;
	private Object mValue; // Very general, this could be used for anything

	public enum Type {
		MAX_SPEED, 
		MAX_ACC, 
		DISEASE_RESISTANCE,
		COLOR,
		// TODO add a few more...
		// TODO every value added must be added to the builder AND the genome decoder
	}

	public Allele(Type type, Object value) {
		mType = type;
		mValue = value;
	}

	public Type getType() {
		return mType;
	}

	public void setType(Type type) {
		mType = type;
	}

	public Object getValue() {
		return mValue;
	}

	public void setValue(Object value) {
		mValue = value;
	}

}
