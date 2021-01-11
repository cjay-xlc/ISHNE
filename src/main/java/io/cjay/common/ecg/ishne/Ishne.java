package io.cjay.common.ecg.ishne;

import io.cjay.common.ecg.ishne.utils.CrcCcitt;
import io.cjay.common.ecg.ishne.utils.NumberUtils;

import java.io.*;

public class Ishne {
	private static final String MAGIC_NUMBER = "ISHNE1.0";

	private String path;

	private IshneHeader header;

	private IshneBody body;

	public static String getMagicNumber() {
		return MAGIC_NUMBER;
	}

	public short getHeaderCrc() {
		if(header == null){
			throw new NullPointerException("null header assigned");
		}
		return (short)(CrcCcitt.calculate(header.getBytes()));
	}

	public IshneHeader getHeader() {
		return header;
	}

	public void setHeader(IshneHeader header) {
		this.header = header;
	}

	public IshneBody getBody() {
		return body;
	}

	public void setBody(IshneBody body) {
		this.body = body;
	}

	private byte[] getBytes() throws IOException {
		if(header == null || body == null){
			throw new NullPointerException("no header or body assigned");
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		dataOutputStream.writeBytes(getMagicNumber());
		dataOutputStream.writeShort(NumberUtils.littleEndianShort(getHeaderCrc()));
		dataOutputStream.write(header.getBytes());
		dataOutputStream.write(body.getBytes());
		dataOutputStream.flush();
		dataOutputStream.close();
		return byteArrayOutputStream.toByteArray();
	}

	public static Ishne read(String path){
		// TODO read from existing ISHNE file
		// process in 2 steps:
		// 1. read data
		// 2. save path: this.path = path;
		throw new UnsupportedOperationException();
	}

	public void save() throws IOException {
		if(this.path != null){
			save(this.path);
		}else{
			throw new NullPointerException("null save path");
		}
	}

	public void save(String path) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(path);
		BufferedOutputStream  bufferedOut = new BufferedOutputStream(fileOutputStream);
		bufferedOut.write(getBytes());
		bufferedOut.flush();
		bufferedOut.close();
	}
}
