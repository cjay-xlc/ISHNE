package io.cjay.common.ecg.ishne;

import io.cjay.common.ecg.ishne.enumeration.LeadQuality;
import io.cjay.common.ecg.ishne.enumeration.LeadType;

public class Lead {
	private LeadType leadType;
	private LeadQuality leadQuality;
	private short amplitude;
	public static final short NONE_AMPLITUDE = -9;

	public Lead(LeadType leadType, LeadQuality leadQuality, short amplitude){
		if(null == leadType || null == leadQuality){
			throw new NullPointerException("'leadType' or 'leadQuality' can not be null.");
		}
		if(LeadType.NONE == leadType){
			if(LeadQuality.NONE != leadQuality || amplitude != NONE_AMPLITUDE){
				throw new IllegalArgumentException("Ambiguous parameter values");
			}
		}else{
			if(LeadQuality.NONE == leadQuality || amplitude == NONE_AMPLITUDE){
				throw new IllegalArgumentException("Ambiguous parameter values");
			}
		}
		this.leadType = leadType;
		this.leadQuality = leadQuality;
		this.amplitude = amplitude;
	}

	public LeadType getLeadType() {
		return leadType;
	}


	public LeadQuality getLeadQuality() {
		return leadQuality;
	}


	public short getAmplitude() {
		return amplitude;
	}

}
