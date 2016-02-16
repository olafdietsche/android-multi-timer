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

		public Data(Bundle bundle) {
			id = bundle.getLong("id");
			name = bundle.getString("name");
			duration = bundle.getLong("duration");
			pausing = bundle.getBoolean("pausing");
			if (pausing)
				remaining = bundle.getLong("remaining");

			running = bundle.getBoolean("running");
			if (running)
				deadline = bundle.getLong("deadline");
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
			Bundle bundle = new Bundle(2);
			bundle.putLong("id", id);
			bundle.putString("name", name);
			bundle.putLong("duration", duration);
			bundle.putLong("remaining", remaining);
			bundle.putLong("deadline", deadline);
			bundle.putBoolean("running", running);
			bundle.putBoolean("pausing", pausing);
			return bundle;
		}

		public static Data fromBundle(Bundle bundle) {
			return new Data(bundle);
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

	public int update(Data data) {
		ContentValues values = new ContentValues(4);
		values.put(COLUMN_NAME_NAME, data.name);
		values.put(COLUMN_NAME_DURATION, data.duration);

		if (data.isPausing())
			values.put(COLUMN_NAME_REMAINING, data.remaining);
		else
			values.put(COLUMN_NAME_REMAINING, (Long) null);

		if (data.isRunning())
			values.put(COLUMN_NAME_DEADLINE, data.deadline);
		else
			values.put(COLUMN_NAME_DEADLINE, (Long) null);

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
