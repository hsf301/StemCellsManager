package com.huashengfu.StemCellsManager.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>MD5</p>
 * <p>MD5转换处理类</p>
 *
 * @author		孙广智(tony.u.sun@163.com)
 * @version		0.0.1
 * <table style="border:1px solid gray;">
 * <tr>
 * <th width="100px">版本号</th><th width="100px">动作</th><th width="100px">修改人</th><th width="100px">修改时间</th>
 * </tr>
 * <!-- 以 Table 方式书写修改历史 -->
 * <tr>
 * <td>0.0.1</td><td>创建类</td><td>sunguangzhi</td><td>2013-4-18 上午10:56:36</td>
 * </tr>
 * </table>
*/
public class MD5 {

	public static String toMD516(byte[] data){
		return toMD5(data).substring(8, 24);
	}
	
	public static String toMD5(byte[] data){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(data);
			return toHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String toHex(byte[] data){
		StringBuilder sb = new StringBuilder();
		int x;
		for(int i=0; i<data.length; i++){
			x = data[i];
			if(x < 0)
				x += 256;
			if(x < 16)
				sb.append("0");
			sb.append(Integer.toHexString(x));
		}
		
		return sb.toString();
	}
}
