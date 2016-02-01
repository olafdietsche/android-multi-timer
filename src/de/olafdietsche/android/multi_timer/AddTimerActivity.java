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

public class AddTimerActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_timer);
		timername_ = (EditText) findViewById(R.id.timername);
		timerdurationHours_ = (EditText) findViewById(R.id.timerduration_hours);
		timerdurationMinutes_ = (EditText) findViewById(R.id.timerduration_minutes);
		timerdurationSeconds_ = (EditText) findViewById(R.id.timerduration_seconds);
	}

	public void addTimer(View view) {
		DatabaseHelper db = new DatabaseHelper(this);
		TimerTableHelper helper = new TimerTableHelper(db);
		ContentValues values = new ContentValues(2);

		String s = timername_.getText().toString();
		values.put(TimerTableHelper.COLUMN_NAME_NAME, s);

		long duration = 0;
		s = timerdurationHours_.getText().toString();
		if (s.length() > 0)
			duration += Long.parseLong(s) * 3600;

		s = timerdurationMinutes_.getText().toString();
		if (s.length() > 0)
			duration += Long.parseLong(s) * 60;

		s = timerdurationSeconds_.getText().toString();
		if (s.length() > 0)
			duration += Long.parseLong(s);

		values.put(TimerTableHelper.COLUMN_NAME_DURATION, duration);

		helper.insert(values);
		helper.close();
		finish();
	}

	public static void start(Context context) {
		Intent intent = new Intent(context, AddTimerActivity.class);
		context.startActivity(intent);
	}

	private EditText timername_, timerdurationHours_, timerdurationMinutes_, timerdurationSeconds_;
	private static final String TAG = MainActivity.class.getName();
}
