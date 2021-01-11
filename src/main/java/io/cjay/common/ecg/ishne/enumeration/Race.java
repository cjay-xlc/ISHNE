package io.cjay.common.ecg.ishne.enumeration;

public enum Race {
	/* 4-9: Reserved */
	UNKNOWN(0),
	CAUCASIAN(1),
	BLACK(2),
	ORIENTAL(3);
	private int code;

	Race(int code){
		this.code = code;
	}

	public short getCode() {
		return (short)code;
	}
}
