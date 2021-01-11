package io.cjay.common.ecg.ishne;

import io.cjay.common.ecg.ishne.enumeration.Pacemaker;
import lombok.Data;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

@Data
public class IshneHeader {

	private static final int DEFAULT_UNFIXED_LENGTH_BLOCK_OFFSET = 522;
	private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT");
	private TimeZone timeZone;

	/* 文件版本 */
	private short fileVersion;

	private Subject subject;

	/* 长度为3，顺序：日、月、年 */
	private long fileCreateMills;

	/* ECG数据的大小，以ECG值的个数为单位 */
	private int sizeOfEcgData;

	private long ecgStartMillis;

	/* 采样率 */
	private short sampleRate;

	/* 导联 */
	private List<Lead> leads;

	/* 起搏器 */
	private Pacemaker pacemaker;

	/* 长度为40，记录仪类型，如analog、digital*/
	private String recorderType;

	/* 长度为80，Optional */
	private String proprietary;

	/* 长度为80，Optional */
	private String copyright;

	/* 长度为88，Optional */
	private String reserved;

	/* 该文件格式只支持ASCII，一般用于存储注释*/
	private String unfixedLengthBlockData;

	public int getSizeOfUnfixedLengthBlock(){
		return null == getUnfixedLengthBlockData() ? 0 : getUnfixedLengthBlockData().getBytes().length;
	}

	public int getOffsetOfUnfixedLengthBlock() {
		return DEFAULT_UNFIXED_LENGTH_BLOCK_OFFSET;
	}

	public int getOffsetOfEcgData(){
		return getOffsetOfUnfixedLengthBlock() + getSizeOfUnfixedLengthBlock();
	}

	private ZonedDateTime getSubjectBirthDateTime() {
		TimeZone timeZone = getTimeZone();
		if(timeZone == null) {
			timeZone = DEFAULT_TIMEZONE;
		}
		return Instant.ofEpochMilli(subject == null ? 0 : subject.getBirth()).atZone(timeZone.toZoneId());
	}

	private ZonedDateTime getEcgRecordStartDateTime() {
		TimeZone timeZone = getTimeZone();
		if(timeZone == null) {
			timeZone = DEFAULT_TIMEZONE;
		}
		return Instant.ofEpochMilli(getEcgStartMillis()).atZone(timeZone.toZoneId());
	}


	private ZonedDateTime getFileCreateDateTime() {
		TimeZone timeZone = getTimeZone();
		if(timeZone == null) {
			timeZone = DEFAULT_TIMEZONE;
		}
		return Instant.ofEpochMilli(getFileCreateMills()).atZone(timeZone.toZoneId());
	}

	public byte[] getBytes() {
		IshneHeaderByteBuffer ishneHeaderByteBuffer = new IshneHeaderByteBuffer();
		ishneHeaderByteBuffer.putFileVersion(getFileVersion());
		ishneHeaderByteBuffer.putSubjectFirstName(subject == null ? null : subject.getFirstName());
		ishneHeaderByteBuffer.putSubjectLastName(subject == null ? null : subject.getLastName());
		ishneHeaderByteBuffer.putSubjectID(subject == null ? null : subject.getId());
		ishneHeaderByteBuffer.putSubjectSex(subject == null ? null : subject.getSex());
		ishneHeaderByteBuffer.putSubjectRace(subject == null ? null : subject.getRace());
		ishneHeaderByteBuffer.putSubjectBirthDateTime(getSubjectBirthDateTime());
		ishneHeaderByteBuffer.putEcgRecordStartDateTime(getEcgRecordStartDateTime());
		ishneHeaderByteBuffer.putFileCreateDateTime(getFileCreateDateTime());
		ishneHeaderByteBuffer.putLeads(getLeads());
		ishneHeaderByteBuffer.putPacemaker(getPacemaker());
		ishneHeaderByteBuffer.putRecorderType(getRecorderType());
		ishneHeaderByteBuffer.putSampleRate(getSampleRate());
		ishneHeaderByteBuffer.putProprietary(getProprietary());
		ishneHeaderByteBuffer.putCopyright(getCopyright());
		ishneHeaderByteBuffer.putReserved(getReserved());
		ishneHeaderByteBuffer.putOffsetOfEcgData(getOffsetOfEcgData());
		ishneHeaderByteBuffer.putSizeOfEcgData(getSizeOfEcgData());
		ishneHeaderByteBuffer.putOffsetOfUnfixedLengthBlock(getOffsetOfUnfixedLengthBlock());
		ishneHeaderByteBuffer.putUnfixedLengthBlockData(getUnfixedLengthBlockData());
		return ishneHeaderByteBuffer.toByteArray();
	}
}
