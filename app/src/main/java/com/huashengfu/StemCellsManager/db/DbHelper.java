package com.huashengfu.StemCellsManager.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.huashengfu.StemCellsManager.entity.User;
import com.huashengfu.StemCellsManager.entity.goods.Specifications;

public class DbHelper extends SQLiteOpenHelper{

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				User.Table + " (" +
				User.Token.toLowerCase() + " varchar2(100), " +
				User.Username.toLowerCase() + " varchar2(100), " +
				User.Password.toLowerCase() + " varchar2(100), " +
				User.StoreName.toLowerCase() + " varchar2(100) " +
					")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + User.Table);
		onCreate(db);
	}
	
}
