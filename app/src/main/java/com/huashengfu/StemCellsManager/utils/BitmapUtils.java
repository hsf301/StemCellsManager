package com.huashengfu.StemCellsManager.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * BitmapUtils
 * </p>
 * <p>
 * 位图图像处理工具类
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
 *          <td>2013-8-15 下午02:03:18</td>
 *          </tr>
 *          <tr>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          </tr>
 *          </table>
 */
public class BitmapUtils {

	/**
	 * 图片压缩处理
	 * 
	 * @param src
	 *            源图
	 * @param quality
	 *            图片品质 1-100
	 * @return 压缩后的图片品质
	 */
	public static int zipBitmap(Bitmap src, int quality) {

		byte[] data = null;

		for (int i = quality; i > 0; i -= 10) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			src.compress(Bitmap.CompressFormat.JPEG, i, baos);

			data = baos.toByteArray();
			double size = data.length / 1024;

			if (size < 15) {
				quality = i;
				break;
			}
		}

		src = BitmapFactory.decodeByteArray(data, 0, data.length);

		return quality;
	}

	/**
	 * 生成圆角图片
	 * 
	 * @param bitmap
	 *            原图
	 * @param pixels
	 *            圆角半径
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
		RectF rectF = new RectF(rect);
		float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	public static byte[] decodeBitmap(String path) {  
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高  
        BitmapFactory.decodeFile(path, opts);  
        opts.inSampleSize = computeSampleSize(opts, -1, opts.outWidth * opts.outHeight);  
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true  
        opts.inPurgeable = true;  
        opts.inInputShareable = true;  
        opts.inDither = false;  
        opts.inPurgeable = true;  
        opts.inTempStorage = new byte[opts.outWidth * opts.outHeight];  
        FileInputStream is = null;  
        Bitmap bmp = null;  
        ByteArrayOutputStream baos = null;  
        try {  
            is = new FileInputStream(path);  
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);  
            double scale = getScaling(opts.outWidth * opts.outHeight,  
            		opts.outWidth * opts.outHeight);  
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,  
                    (int) (opts.outWidth * scale),  
                    (int) (opts.outHeight * scale), true);  
            baos = new ByteArrayOutputStream();  
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
            bmp2.recycle();  
            bmp.recycle();
            return baos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
            File f = new File(path);
            if(f.exists())
            	f.delete();
        } catch (Exception e) {  
            e.printStackTrace();  
            File f = new File(path);
            if(f.exists())
            	f.delete();
        } finally {  
            try {
            	if(is != null)
            		is.close();
            	
            	if(baos != null)
            		baos.close();
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            System.gc();  
        }  
        
        if(baos != null)
        	return baos.toByteArray();
        else
        	return null;
    }   
	
	public static byte[] decodeBitmap(byte[] data){
		BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高  
       
        opts.inSampleSize = BitmapUtils.computeSampleSize(opts, -1, 4160 * 3120);  
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true  
        opts.inPurgeable = true;  
        opts.inInputShareable = true;  
        opts.inDither = false;  
        opts.inPurgeable = true;  
        opts.inTempStorage = new byte[16 * 1024];  
        Bitmap bmp =  BitmapFactory.decodeByteArray(data, 0, data.length, opts);  
        ByteArrayOutputStream baos = null;  
        double scale = BitmapUtils.getScaling(opts.outWidth * opts.outHeight,  
                1024 * 600);  
        Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,  
                (int) (opts.outWidth * scale),  
                (int) (opts.outHeight * scale), true);  
        bmp.recycle();  
        baos = new ByteArrayOutputStream();  
        bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
        bmp2.recycle();
        bmp.recycle();
        return baos.toByteArray();
	}
  
    private static double getScaling(int src, int des) {  
        /** 
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49 
         */  
        double scale = Math.sqrt((double) des / (double) src);  
        return scale;  
    }  
  
    public static int computeSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        int initialSize = computeInitialSampleSize(options, minSideLength,  
                maxNumOfPixels);  
  
        int roundedSize;  
        if (initialSize <= 8) {  
            roundedSize = 1;  
            while (roundedSize < initialSize) {  
                roundedSize <<= 1;  
            }  
        } else {  
            roundedSize = (initialSize + 7) / 8 * 8;  
        }  
  
        return roundedSize;  
    }  
  
    private static int computeInitialSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        double w = options.outWidth;  
        double h = options.outHeight;  
  
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math  
                .sqrt(w * h / maxNumOfPixels));  
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(  
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
  
        if (upperBound < lowerBound) {  
            return lowerBound;  
        }  
  
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
            return 1;  
        } else if (minSideLength == -1) {  
            return lowerBound;  
        } else {  
            return upperBound;  
        }  
    }
    
    public static Bitmap decodeBitmap(String path, int displayWidth, int displayHeight) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        // op.inJustDecodeBounds = true;表示我们只读取Bitmap的宽高等信息，不读取像素。
        Bitmap bmp = BitmapFactory.decodeFile(path, op); // 获取尺寸信息
        // op.outWidth表示的是图像真实的宽度
        // op.inSamplySize 表示的是缩小的比例
        // op.inSamplySize = 4,表示缩小1/4的宽和高，1/16的像素，android认为设置为2是最快的。
        // 获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / (float) displayWidth);
        int hRatio = (int) Math.ceil(op.outHeight / (float) displayHeight);
        // 如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1) {
                if (wRatio > hRatio) {
                        // 如果太宽，我们就缩小宽度到需要的大小，注意，高度就会变得更加的小。
                        op.inSampleSize = wRatio;
                } else {
                        op.inSampleSize = hRatio;
                }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        Bitmap compress_bmp = saveBitmap(path,bmp);
        // 从原Bitmap创建一个给定宽高的Bitmap
        return Bitmap.createScaledBitmap(compress_bmp, displayWidth, displayHeight, true);
	}
    
    public static Bitmap saveBitmap(String filePath,Bitmap bm) {
		File file = new File(filePath);
		try {
			FileUtil.obbFile(file);
			FileOutputStream out = new FileOutputStream(file);
 			bm.compress(Bitmap.CompressFormat.JPEG, 70, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}
    
    /**
     * 从assets文件夹中获取图片
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap imageFromAssetsFile(Context context, String fileName) {
    	
    	Bitmap bitmap = null;  
        AssetManager am =context.getResources().getAssets();  
        
        try  
        {  
            InputStream is = am.open(fileName);  
            bitmap = BitmapFactory.decodeStream(is);  
            is.close();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    
        return bitmap; 
    	
    }
}
