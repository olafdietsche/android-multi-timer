// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import de.olafdietsche.android.widget.DurationEditText;

public class AddTimerActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_timer);
		timername_ = (EditText) findViewById(R.id.timername);
		timerduration_ = (DurationEditText) findViewById(R.id.timerduration);
	}

	public void saveTimer(View view) {
		DatabaseHelper db = new DatabaseHelper(this);
		TimerTableHelper helper = new TimerTableHelper(db);
		ContentValues values = new ContentValues(2);

		String s = timername_.getText().toString();
		values.put(TimerTableHelper.COLUMN_NAME_NAME, s);
		long duration = timerduration_.getDuration();
		values.put(TimerTableHelper.COLUMN_NAME_DURATION, duration);

		helper.insert(values);
		helper.close();
		finish();
	}

	public static void start(Context context) {
		Intent intent = new Intent(context, AddTimerActivity.class);
		context.startActivity(intent);
	}

	private EditText timername_;
	private DurationEditText timerduration_;
	private static final String TAG = MainActivity.class.getName();
}
