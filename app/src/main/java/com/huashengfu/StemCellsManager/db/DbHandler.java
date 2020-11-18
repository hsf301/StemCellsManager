package com.huashengfu.StemCellsManager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.entity.User;
import com.huashengfu.StemCellsManager.entity.goods.Specifications;
import com.huashengfu.StemCellsManager.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DbHandler extends AbstractDbHandler {
	
	public static final Object lock = new Object();

	private static final String name = "StemCellsManager.db";

	private static final int version = 2;

	private DbHelper dbHelper;

	private static DbHandler dbHandler;

	private DbHandler(Context context) {
		dbHelper = new DbHelper(context, name, null, version);
	}

	public static DbHandler getInstance(Context context) {
		if (dbHandler == null)
			dbHandler = new DbHandler(context);
		return dbHandler;
	}

	@Override
	public void clear(String table) {
		synchronized (lock) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.delete(table, null, null);
			db.close();
		}
	}

	@Override
	public void save(String table, Object obj) {
		synchronized (lock) {
			try {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = getContentValues(obj);
				db.insert(table, null, values);
				db.close();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				Log.i(Constants.Log.Log, e.toString());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				Log.i(Constants.Log.Log, e.toString());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				Log.i(Constants.Log.Log, e.toString());
			} catch (Exception e) {
				e.printStackTrace();
				Log.i(Constants.Log.Log, e.toString());
			}

		}
	}

	public void update(User user){
		synchronized (lock) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			try {
				ContentValues values = getContentValues(user);
				db.update(User.Table, values, User.Username.toLowerCase() + "=?", new String[]{user.getUsername()});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			db.close();
		}
	}
	
	@Override
	public void sayByeBye() {
		if (dbHelper != null)
			dbHelper.close();
		dbHelper = null;
		dbHandler = null;
	}
	
	public boolean hasValue(String table, String where, String[] selectionArgs){
		synchronized (lock) {
			String sql = "select * from " + table + " where " + where;
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor c = db.rawQuery(sql, selectionArgs);
			boolean has = false;
			if(c.moveToFirst())
				has = true;
			c.close();
			db.close();
			return has;
		}
	}
	
	public User getUser(String username){
		if(StringUtils.isNullOrBlank(username))
			return null;
		
		synchronized (lock) {
			String sql = "select * from " + User.Table + " where " + User.Username.toLowerCase() + "=?";
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor c = db.rawQuery(sql, new String[]{username});
			User user = null;
			if(c.moveToNext())
				user = (User) getEntity(c, User.class);
			c.close();
			db.close();
			return user;
		}
	}

}
