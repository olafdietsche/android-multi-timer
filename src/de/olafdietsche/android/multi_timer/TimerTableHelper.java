// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import de.olafdietsche.android.database.TableHelper;

public class TimerTableHelper extends TableHelper {
	private static final String TABLE_NAME = "timer";

	public static final String COLUMN_NAME_TIMERNAME = "timername";
	public static final String COLUMN_NAME_TIMERDURATION = "timerduration";

	public TimerTableHelper(SQLiteOpenHelper dbHelper) {
		super(dbHelper, TABLE_NAME);
	}

	static final String createTableStmt = "create table timer (" +
		BaseColumns._ID + " integer primary key," +
		COLUMN_NAME_TIMERNAME + " text not null," +
		COLUMN_NAME_TIMERDURATION + " long not null)";
}
