package io.cjay.common.ecg.ishne.enumeration;

public enum LeadQuality {
	NONE(-9),
	UNKNOWN(0),
	GOOD(1),
	// < 10% of total length
	INTERMITTENT_NOISE(2),
	// > 10%
	FREQUENT_NOISE(3),
	// < 10%
	INTERMITTENT_DISCONNECTION(4),
	// > 10%
	FREQUENT_DISCONNECTION(5);

	private int code;

	LeadQuality(int code){
		this.code = code;
	}

	public short getCode() {
		return (short)code;
	}
}
