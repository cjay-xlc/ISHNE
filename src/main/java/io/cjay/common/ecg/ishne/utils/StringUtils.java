package io.cjay.common.ecg.ishne.utils;

public class StringUtils {
	public static boolean containsNonAscii(String str){
		if(null == str){
			return false;
		}
		for(int i = 0; i < str.length(); ++i){
			char ch = str.charAt(i);
			if((short)ch < Byte.MIN_VALUE || (short)ch > Byte.MAX_VALUE){
				return true;
			}
		}
		return false;
	}

	public static String zeroPaddingBack(String str, int targetLength) {
		if(null == str){
			str = "";
		}
		StringBuilder stringBuilder = new StringBuilder(str);
		while(stringBuilder.length() < targetLength){
			stringBuilder.append((char)0);
		}
		return stringBuilder.toString();
	}

}
