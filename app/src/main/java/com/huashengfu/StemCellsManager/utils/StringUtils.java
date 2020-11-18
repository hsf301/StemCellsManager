package com.huashengfu.StemCellsManager.utils;

import android.util.Log;

import com.huashengfu.StemCellsManager.Constants;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * <p>StringUtils</p>
 * <p>文本处理工具类</p>
 *
 * @author		孙广智(tony.u.sun@163.com)
 * @version		0.0.1
 * <table style="border:1px solid gray;">
 * <tr>
 * <th width="100px">版本号</th><th width="100px">动作</th><th width="100px">修改人</th><th width="100px">修改时间</th>
 * </tr>
 * <!-- 以 Table 方式书写修改历史 -->
 * <tr>
 * <td>0.0.1</td><td>创建类</td><td>sunguangzhi</td><td>2013-4-18 上午10:47:10</td>
 * </tr>
 * </table>
*/
public class StringUtils {

	/**
	 * 判断给定的字符串是否为合理的手机号码
	 * 
	 * @param msisdn	手机号码
	 * @return			是：true，否：false
	 */
	public static boolean isMobile(String msisdn) {
		if (isNullOrBlank(msisdn))
			return false;

//		Pattern p = Pattern
//				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		Matcher m = p.matcher(msisdn);
//		return m.matches();
		
		if(msisdn.length() != 11)
			return false;
		
		if(!msisdn.substring(0, 1).equals("1"))
			return false;
		
		return true;
	}
	
	/**
	 * 判断是否为电话的区号部分
	 * 
	 * @param areaCode
	 * @return
	 */
	public static boolean isAreaCode(String areaCode){
		if(isNullOrBlank(areaCode))
			return false;
		
		if(!areaCode.substring(0, 1).equals("0"))
			return false;
		
		return true;
	}
	
	/**
	 * 判断是否为有效的固定电话，7-8位
	 * 
	 * @param tel
	 * @return
	 */
	public static boolean isTelephone(String tel){
		if(isNullOrBlank(tel))
			return false;
		
		if(tel.length() > 8 || tel.length() < 7)
			return false;
		
		return true;
	}

	public static boolean isCMCCMobile(String msisdn) {
		// 1340-1348、135、136、137、138、139、147（数据卡专用）、150、151、152、157(TD专属号段)、158、159、182、183（2011年新放号）、187、188
		// 134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		if (isNullOrBlank(msisdn))
			return false;

		if (msisdn.length() != 11)
			return false;

		if (!isMobile(msisdn))
			return false;

		String str = msisdn.substring(0, 3);
		if (str.equals("134") || str.equals("135") || str.equals("136")
				|| str.equals("137") || str.equals("138") || str.equals("139")
				|| str.equals("150") || str.equals("151") || str.equals("152")
				|| str.equals("157") || str.equals("158") || str.equals("159")
				|| str.equals("182") || str.equals("183") || str.equals("187")
				|| str.equals("188"))
			return true;

		return false;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(String str) {
		if (null == str)
			return true;
		if (str.length() == 0)
			return true;
		return false;
	}
	
	/**
	 * 将时间字符串转换为long型数据
	 * 时间格式为：20120621144107
	 * @param time
	 * @return
	 */
	public static long getTime(String time){
		if(time == null || time.length() != 14)
			return System.currentTimeMillis();
		else{
			String year = time.substring(0, 4);
			String month = time.substring(4,6);
			String day = time.substring(6, 8);
			String hour = time.substring(8, 10);
			String minute = time.substring(10, 12);
			String second = time.substring(12, 14);
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(year));
			cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
			cal.set(Calendar.MINUTE, Integer.parseInt(minute));
			cal.set(Calendar.SECOND, Integer.parseInt(second));
			
			return cal.getTimeInMillis();
		}
	}
	
	/**
	 * 输入的字符是否是汉字
	 * 
	 * @param a
	 *            char
	 * @return boolean
	 */
	public static boolean isChinese(char a) {
		int v = (int) a;
		return (v >= 19968 && v <= 171941);
	}

	public static boolean containsChinese(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (isChinese(s.charAt(i)))
				return true;
		}
		return false;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	public static boolean isPassword(String str){
		String reg = ".*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*?_.\\(\\)]).*";
		Pattern pattern = Pattern.compile(reg);
		return pattern.matcher(str).matches();
	}
	
	public static String Double2String(double d){
		DecimalFormat df = new DecimalFormat("#");
		return df.format(d);
	}
	
	/**
	 * 安全的trim方式，剪头去尾，在输入为空的情况下也能安全剪除字符串两端的空白
	 * 
	 * @param str
	 * @return 返回绝不为空，至少为长度为0的串
	 */
	public static final String trim(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}
	
	public static boolean isPasswordCharacterAndNumber(String str){
		String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,32}$";
		Pattern pattern = Pattern.compile(reg);
		return pattern.matcher(str).matches();
	}

	public static String getWeek(long date){
		String[] week = new String[]{
				"星期日","星期一","星期二","星期三","星期四","星期五","星期六"
        };

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);

		return week[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

	public static String getSex(int sex){
		String str = "";
		if(sex == 1)
			str = "先生";
		else
			str = "女士";
		return str;
	}

	public static void print(String str){
		int start = 0;
		int end = 500;
		if(str.length() <= end){
			Log.i(Constants.Log.Log, str);
		}else{
			while(true){
				Log.i(Constants.Log.Log, str.substring(start, end));
				start = end;
				end += 500;
				if(end > str.length()){
					Log.i(Constants.Log.Log, str.substring(start));
					break;
				}
			}
		}
	}
}
