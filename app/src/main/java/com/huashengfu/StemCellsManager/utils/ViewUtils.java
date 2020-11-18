package com.huashengfu.StemCellsManager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * <p>ViewUtils</p>
 * <p>空间工具类</p>
 *
 * @author		孙广智(tony.u.sun@163.com;sunguangzhi@nvlbs.com)
 * @version		0.0.0
 * <table style="border:1px solid gray;">
 * <tr>
 * <th width="100px">版本号</th><th width="100px">动作</th><th width="100px">修改人</th><th width="100px">修改时间</th>
 * </tr>
 * <!-- 以 Table 方式书写修改历史 -->
 * <tr>
 * <td>0.0.0</td><td>创建类</td><td>sunguangzhi</td><td>2013-7-26 上午10:15:25</td>
 * </tr>
 * <tr>
 * <td>XXX</td><td>XXX</td><td>XXX</td><td>XXX</td>
 * </tr>
 * </table>
*/
public class ViewUtils {

	/**
	 * 获取控件的宽度
	 * @param view
	 * @return
	 */
	public static int getViewWidth(View view){
		int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		
		view.measure(w, h);
		return view.getMeasuredWidth();
	}
	
	/**
	 * 获取控件的高度
	 * @param view
	 * @return
	 */
	public static int getViewHeight(View view){
		int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		
		view.measure(w, h);
		return view.getMeasuredHeight();
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(float dpValue) {
		final float scale = Resources.getSystem().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(float pxValue) {
		final float scale = Resources.getSystem().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 * @param text
	 */
	public static void hideSoftInput(Context context, View text){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
	}
	
	/**
	 * 显示软键盘
	 * 
	 * @param context
	 * @param text
	 */
	public static void showSoftInput(Context context, View text){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(text, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 设置背景色
	 *
	 * @param alpha
	 */
	public static void background(Activity activity, float alpha){
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = alpha;
		activity.getWindow().setAttributes(lp);
	}
}
