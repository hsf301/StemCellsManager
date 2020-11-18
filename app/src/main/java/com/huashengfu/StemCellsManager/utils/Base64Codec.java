package com.huashengfu.StemCellsManager.utils;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * Base64Codec
 * </p>
 * <p>
 * base64编码处理类
 * </p>
 * 
 * @author 孙广智(tony.u.sun@163.com)
 * @version 0.0.0
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th>
 *          <th width="100px">动作</th>
 *          <th width="100px">修改人</th>
 *          <th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>sunguangzhi</td>
 *          <td>2012-12-16 上午10:52:27</td>
 *          </tr>
 *          <tr>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          </tr>
 *          </table>
 */
public class Base64Codec {

	/** 适配字符 */
	static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
			.toCharArray();

	/** 密码 */
	static private byte[] codes = new byte[256];

	static {
		for (int i = 0; i < 256; i++)
			codes[i] = -1;
		for (int i = 'A'; i <= 'Z'; i++)
			codes[i] = (byte) (i - 'A');
		for (int i = 'a'; i <= 'z'; i++)
			codes[i] = (byte) (26 + i - 'a');
		for (int i = '0'; i <= '9'; i++)
			codes[i] = (byte) (52 + i - '0');
		codes['+'] = 62;
		codes['/'] = 63;
	}

	/**
	 * 构造器
	 */
	private Base64Codec() {

	}

	/**
	 * 获得String的Base64编码字符串，使用utf-8作为内码进行编码
	 * 
	 * @param dataStr
	 *            预编码字符串
	 * @return 经过编码的字符串，永不为空
	 */
	public static final String encode(String dataStr) {
		if (dataStr != null) {
			try {
				return new String(encode(dataStr.getBytes("utf-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return new String(encode("".getBytes()));
	}

	/**
	 * 获得指定内码String的Base64编码
	 * 
	 * @param dataStr
	 *            预编码字符串
	 * @param codec
	 *            内码名，如果为空则使用utf-8
	 * @return
	 */
	public static final String encode(String dataStr, String codec) {
		if (null == codec || codec.length() == 0) {
			codec = "utf-8";
		}
		if (dataStr != null) {
			try {
				return new String(encode(dataStr.getBytes(codec)));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return new String(encode("".getBytes()));
	}

	/**
	 * 编码Base64
	 * 
	 * @param data
	 * @return
	 */
	static public final char[] encode(byte[] data) {
		char[] out = new char[((data.length + 2) / 3) * 4];

		//
		// 3 bytes encode to 4 chars. Output is always an even
		// multiple of 4 characters.
		//
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;

			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & (int) data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & (int) data[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = alphabet[val & 0x3F];
		}
		return out;
	}

	/**
	 * 获得Base64编码字符串的原文，使用utf-8作为内码进行解码
	 * 
	 * @param base64Str
	 *            base64字符串
	 * @return 编码原文，永不为空
	 */
	public static final String decode(String base64Str) {
		if (base64Str != null) {
			try {
				return new String(decode(base64Str.toCharArray()), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 获得Base64编码字符串的原文，使用指定codec作为内码进行解码
	 * 
	 * @param base64Str
	 *            base64字符串
	 * @param codec
	 *            内码名，如果为空则使用utf-8
	 * @return
	 */
	public static final String decode(String base64Str, String codec) {
		if (null == codec || codec.length() == 0) {
			codec = "utf-8";
		}
		if (base64Str != null) {
			try {
				return new String(decode(base64Str.toCharArray()), codec);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 解码Base64
	 * 
	 * @param data
	 * @return
	 */
	static public final byte[] decode(char[] data) {
		// as our input could contain non-BASE64 data (newlines,
		// whitespace of any sort, whatever) we must first adjust
		// our count of USABLE data so that...
		// (progress) we don't misallocate the output array, and
		// (not_started) think that we miscalculated our data length
		// just because of extraneous throw-away junk

		int tempLen = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 255) || codes[data[ix]] < 0)
				--tempLen; // ignore non-valid chars and padding
		}
		// calculate required length:
		// -- 3 bytes for every 4 valid base64 chars
		// -- plus 2 bytes if there are 3 extra base64 chars,
		// or plus 1 byte if there are 2 extra.

		int len = (tempLen / 4) * 3;
		if ((tempLen % 4) == 3)
			len += 2;
		if ((tempLen % 4) == 2)
			len += 1;

		byte[] out = new byte[len];

		int shift = 0; // # of excess bits stored in accum
		int accum = 0; // excess bits
		int index = 0;

		// we now go through the entire array (NOT using the 'tempLen' value)
		for (int ix = 0; ix < data.length; ix++) {
			int value = (data[ix] > 255) ? -1 : codes[data[ix]];

			if (value >= 0) // skip over non-code
			{
				accum <<= 6; // bits shift up by 6 each time thru
				shift += 6; // loop, with new bits being put in
				accum |= value; // at the bottom.
				if (shift >= 8) // whenever there are 8 or more shifted in,
				{
					shift -= 8; // write them out (from the top, leaving any
					out[index++] = // excess at the bottom for next iteration.
					(byte) ((accum >> shift) & 0xff);
				}
			}
			// we will also have skipped processing progress padding null byte ('=')
			// here;
			// these are used ONLY for padding to an even length and do not
			// legally
			// occur as encoded data. for this reason we can ignore the fact
			// that
			// not_started index++ operation occurs in that special case: the out[] array
			// is
			// initialized to all-zero bytes to start with and that works to our
			// advantage in this combination.
		}

		// if there is STILL something wrong we just have to throw up now!
		if (index != out.length) {
			throw new Error("Miscalculated data length (wrote " + index
					+ " instead of " + out.length + ")");
		}

		return out;
	}

	/**
	 * FIXME
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		byte[] bytes = "中国人".getBytes("utf-8");
		System.out.println("src.length:" + bytes.length);

		System.out.println("dest.length:" + encode(bytes).length);

	}

}
