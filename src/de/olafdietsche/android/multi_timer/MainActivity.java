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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
		list.setOnCreateContextMenuListener(this);
		list.setItemsCanFocus(true);

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

		AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				startTimerUpdate();
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
		};

		list.setOnScrollListener(listener);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		Log.d(TAG, "onCreateContextMenu");
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		if (info == null || info.id < 0)
			return;

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);

		TimerEntry te = getHolder(info.targetView);
		TimerTableHelper.Data data = te.getData();
		menu.setHeaderTitle(data.name);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d(TAG, "onContextItemSelected");
		switch (item.getItemId()) {
		case R.id.menu_delete:
			deleteItem(item);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void deleteItem(MenuItem item) {
		Log.d(TAG, "deleteItem");
		DatabaseHelper db = new DatabaseHelper(this);
		TimerTableHelper helper = new TimerTableHelper(db);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		helper.delete(info.id, null, null);
		fillTimerList(this);
	}

	public void startTimer(View view) {
		TimerEntry te = getHolder(view);
		te.startTimer();
		updateTimerData(view.getContext(), te.getData());
		startTimerUpdate();
	}

	public void stopTimer(View view) {
		TimerEntry te = getHolder(view);
		te.stopTimer();
		updateTimerData(view.getContext(), te.getData());
	}

	public void pauseTimer(View view) {
		TimerEntry te = getHolder(view);
		te.pauseTimer();
		updateTimerData(view.getContext(), te.getData());
	}

	public void resumeTimer(View view) {
		TimerEntry te = getHolder(view);
		te.resumeTimer();
		updateTimerData(view.getContext(), te.getData());
		startTimerUpdate();
	}

	public void editItem(View view) {
		TimerEntry te = getHolder(view);
		TimerTableHelper.Data data = te.getData();
		EditTimerActivity.start(this, data);
	}

	private TimerEntry getHolder(View view) {
		TimerEntry te = (TimerEntry) view.getTag();
		if (te == null) {
			View parent = (View) view.getParent();
			te = (TimerEntry) parent.getTag();
		}

		return te;
	}

	private void updateTimerData(final Context context, final TimerTableHelper.Data data) {
		DatabaseHelper db = new DatabaseHelper(context);
		TimerTableHelper helper = new TimerTableHelper(db);
		helper.update(data);
		fillTimerList(context);
	}

	private void fillTimerList(final Context context) {
		AsyncTask<Void, Void, Cursor> task = new AsyncTask<Void, Void, Cursor>() {
			@Override
			protected Cursor doInBackground(Void... unused) {
				DatabaseHelper db = new DatabaseHelper(context);
				TimerTableHelper helper = new TimerTableHelper(db);
				Cursor cursor = helper.query(TimerTableHelper.Data.projection, null, null, TimerTableHelper.COLUMN_NAME_DEADLINE + " is null, " + TimerTableHelper.COLUMN_NAME_DEADLINE + ", " + TimerTableHelper.COLUMN_NAME_NAME);
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
