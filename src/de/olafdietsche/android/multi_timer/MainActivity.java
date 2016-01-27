// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.CursorAdapter;
import android.widget.ListView;

import de.olafdietsche.net.SimpleHttpClient;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ListView list = (ListView) findViewById(R.id.timer_list);

		adapter_ = new TimerCursorAdapter(this, null);
		list.setAdapter(adapter_);
	}

	@Override
	public void onResume() {
		super.onResume();
		updateTimerList(this);
	}

	private void updateTimerList(final Context context) {
		AsyncTask<Void, Void, Cursor> task = new AsyncTask<Void, Void, Cursor>() {
			@Override
			protected Cursor doInBackground(Void... unused) {
				DatabaseHelper db = new DatabaseHelper(context);
				TimerTableHelper helper = new TimerTableHelper(db);
				Cursor cursor = helper.query(projection_, null, null, "timername desc");
				return cursor;
			}

			@Override
			protected void onPostExecute(Cursor cursor) {
				adapter_.changeCursor(cursor);
			}
		};

		task.execute();
	}

	private CursorAdapter adapter_;
	private static final String[] projection_ = new String[] {
		BaseColumns._ID,
		TimerTableHelper.COLUMN_NAME_TIMERNAME,
		TimerTableHelper.COLUMN_NAME_TIMERDURATION
	};

	private static final String TAG = MainActivity.class.getName();
}
