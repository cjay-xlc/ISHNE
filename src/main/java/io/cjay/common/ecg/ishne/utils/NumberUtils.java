package io.cjay.common.ecg.ishne.utils;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NumberUtils {

	public static short littleEndianShort(short value) {
		byte[] bytes = shortToByteArray(value);
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}


	public static int littleEndianInt(int value) {
		byte[] bytes = intToByteArray(value);
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}


	/**
	 * 大端存储：从高位到低位
	 * */
	public static byte[] intToByteArray(int value){
		byte[] bytes = new byte[Integer.SIZE / 8];
		for(int i = 0; i < bytes.length; ++i){
			bytes[i] = (byte)(value >>> ((bytes.length - i - 1) * 8) & 0xFF);
		}
		return bytes;
	}

	/**
	 * 大端存储：从高位到低位
	 * */
	public static byte[] shortToByteArray(short value){
		byte[] bytes = new byte[Short.SIZE / 8];
		for(int i = 0; i < bytes.length; ++i){
			bytes[i] = (byte)(value >>> ((bytes.length - i - 1) * 8) & 0xFF);
		}
		return bytes;
	}
}
