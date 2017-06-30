package com.androidpotato.mylibrary.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;

public class TypeConvert {
	public static String bytesToString(byte[] bytes) {
		String value=null;
		try {
			value = new String(bytes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return value;
	}


	public static int byteToInt(byte byteData) {
		return (byteData & 0xFF);
	}

	public static float byteToFloat(byte byteData) {
		return (byteData & 0xFF);
	}

	public static String bytesToHexString(byte[] b) {
		return bytesToHexString(b, false);
	}

	public static String bytesToHexString(byte[] b, boolean isDebug) {
		if (b==null) {
			return null;
		}
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			if (isDebug) {
				ret += hex.toUpperCase() + " ";
			} else {
				ret += hex.toUpperCase();
			}

		}
		return ret;
	}

	public static String bytesToHexString(byte[] b, boolean isDebug, int count) {
		String ret = "";
		for (int i = 0; i < count; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			if (isDebug) {
				ret += hex.toUpperCase() + " ";
			} else {
				ret += hex.toUpperCase();
			}

		}
		return ret;
	}

	public static String bytesToHexString(byte[] b, String divider, boolean isUpper) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			if (isUpper) {
				ret += hex.toUpperCase() + divider;
			} else {
				ret += hex.toLowerCase() + divider;
			}

		}
		return ret;
	}

	public static String byteToHexString(byte b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex.toUpperCase();
	}

	public static String byteToHexString(byte b, boolean isUpper) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		if (isUpper) {
			return hex.toUpperCase();
		} else {
			return hex.toLowerCase();
		}

	}

	public static String doubleToHexString(double value) {
		String hex = Integer.toHexString(Math.round(Math.round(value)));
		// String hex = Double.toHexString(value);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex.toUpperCase();
	}

	public static byte[] stringsToBytes(String[] dataStrings) {
		byte[] dataBytes = new byte[dataStrings.length];
		for (int i = 0; i < dataStrings.length; i++) {
			dataBytes[i] = Byte.parseByte(dataStrings[i]);
		}
		return dataBytes;
	}

	// public static long hexStringToLong(String hexString) {
	//
	// if (hexString == null || hexString.equals("")) {
	// throw new Exception();
	// }
	//
	// // hexString = hexString.toUpperCase();
	// // int length=hexString.length();
	// // char[] hexChars = hexString.toCharArray();
	// // for (int i = 0; i < length; i++) {
	// // "0123456789ABCDEF".indexOf(c)
	// // }
	//
	// // int length = hexString.length() / 2;
	// // char[] hexChars = hexString.toCharArray();
	// // byte[] d = new byte[length];
	// // for (int i = 0; i < length; i++) {
	// // int pos = i * 2;
	// // d[i] = (byte) (hexCharToByte(hexChars[pos]) << 4 |
	// hexCharToByte(hexChars[pos + 1]));
	// // }
	// // return d;
	// }

	public static byte[] hexStringToBytes(String hexString) {

		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (hexCharToByte(hexChars[pos]) << 4 | hexCharToByte(hexChars[pos + 1]));
		}
		return d;
	}
	public static long hexStringToLong(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return 0;
		}
		hexString = hexString.toUpperCase();
//		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
//		byte[] d = new byte[length];
		//005447904C
		//9876543210
		long value=0;
		for (int i =0; i<hexChars.length; i++) {
			value += Math.pow(16, hexChars.length - 1 - i)* hexCharToInt(hexChars[i]);
		}
//		for (int i = 0; i < length; i++) {
//			int pos = i * 2;
//			d[i] = (byte) (hexCharToByte(hexChars[pos]) << 4 | hexCharToByte(hexChars[pos + 1]));
//		}
		return value;
	}
	public static int[] hexStringToIntArray(String hexString, int byteCount) {

		if (hexString == null || hexString.equals("") || byteCount < 1 || byteCount > 4) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / byteCount;
		char[] hexChars = hexString.toCharArray();
		int[] arr = new int[length];
		for (int i = 0; i < length; i++) {
			int pos = i * byteCount;
			int item = 0;
			switch (byteCount) {
			case 1:
				item = (hexCharToInt(hexChars[pos]));
				break;
			case 2:
				item = ((hexCharToInt(hexChars[pos]) * 16) + (hexCharToInt(hexChars[pos + 1])));
				break;
			case 3:
				item = ((hexCharToInt(hexChars[pos]) * 16 * 16) + (hexCharToInt(hexChars[pos + 1]) * 16) + (hexCharToInt(hexChars[pos + 2])));
				break;
			case 4:
				item = ((hexCharToInt(hexChars[pos]) * 16 * 16 * 16) + (hexCharToInt(hexChars[pos + 1]) * 16 * 16) + (hexCharToInt(hexChars[pos + 2]) * 16) + (hexCharToInt(hexChars[pos + 3])));
				break;
			default:
				break;
			}

			arr[i] = item;
		}
		return arr;
	}

	public static int[] hexStringToIntArray(String hexString) {

		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 4;
		char[] hexChars = hexString.toCharArray();
		int[] arr = new int[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 4;
			int item = ((hexCharToInt(hexChars[pos]) * 16 * 16 * 16) + (hexCharToInt(hexChars[pos + 1]) * 16 * 16) + (hexCharToInt(hexChars[pos + 2]) * 16) + (hexCharToInt(hexChars[pos + 3])));
			arr[i] = item;
		}
		return arr;
	}

	public static int hexStringToInt(String hexString) {
		return (int) TypeConvert.hexStringToLong(hexString);
//		byte[] bytes = hexStringToBytes(hexString);
//		if (bytes != null && bytes.length > 0) {
//			return byteToInt(bytes[0]);
//		}
//		return Integer.MIN_VALUE;
	}

	public static String intToHexString(int value) {
		String hex = Integer.toHexString(value);
		if (hex.length() == 1) {
			hex = "0" + hex;
		}
		return hex;
	}

	public static String intToHexString(int value, boolean isUpper) {
		String hex = Integer.toHexString(value);
		if (hex.length() == 1) {
			hex = "0" + hex;
		}
		if (isUpper) {
			hex = hex.toUpperCase();
		} else {
			hex = hex.toLowerCase();
		}
		return hex;
	}

	public static byte hexCharToByte(char c) {

		return (byte) "0123456789ABCDEF".indexOf(c);

	}

	public static int hexCharToInt(char c) {

		return "0123456789ABCDEF".indexOf(c);

	}

	public static byte intToByte(int value) {
		return (byte) (value & 0xFF);
	}
	public static int bytesToInt(byte[] bytes) {
		int value=0;
		if (bytes!=null && bytes.length<=4) {
			//i=3 <<0 i=2 <<8 i=1 <<16 i=0<<24
			for (int i = bytes.length-1; i>=0 ; i--) {
				int b=bytes[i] & 0xff;
				value+=b<<((bytes.length-1-i)*8);
			}
			return value;
		}else {
			return Integer.MIN_VALUE;
		}
		
	}
	public static long bytesToLong(byte[] bytes) {
		long value=0;
		if (bytes!=null && bytes.length<=8) {
			//i=3 <<0 i=2 <<8 i=1 <<16 i=0<<24
			for (int i = bytes.length-1; i>=0 ; i--) {
				int b=bytes[i] & 0xff;
				value+=b<<((bytes.length-1-i)*8);
			}
			return value;
		}else {
			return Long.MIN_VALUE;
		}
		
	}
	/**@deprecated*/
	public static byte[] intToByteArray1(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	public static byte[] intToByteArray(int value, int count) {
		int i=value;
		byte[] result = new byte[count];
		switch (count) {
		case 1:
			result[0] = (byte) (i & 0xFF);
			break;
		case 2:
			result[0] = (byte) ((i >> 8) & 0xFF);
			result[1] = (byte) (i & 0xFF);
			break;
		case 3:
			result[0] = (byte) ((i >> 16) & 0xFF);
			result[1] = (byte) ((i >> 8) & 0xFF);
			result[2] = (byte) (i & 0xFF);
			break;
		case 4:
			result[0] = (byte) ((i >> 24) & 0xFF);
			result[1] = (byte) ((i >> 16) & 0xFF);
			result[2] = (byte) ((i >> 8) & 0xFF);
			result[3] = (byte) (i & 0xFF);
			break;
		default:
			break;
		}
		return result;
	}
	public static byte[] longToByteArray(long value, int count) {
		long i=value;
		byte[] result = new byte[count];
		switch (count) {
		case 1:
			result[0] = (byte) (i & 0xFF);
			break;
		case 2:
			result[0] = (byte) ((i >> 8) & 0xFF);
			result[1] = (byte) (i & 0xFF);
			break;
		case 3:
			result[0] = (byte) ((i >> 16) & 0xFF);
			result[1] = (byte) ((i >> 8) & 0xFF);
			result[2] = (byte) (i & 0xFF);
			break;
		case 4:
			result[0] = (byte) ((i >> 24) & 0xFF);
			result[1] = (byte) ((i >> 16) & 0xFF);
			result[2] = (byte) ((i >> 8) & 0xFF);
			result[3] = (byte) (i & 0xFF);
			break;
		case 5:
			result[0] = (byte) ((i >> 32) & 0xFF);
			result[1] = (byte) ((i >> 24) & 0xFF);
			result[2] = (byte) ((i >> 16) & 0xFF);
			result[3] = (byte) ((i >> 8) & 0xFF);
			result[4] = (byte) (i & 0xFF);
			break;
		case 6:
			result[0] = (byte) ((i >> 40) & 0xFF);
			result[1] = (byte) ((i >> 32) & 0xFF);
			result[2] = (byte) ((i >> 24) & 0xFF);
			result[3] = (byte) ((i >> 16) & 0xFF);
			result[4] = (byte) ((i >> 8) & 0xFF);
			result[5] = (byte) (i & 0xFF);
			break;
		case 7:
			result[0] = (byte) ((i >> 48) & 0xFF);
			result[1] = (byte) ((i >> 40) & 0xFF);
			result[2] = (byte) ((i >> 32) & 0xFF);
			result[3] = (byte) ((i >> 24) & 0xFF);
			result[4] = (byte) ((i >> 16) & 0xFF);
			result[5] = (byte) ((i >> 8) & 0xFF);
			result[6] = (byte) (i & 0xFF);
			break;
		case 8:
			result[0] = (byte) ((i >> 56) & 0xFF);
			result[1] = (byte) ((i >> 48) & 0xFF);
			result[2] = (byte) ((i >> 40) & 0xFF);
			result[3] = (byte) ((i >> 32) & 0xFF);
			result[4] = (byte) ((i >> 24) & 0xFF);
			result[5] = (byte) ((i >> 16) & 0xFF);
			result[6] = (byte) ((i >> 8) & 0xFF);
			result[7] = (byte) (i & 0xFF);
			break;
		default:
			break;
		}
		return result;
	}
	public static byte[] intArrayToByteArray(int[] intArray, int count) {
		byte[] result = new byte[intArray.length * count];
		for (int j = 0; j < intArray.length; j++) {
			int i = intArray[j];
			switch (count) {
			case 1:
				result[j * count] = (byte) (i & 0xFF);
				break;
			case 2:
				result[j * count + 0] = (byte) ((i >> 8) & 0xFF);
				result[j * count + 1] = (byte) (i & 0xFF);
				break;
			case 3:
				result[j * count + 0] = (byte) ((i >> 16) & 0xFF);
				result[j * count + 1] = (byte) ((i >> 8) & 0xFF);
				result[j * count + 2] = (byte) (i & 0xFF);
				break;
			case 4:
				result[j * count + 0] = (byte) ((i >> 24) & 0xFF);
				result[j * count + 1] = (byte) ((i >> 16) & 0xFF);
				result[j * count + 2] = (byte) ((i >> 8) & 0xFF);
				result[j * count + 3] = (byte) (i & 0xFF);
				break;
			default:
				break;
			}

		}
		return result;
	}

	public static String intArrayToHexString(int[] intArray) {
		String result = "";
		for (int i = 0; i < intArray.length; i++) {
			String str = Integer.toHexString(intArray[i]);
			if ((str.length() % 2) != 0) {
				str = "0" + str;
			}
			result += str;
		}
		return result;
	}

	public static byte[] intArrayToByteArray(int[] intArray) {
		byte[] result = new byte[intArray.length * 4];
		for (int j = 0; j < intArray.length; j++) {
			int i = intArray[j];
			// byte[] item = new byte[4];
			// item[0] = (byte) ((i >> 24) & 0xFF);
			// item[1] = (byte) ((i >> 16) & 0xFF);
			// item[2] = (byte) ((i >> 8) & 0xFF);
			// item[3] = (byte) (i & 0xFF);

			result[j * 4 + 0] = (byte) ((i >> 24) & 0xFF);
			result[j * 4 + 1] = (byte) ((i >> 16) & 0xFF);
			result[j * 4 + 2] = (byte) ((i >> 8) & 0xFF);
			result[j * 4 + 3] = (byte) (i & 0xFF);
		}

		return result;
	}
	/**@deprecated*/
	public static byte[] intToByteArray2(int i) throws Exception {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);
		out.writeInt(i);
		byte[] b = buf.toByteArray();
		out.close();
		buf.close();
		return b;
	}
}
