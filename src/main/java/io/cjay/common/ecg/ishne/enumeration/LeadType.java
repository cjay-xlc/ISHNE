package io.cjay.common.ecg.ishne.enumeration;

public enum LeadType {
	NONE(-9),
	UNKNOWN(0),
	GENERIC_BIPOLAR(1),
	X_BIPOLAR(2),
	Y_BIPOLAR(3),
	Z_BIPOLAR(4),
	I(5),
	II(6),
	III(7),
	VR(8),
	VL(9),
	VF(10),
	V1(11),
	V2(12),
	V3(13),
	V4(14),
	V5(15),
	V6(16),
	ES(17),
	AS(18),
	AI(19);

	private int code;

	LeadType(int code){
		this.code = code;
	}

	public short getCode() {
		return (short)code;
	}
}
