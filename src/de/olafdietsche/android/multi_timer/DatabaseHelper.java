// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "timer.db";
	private static final int DATABASE_VERSION = 2;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TimerTableHelper.createTableStmt);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > DATABASE_VERSION)
			throw new UnsupportedOperationException("Database upgrade from version " + oldVersion + " to version " + newVersion + " not implemented");

		if (oldVersion == 1 && newVersion >= 2)
			upgradeV2(db);
	}

	private void upgradeV2(SQLiteDatabase db) {
		db.execSQL(TimerTableHelper.upgradeV2);
	}
}
