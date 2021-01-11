package io.cjay.common.ecg.ishne;

import io.cjay.common.ecg.ishne.utils.NumberUtils;
import io.cjay.common.ecg.ishne.utils.StringUtils;
import io.cjay.common.ecg.ishne.enumeration.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class IshneHeaderByteBuffer {

	private byte[] bytesBeforeUnfixedLengthBlock;
	private int offsetOfUnfixedLengthBlock;
	private int offsetOfEcgData;
	private String unfixedLengthBlockData;

	IshneHeaderByteBuffer(){
		bytesBeforeUnfixedLengthBlock = new byte[512];
	}

	private void write(int value, int beginIndex){
		value = NumberUtils.littleEndianInt(value);
		bytesBeforeUnfixedLengthBlock[beginIndex] = (byte)(value >>> 24 & 255);
		bytesBeforeUnfixedLengthBlock[beginIndex + 1] = (byte)(value >>> 16 & 255);
		bytesBeforeUnfixedLengthBlock[beginIndex + 2] = (byte)(value >>> 8 & 255);
		bytesBeforeUnfixedLengthBlock[beginIndex + 3] = (byte)(value >>> 0 & 255);
	}

	private void write(short value, int beginIndex){
		value = NumberUtils.littleEndianShort(value);
		bytesBeforeUnfixedLengthBlock[beginIndex] = (byte) ((int)value >>> 8 & 255);
		bytesBeforeUnfixedLengthBlock[beginIndex + 1] = (byte) ((int)value >>> 0 & 255);
	}

	private void write(String value, int beginIndex){
		byte[] bytes = value.getBytes();
		for(int i = 0; i < bytes.length; ++i){
			bytesBeforeUnfixedLengthBlock[beginIndex + i] = bytes[i];
		}
	}

	public void putSizeOfEcgData(int sizeOfEcgData) {
		write(sizeOfEcgData, 4);
	}
	public void putOffsetOfUnfixedLengthBlock(int offsetOfUnfixedLengthBlock) {
		this.offsetOfUnfixedLengthBlock = offsetOfUnfixedLengthBlock;
		write(offsetOfUnfixedLengthBlock, 8);
	}
	public void putOffsetOfEcgData(int offsetOfEcgData) {
		this.offsetOfEcgData = offsetOfEcgData;
		write(offsetOfEcgData, 12);
	}
	public void putFileVersion(short fileVersion) {
		write(fileVersion, 16);
	}
	public void putSubjectFirstName(String subjectFirstName) throws IllegalArgumentException {
		if(null != subjectFirstName) {
			if (subjectFirstName.length() > 40) {
				throw new IllegalArgumentException("Length out of range! The range of length is [0,40].");
			}
			if(StringUtils.containsNonAscii(subjectFirstName)){
				throw new IllegalArgumentException("Contains non-ascii characters!");
			}
		}
		subjectFirstName = StringUtils.zeroPaddingBack(subjectFirstName, 40);
		write(subjectFirstName, 18);
	}
	public void putSubjectLastName(String subjectLastName) throws IllegalArgumentException {
		if(null != subjectLastName) {
			if (subjectLastName.length() > 40) {
				throw new IllegalArgumentException("Length out of range! The range of length is [0,40].");
			}
			if (StringUtils.containsNonAscii(subjectLastName)) {
				throw new IllegalArgumentException("Contains non-ascii characters!");
			}
		}
		subjectLastName = StringUtils.zeroPaddingBack(subjectLastName, 40);
		write(subjectLastName, 58);
	}
	public void putSubjectID(String subjectID) throws IllegalArgumentException {
		if(null != subjectID) {
			if (subjectID.length() > 20) {
				throw new IllegalArgumentException("Length out of range! The range of length is [0,20].");
			}
			if (StringUtils.containsNonAscii(subjectID)) {
				throw new IllegalArgumentException("Contains non-ascii characters!");
			}
		}
		subjectID = StringUtils.zeroPaddingBack(subjectID, 20);
		write(subjectID, 98);
	}
	public void putSubjectSex(Sex subjectSex){
		if(null == subjectSex){
			subjectSex = Sex.UNKNOWN;
		}
		write(subjectSex.getCode(), 118);
	}
	public void putSubjectRace(Race subjectRace){
		if(null == subjectRace){
			subjectRace = Race.UNKNOWN;
		}
		write(subjectRace.getCode(), 120);
	}
	public void putSubjectBirthDateTime(ZonedDateTime subjectBirthDateTime) {
		write((short)(subjectBirthDateTime.getDayOfMonth()),122);
		write((short)(subjectBirthDateTime.getMonth().getValue()), 124);
		write((short)(subjectBirthDateTime.getYear()), 126);
	}
	public void putEcgRecordStartDateTime(ZonedDateTime ecgRecordStartDateTime) {
		write((short)(ecgRecordStartDateTime.getDayOfMonth()), 128);
		write((short)(ecgRecordStartDateTime.getMonth().getValue()), 130);
		write((short)(ecgRecordStartDateTime.getYear()), 132);
		write((short)(ecgRecordStartDateTime.getHour()), 140);
		write((short)(ecgRecordStartDateTime.getMinute()),142);
		write((short)(ecgRecordStartDateTime.getSecond()),144);
	}
	public void putFileCreateDateTime(ZonedDateTime fileCreateDateTime) {
		write((short)(fileCreateDateTime.getDayOfMonth()), 134);
		write((short)(fileCreateDateTime.getMonth().getValue()), 136);
		write((short)(fileCreateDateTime.getYear()), 138);
	}

	private int getLeadsCount(List<Lead> leads){
		if(null == leads){
			return 0;
		}
		int count = leads.size();
		for(Lead lead: leads){
			if(LeadType.NONE == lead.getLeadType()){
				--count;
			}
		}
		return count;
	}
	/**
	 * @param leads 长度如果 < 12，则缺失部分自动补为Leads.NONE，如果 > 12 则截取前面12个，不允许有null元素
	 * @throws IllegalArgumentException
	 */
	private List<Lead> standardizedLeads(List<Lead> leads){
		if(leads == null){
			leads = new ArrayList<Lead>();
		}
		int length = leads.size();
		if(length >= 12){
			length = 12;
		}
		List<Lead> candidateLeads = new ArrayList<>(length);
		for(int i = 0; i < length; ++i){
			Lead lead = leads.get(i);
			if(null == lead){
				throw new IllegalArgumentException("Can not contain null pointer element in leads array.");
			}else{
				candidateLeads.add(lead);
			}
		}
		for(int i = length; i < 12; ++i){
			candidateLeads.add(new Lead(LeadType.NONE, LeadQuality.NONE, Lead.NONE_AMPLITUDE));
		}
		return candidateLeads;
	}

	public void putLeads(List<Lead> leads){
		leads = standardizedLeads(leads);
		write((short)(getLeadsCount(leads)), 146);
		int offset = 148;
		for (int i = 0; i < leads.size(); ++i) {
			write(leads.get(i).getLeadType().getCode(), offset + i * 2);
		}
		offset += leads.size() * 2;
		for (int i = 0; i < leads.size(); ++i) {
			write(leads.get(i).getLeadQuality().getCode(), offset + i * 2);
		}
		offset += leads.size() * 2;
		for (int i = 0; i < leads.size(); ++i) {
			write(leads.get(i).getAmplitude(), offset + i * 2);
		}
	}

	public void putPacemaker(Pacemaker pacemaker){
		if(null == pacemaker){
			pacemaker = Pacemaker.NONE;
		}
		write(pacemaker.getCode(), 220);
	}

	public void putRecorderType(String recorderType){
		if(null != recorderType) {
			if (recorderType.length() > 40) {
				throw new IllegalArgumentException("Length out of range! The range of length is [0,40].");
			}
			if (StringUtils.containsNonAscii(recorderType)) {
				throw new IllegalArgumentException("Contains non-ascii characters!");
			}
		}
		recorderType = StringUtils.zeroPaddingBack(recorderType, 40);
		write(recorderType, 222);
	}

	public void putSampleRate(short sampleRate){
		write(sampleRate, 262);
	}

	public void putProprietary(String proprietary){
		if(null != proprietary) {
			if (proprietary.length() > 80) {
				throw new IllegalArgumentException("Length out of range! The range of length is [0,80].");
			}
			if (StringUtils.containsNonAscii(proprietary)) {
				throw new IllegalArgumentException("Contains non-ascii characters!");
			}
		}
		proprietary = StringUtils.zeroPaddingBack(proprietary, 80);
		write(proprietary, 264);
	}

	public void putCopyright(String copyright){
		if(null != copyright) {
			if (copyright.length() > 80) {
				throw new IllegalArgumentException("Length out of range! The range of length is [0,80].");
			}
			if (StringUtils.containsNonAscii(copyright)) {
				throw new IllegalArgumentException("Contains non-ascii characters!");
			}
		}
		copyright = StringUtils.zeroPaddingBack(copyright, 80);
		write(copyright, 344);
	}

	public void putReserved(String reserved){
		if(null != reserved) {
			if (reserved.length() > 88) {
				throw new IllegalArgumentException("Length out of range! The range of length is [0,88].");
			}
			if (StringUtils.containsNonAscii(reserved)) {
				throw new IllegalArgumentException("Contains non-ascii characters!");
			}
		}
		reserved = StringUtils.zeroPaddingBack(reserved, 88);
		write(reserved, 424);
	}

	public void putUnfixedLengthBlockData(String unfixedLengthBlockData){
		if(null == unfixedLengthBlockData){
			write(0, 0);
			unfixedLengthBlockData = "";
		}else {
			if(StringUtils.containsNonAscii(unfixedLengthBlockData)){
				throw new IllegalArgumentException("Contains non-ascii characters!");
			}
			write(unfixedLengthBlockData.getBytes().length, 0);
		}
		this.unfixedLengthBlockData = unfixedLengthBlockData;
	}

	public byte[] toByteArray(){
		List<Byte> bytesList = new ArrayList<>();
		for(byte value: bytesBeforeUnfixedLengthBlock){
			bytesList.add(value);
		}
		for (int i = 0; i < offsetOfUnfixedLengthBlock - 522; ++i) {
			bytesList.add((byte)0);
		}
		byte[] unfixedLengthBlockBytes = unfixedLengthBlockData.getBytes();
		for(byte value: unfixedLengthBlockBytes){
			bytesList.add(value);
		}
		for (int i = 0; i < offsetOfEcgData - offsetOfUnfixedLengthBlock - unfixedLengthBlockBytes.length; ++i) {
			bytesList.add((byte)0);
		}
		byte[] bytes = new byte[bytesList.size()];
		for(int i = 0; i < bytes.length; ++i){
			bytes[i] = bytesList.get(i);
		}
		return bytes;
	}
}
