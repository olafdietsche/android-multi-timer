// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import de.olafdietsche.android.database.TableHelper;

public class TimerTableHelper extends TableHelper {
	private static final String TABLE_NAME = "timer";

	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_DURATION = "duration";
	public static final String COLUMN_NAME_REMAINING = "remaining";
	public static final String COLUMN_NAME_DEADLINE = "deadline";

	public static class Data {
		public Data(Cursor cursor) {
			id = cursor.getLong(idIndex);
			name = cursor.getString(nameIndex);
			duration = cursor.getLong(durationIndex);

			pausing = !cursor.isNull(remainingIndex);
			if (pausing)
				remaining = cursor.getLong(remainingIndex);

			running = !cursor.isNull(deadlineIndex);
			if (running)
				deadline = cursor.getLong(deadlineIndex);
		}

		public void startTimer() {
			if (!isRunning()) {
				long now = System.currentTimeMillis() / 1000;
				deadline = now + duration;
				running = true;
				pausing = false;
			}
		}

		public void stopTimer() {
			running = pausing = false;
		}

		public void pauseTimer() {
			if (!isRunning())
				return;

			long now = System.currentTimeMillis() / 1000;
			remaining = deadline - now;
			running = false;
			pausing = true;
		}

		public void resumeTimer() {
			if (!isPausing())
				return;

			long now = System.currentTimeMillis() / 1000;
			deadline = now + remaining;
			pausing = false;
			running = true;
		}

		public boolean isRunning() {
			return running;
		}

		public boolean isPausing() {
			return pausing;
		}

		public String toString() {
			return "{TimerTableHelper.Data: " + name + ", " + duration + ", " + pausing + "/" + remaining + ", " + running + "/" + deadline + "}";
		}

		final long id;
		String name;
		long duration;
		long remaining;
		long deadline;
		boolean running, pausing;

		private static final int idIndex = 0, nameIndex = 1, durationIndex = 2, remainingIndex = 3, deadlineIndex = 4;

		public static final String[] projection = new String[] {
			BaseColumns._ID,
			TimerTableHelper.COLUMN_NAME_NAME,
			TimerTableHelper.COLUMN_NAME_DURATION,
			TimerTableHelper.COLUMN_NAME_REMAINING,
			TimerTableHelper.COLUMN_NAME_DEADLINE,
		};
	}

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
