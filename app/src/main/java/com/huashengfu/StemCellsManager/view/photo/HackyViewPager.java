package com.huashengfu.StemCellsManager.view.photo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * <p>HackyViewPager.java</p>
 * <p>HackyViewPager</p>
 *
 * @author		李振宁(zning.li@foxmail.com)
 * @version		0.0.1
 * <table style="border:1px solid gray;">
 * <tr>
 * <th width="100px">版本号</th><th width="100px">动作</th><th width="100px">修改人</th><th width="100px">修改时间</th>
 * </tr>
 * <!-- 以 Table 方式书写修改历史 -->
 * <tr>
 * <td>0.0.1</td><td>创建类</td><td>lizhenning</td><td>2015年1月9日 下午2:35:20</td>
 * </tr>
 * <tr>
 * <td>XXX</td><td>XXX</td><td>XXX</td><td>XXX</td>
 * </tr>
 * </table>
 */

public class HackyViewPager extends ViewPager {

	public HackyViewPager(Context context) {
		super(context);
	}

	
	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
	}

}
