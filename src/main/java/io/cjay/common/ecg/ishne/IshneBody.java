package io.cjay.common.ecg.ishne;

import io.cjay.common.ecg.ishne.utils.NumberUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class IshneBody {
	private List<Short> ecgData;

	public List<Short> getEcgData() {
		return ecgData;
	}

	public void setEcgData(List<Short> ecgData) {
		this.ecgData = ecgData;
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		List<Short> ecgData = getEcgData();
		for (Short ecgDatum : ecgData) {
			dataOutputStream.writeShort(NumberUtils.littleEndianShort(ecgDatum));
		}
		dataOutputStream.flush();
		dataOutputStream.close();
		return byteArrayOutputStream.toByteArray();
	}
}
