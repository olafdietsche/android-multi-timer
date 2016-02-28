// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;

import de.olafdietsche.android.database.TableHelper;

public class TimerTableHelper extends TableHelper {
	private static final String TABLE_NAME = "timer";

	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_DURATION = "duration";
	public static final String COLUMN_NAME_REMAINING = "remaining";
	public static final String COLUMN_NAME_DEADLINE = "deadline";
	private static final String COLUMN_NAME_PAUSING = "pausing";
	private static final String COLUMN_NAME_RUNNING = "running";

	public static class Data {
		public Data() {
		}

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

		public Data(Bundle bundle) {
			id = bundle.getLong(BaseColumns._ID);
			name = bundle.getString(COLUMN_NAME_NAME);
			duration = bundle.getLong(COLUMN_NAME_DURATION);
			pausing = bundle.getBoolean(COLUMN_NAME_PAUSING);
			if (pausing)
				remaining = bundle.getLong(COLUMN_NAME_REMAINING);

			running = bundle.getBoolean(COLUMN_NAME_RUNNING);
			if (running)
				deadline = bundle.getLong(COLUMN_NAME_DEADLINE);
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

		public Bundle toBundle() {
			Bundle bundle = new Bundle(7);
			bundle.putLong(BaseColumns._ID, id);
			bundle.putString(COLUMN_NAME_NAME, name);
			bundle.putLong(COLUMN_NAME_DURATION, duration);
			bundle.putLong(COLUMN_NAME_REMAINING, remaining);
			bundle.putLong(COLUMN_NAME_DEADLINE, deadline);
			bundle.putBoolean(COLUMN_NAME_RUNNING, running);
			bundle.putBoolean(COLUMN_NAME_PAUSING, pausing);
			return bundle;
		}

		public ContentValues toContentValues() {
			ContentValues values = new ContentValues(4);
			values.put(COLUMN_NAME_NAME, name);
			values.put(COLUMN_NAME_DURATION, duration);

			if (isPausing())
				values.put(COLUMN_NAME_REMAINING, remaining);
			else
				values.put(COLUMN_NAME_REMAINING, (Long) null);

			if (isRunning())
				values.put(COLUMN_NAME_DEADLINE, deadline);
			else
				values.put(COLUMN_NAME_DEADLINE, (Long) null);

			return values;
		}

		public static Data fromBundle(Bundle bundle) {
			return new Data(bundle);
		}

		public String toString() {
			return "{TimerTableHelper.Data: " + name + ", " + duration + ", " + pausing + "/" + remaining + ", " + running + "/" + deadline + "}";
		}

		long id;
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

	public long insert(Data data) {
		ContentValues values = data.toContentValues();
		data.id = insert(values);
		return data.id;
	}

	public int update(Data data) {
		ContentValues values = data.toContentValues();
		return update(data.id, values);
	}

	static final String createTableStmt = "create table timer (" +
		BaseColumns._ID + " integer primary key," +
		COLUMN_NAME_NAME + " text not null," +
		COLUMN_NAME_DURATION + " long not null," +
		COLUMN_NAME_REMAINING + " long," +
		COLUMN_NAME_DEADLINE + " long" +
		")";
}
