package com.huashengfu.StemCellsManager.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtil {
	
	private FileUtil(){
		super();
	}

	public static void obbFile(File file) throws IOException{
		obbFile(file, false);
	}
	
	public static void obbFile(File file, boolean append) throws IOException{
		if(file.exists()){
			if(!append){
				file.delete();
				file.createNewFile();
			}
		} else {
			obbDir(file.getParentFile());
			file.createNewFile();
		}
	}
	
	public static void obbDir(File file){
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	/**
	 * 删除文件
	 * @param file
	 */
	public static void deleteFile(File file){
		if(file.exists()){
			if(file.isFile()){
				file.delete();
			}
		}
	}
	
	/**
	 * 保存一个输入流到文件
	 * @param is
	 * @param saveFile
	 * @throws IOException 
	 */
	public static void writeToFile(InputStream is, File saveFile) throws IOException{
		OutputStream os = new FileOutputStream(saveFile);
		int bytesRead = 0;
		byte[] buffer = new byte[1024*1];
		while ((bytesRead = is.read(buffer, 0, 1024*1)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		is.close();
	}
	
	/**
	 * 保存一个str到文件
	 * @param file
	 * @param str
	 * @throws IOException 
	 */
	public static void writeStrToFile(File file, String str) throws IOException {
		obbFile(file, false);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(str.getBytes());
		outStream.flush();
		outStream.close();
	}
	
	/**
	 * 从文件中获取str内容
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	public static String readStrFromFile(String filePath) throws IOException {
		InputStream is = new FileInputStream(new File(filePath));
		String s = readStrFromInputStream(is);
		is.close();
		return s;
	}
	
	/**
	 * 从输入流中读取字符串
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	public static String readStrFromInputStream(InputStream is) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null){
			buffer.append(line);
		}
		in.close();
		return buffer.toString();
	}

	public static String getImagePath(Activity activity, Uri uri, String selection){
		String Path=null;
		Cursor cursor = activity.getContentResolver().query(uri,null,selection,null,null);
		if(cursor!=null){
			if(cursor.moveToFirst()){
				Path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			cursor.close();
		}
		return Path;
	}

	public static String getVideoPath(Activity activity, Uri uri, String selection){
		String Path=null;
		Cursor cursor = activity.getContentResolver().query(uri,null,selection,null,null);
		if(cursor!=null){
			if(cursor.moveToFirst()){
				Path=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
			}
			cursor.close();
		}
		return Path;
	}
}