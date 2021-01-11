package io.cjay.common.ecg.ishne.enumeration;

public enum Sex {

	UNKNOWN(0),
	MALE(1),
	FEMALE(2);


	private int code;

	Sex(int code){
		this.code = code;
	}

	public short getCode() {
		return (short)code;
	}
}
