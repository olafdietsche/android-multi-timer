// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private static final int MSG_UPDATE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ListView list = (ListView) findViewById(R.id.timer_list);

		adapter_ = new TimerCursorAdapter(this, null);
		list.setAdapter(adapter_);
		updateHandler = new Handler() {
			@Override
			public void handleMessage(Message m) {
				boolean cont = updateTimerEntries();
				if (cont)
					startTimerUpdate();
			}
		};
	}

	@Override
	public void onResume() {
		super.onResume();
		fillTimerList(this);
		startTimerUpdate();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopTimerUpdate();
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
		TimerEntry te = getHolder(view);
		te.startTimer();
		startTimerUpdate();
	}

	public void stopTimer(View view) {
		Log.d(TAG, "stopTimer");
		TimerEntry te = getHolder(view);
		te.stopTimer();
	}

	public void pauseTimer(View view) {
		Log.d(TAG, "pauseTimer");
		TimerEntry te = getHolder(view);
		te.pauseTimer();
	}

	public void resumeTimer(View view) {
		Log.d(TAG, "resumeTimer");
		TimerEntry te = getHolder(view);
		te.resumeTimer();
		startTimerUpdate();
	}

	private TimerEntry getHolder(View view) {
		View parent = (View) view.getParent();
		TimerEntry te = (TimerEntry) parent.getTag();
		return te;
	}

	private void fillTimerList(final Context context) {
		AsyncTask<Void, Void, Cursor> task = new AsyncTask<Void, Void, Cursor>() {
			@Override
			protected Cursor doInBackground(Void... unused) {
				DatabaseHelper db = new DatabaseHelper(context);
				TimerTableHelper helper = new TimerTableHelper(db);
				Cursor cursor = helper.query(TimerTableHelper.Data.projection, null, null, TimerTableHelper.COLUMN_NAME_NAME);
				return cursor;
			}

			@Override
			protected void onPostExecute(Cursor cursor) {
				adapter_.changeCursor(cursor);
			}
		};

		task.execute();
	}

	private void startTimerUpdate() {
		updateHandler.removeMessages(MSG_UPDATE);
		updateHandler.sendEmptyMessageDelayed(MSG_UPDATE, 1000);
	}

	private void stopTimerUpdate() {
		updateHandler.removeMessages(MSG_UPDATE);
	}

	private boolean updateTimerEntries() {
		boolean cont = false;
		ListView list = (ListView) findViewById(R.id.timer_list);
		int n = list.getChildCount();
		for (int i = 0; i < n; ++i) {
			View child = list.getChildAt(i);
			TimerEntry te = (TimerEntry) child.getTag();
			cont |= te.updateTimer();
		}

		return cont;
	}

	private Handler updateHandler;
	private CursorAdapter adapter_;

	private static final String TAG = MainActivity.class.getName();
}
