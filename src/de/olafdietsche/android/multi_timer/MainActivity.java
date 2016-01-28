// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_timer:
			AddTimerActivity.start(this);
			return true;
		}

		return false;
	}

	public void startTimer(View view) {
		Log.d(TAG, "startTimer");
		TimerEntry tv = getHolder(view);
		tv.startTimer();
	}

	public void stopTimer(View view) {
		Log.d(TAG, "stopTimer");
		TimerEntry tv = getHolder(view);
		tv.stopTimer();
	}

	public void pauseTimer(View view) {
		Log.d(TAG, "pauseTimer");
		TimerEntry tv = getHolder(view);
		tv.pauseTimer();
	}

	public void resumeTimer(View view) {
		Log.d(TAG, "resumeTimer");
		TimerEntry tv = getHolder(view);
		tv.resumeTimer();
	}

	private TimerEntry getHolder(View view) {
		View parent = (View) view.getParent();
		TimerEntry tv = (TimerEntry) parent.getTag();
		return tv;
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
