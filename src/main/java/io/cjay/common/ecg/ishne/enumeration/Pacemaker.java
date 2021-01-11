package io.cjay.common.ecg.ishne.enumeration;

public enum Pacemaker {
	NONE(0),
	UNKNOWN(1),
	SINGLE_CHAMBER_UNIPOLAR(2),
	DUAL_CHAMBER_UNIPOLAR(3),
	SINGLE_CHAMBER_BIPOLAR(4),
	DUAL_CHAMBER_BIPOLAR(5);

	private int code;

	Pacemaker(int code){
		this.code = code;
	}

	public short getCode() {
		return (short)code;
	}
}
