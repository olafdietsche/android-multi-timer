// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import de.olafdietsche.android.database.TableHelper;

public class TimerTableHelper extends TableHelper {
	private static final String TABLE_NAME = "timer";

	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_DURATION = "duration";
	public static final String COLUMN_NAME_REMAINING = "remaining";
	public static final String COLUMN_NAME_DEADLINE = "deadline";

	public TimerTableHelper(SQLiteOpenHelper dbHelper) {
		super(dbHelper, TABLE_NAME);
	}

	static final String createTableStmt = "create table timer (" +
		BaseColumns._ID + " integer primary key," +
		COLUMN_NAME_NAME + " text not null," +
		COLUMN_NAME_DURATION + " long not null," +
		COLUMN_NAME_REMAINING + " long," +
		COLUMN_NAME_DEADLINE + " long" +
		")";
}
