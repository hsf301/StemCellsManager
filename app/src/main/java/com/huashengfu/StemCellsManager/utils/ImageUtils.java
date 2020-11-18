package com.huashengfu.StemCellsManager.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;

/**
 * <p>ImageUtils</p>
 * <p>图像处理工具类</p>
 *
 * @author		孙广智(tony.u.sun@163.com)
 * @version		0.0.0
 * <table style="border:1px solid gray;">
 * <tr>
 * <th width="100px">版本号</th><th width="100px">动作</th><th width="100px">修改人</th><th width="100px">修改时间</th>
 * </tr>
 * <!-- 以 Table 方式书写修改历史 -->
 * <tr>
 * <td>0.0.0</td><td>创建类</td><td>sunguangzhi</td><td>2013-8-15 下午02:02:48</td>
 * </tr>
 * <tr>
 * <td>XXX</td><td>XXX</td><td>XXX</td><td>XXX</td>
 * </tr>
 * </table>
*/
public class ImageUtils {

	/**
	 * 获取从相册读取图片并裁剪的intent实例
	 * 
	 * @param uri			输出的图片文件uri
	 * @param aspectX		裁剪的宽度
	 * @param aspectY		裁剪的高度
	 * @param outputX		输出的宽度
	 * @param outputY		输出的高度
	 * @return
	 */
	public static Intent getAlbumImageIntent(Uri uri, int aspectX, int aspectY,
			int outputX, int outputY){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);  
        intent.setType("image/*");  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX", aspectX);  
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        
        return intent;
	}
	
	public static Intent getAlbumImageIntent(Uri uri){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);  
        intent.setType("image/*");  
        intent.putExtra("crop", "true");  
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        
        return intent;
	}
	
	/**
	 * 获取图片裁剪的intent实例
	 * 
	 * @param uri			输入输出的图片文件uri
	 * @param aspectX		裁剪的宽度
	 * @param aspectY		裁剪的高度
	 * @param outputX		输出的宽度
	 * @param outputY		输出的高度
	 * @return
	 */
	public static Intent getCropImageIntent(Uri uri, int aspectX, int aspectY,
			int outputX, int outputY){
		Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/*");  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX", aspectX);  
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        
        return intent;
	}
	
	/**
	 * 生成图片
	 * 
	 * @param context		上下文context
	 * @param uri			图片文件uri
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap decodeStream(Context context, Uri uri) throws FileNotFoundException{
		return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
	}
}
