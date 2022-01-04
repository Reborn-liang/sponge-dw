package cn.nearf.ggz.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ByteArray {
	
	public static final byte ZERO_BYTE = 0x00;
	public static final byte BOOLEAN_TRUE = 0x01;
	public static final byte BOOLEAN_FALSE = 0x00;
	
	private List<Byte> allBytes = new ArrayList<Byte>();
	
	private final int limited;
	
	public ByteArray() {
		this.limited = -1;
	}
	
	public ByteArray(final int limited) {
		this.limited = limited;
	}
	
	public void appendByte(byte b) {
		if (limited > 0 && allBytes.size() >= limited) {
			return;
		}
		allBytes.add(b);
	}
	
	public void appendBytes(byte[] bs) {
		for (byte b : bs) {
			appendByte(b);
		}
	}
	
	public void appendBytes(byte[] bs, int len) {
		int rLen = bs.length > len ? len : bs.length;
		for (int i =  0; i < rLen; i ++) {
			byte b = bs[i];
			appendByte(b);
		}
	}
	
	public void appendUtf8String(String s) {
		try {
			appendBytes(s.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	public void appendStringByEncode(String s, String encode) {
		try {
			appendBytes(s.getBytes(encode));
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	public void appendEnum(Enum enumObj) {
		this.appendInt(enumObj.ordinal());
	}
	
	public void appenEnum(Enum enumObj, int byteSize) {
		this.appendLong(enumObj.ordinal(), byteSize);
	}
	
	public void appendInt(int i) {
		byte[] bytes = new byte[4];
        bytes[0] = (byte) (i & 0xff);
        bytes[1] = (byte) ((i & 0xff00) >> 8);
        bytes[2] = (byte) ((i & 0xff0000) >> 16);
        bytes[3] = (byte) ((i & 0xff000000) >> 24);
        appendBytes(bytes);
	}
	
	public void appendInt(int i, int byteSize) {
		byte[] bytes = new byte[byteSize];
		
		int j = 0;
		for (j = 0; j < byteSize && j < 4; j ++) {
			bytes[j] = (byte) ((i & (0xff << (2 * j))) >> (8 * j));
		}
		for (; j < byteSize; j ++) {
			bytes[j] = ZERO_BYTE;
		}
		
		appendBytes(bytes);
	}
	
	public void appendLong(long l) {
        appendLong(l, 8);
	}
	
	public void appendLong(long l, int byteSize) {
		byte[] bytes = new byte[byteSize];
        bytes[0] = (byte) (l & 0xff);
        for (int i = 1; i < byteSize; i ++) {
        	bytes[i] = (byte) ((l >> (i * 8)) & 0xff);
        }
        appendBytes(bytes);
	}
	
	public void appendDouble(double d) {
		appendLong(Double.doubleToLongBits(d));
	}
	
	public void appendBoolean(boolean b) {
		appendByte(b ? BOOLEAN_TRUE : BOOLEAN_FALSE);
	}
	
	public void appendDatetime(Date date) {
		final long time = date.getTime();
		appendLong(time);
	}
	
	
	public void replace(byte[] bs, int from) {
		if (allBytes.size() < from) {
			while (allBytes.size() < from) {
				appendByte(ZERO_BYTE);
			}
			appendBytes(bs);
		} else {
			for (int i = from; i < from + bs.length; i ++) {
				if (allBytes.size() < i) {
					appendByte(bs[i - from]);
				} else {
					allBytes.set(i, bs[i - from]);
				}
			}
		}
	}
	
	public void clear() {
		allBytes.clear();
	}
	
	/////////////////////////////
	public byte getByte(int index) {
		if (index >= 0 && index < allBytes.size()) {
			return allBytes.get(index);
		}
		return ZERO_BYTE;
	}
	
	public int getInt(int index) {
		return getInt(index, 4);
	}
	
	public int getInt(int index, int length) {
		byte[] bytes = getBytes(index, length, ZERO_BYTE);
		int res = (0xff & bytes[0]);
		for (int i = 1; i < bytes.length; i++) {
			res |=  ((0xffl << (2 * i)) & (bytes[i] << (8 * i)));
		}
		return res;
	}
	
	public long getLong(int index) {
		return getLong(index, 4);
	}
	
	public long getLong(int index, int length) {
		byte[] bytes = getBytes(index, length, ZERO_BYTE);
		long res = 0;
		for (int ix = 0; ix < 8; ++ix) {
			res <<= 8;
			res |= (bytes[ix] & 0xff);
		}
		return res;
	}
	
	/////////////////////////////
	public byte[] getBytes(int from, int length, byte defaultValue) {
		byte[] res = new byte[length];
		int expectedLen = from + length;
		boolean moreByte = expectedLen > allBytes.size();
		if (moreByte) {
			if (allBytes.size() <= from) {
				for (int i = 0; i < length; i++) {
					res[i] = defaultValue;
				}
			} else {
				for (int i = from; i < allBytes.size(); i++) {
					byte b = allBytes.get(i);
					res[i - from] = b;
				}
				for (int i = allBytes.size(); i < expectedLen; i++) {
					res[i - from] = defaultValue;
				}
			}
		} else {
			for (int i = 0; i < length; i++) {
				byte b = allBytes.get(from + i);
				res[i] = b;
			}
		}
		
		return res;
	}
	
	public byte[] getBytes(int length, byte defaultValue) {
		return getBytes(0, length, defaultValue);
	}
	
	public byte[] getBytes(int length) {
		return getBytes(length, ZERO_BYTE);
	}
	
	public byte[] getBytes() {
		byte[] res = new byte[allBytes.size()];
		for (int i = 0; i < res.length; i++) {
			byte b = allBytes.get(i);
			res[i] = b;
		}
		return res;
	}
	
	public static void main(String[] args) {
		byte[] test = "hello world".getBytes();
		
		ByteArray array = new ByteArray();
		array.appendBytes(test);
		
		System.out.println(new String(array.getBytes()));
		
		array = new ByteArray();
		array.appendInt(3, 2);
		
		System.out.println(array.getBytes()[0] + "," + array.getBytes()[1]);
	}

	
}
