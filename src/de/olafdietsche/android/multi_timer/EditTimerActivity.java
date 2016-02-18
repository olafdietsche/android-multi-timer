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

public class EditTimerActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("timer");
		data = new TimerTableHelper.Data(bundle);

		setContentView(R.layout.edit_timer);
		timername = (EditText) findViewById(R.id.timername);
		timername.setText(data.name);
		long duration = data.duration;
		timerduration = (DurationEditText) findViewById(R.id.timerduration);
		timerduration.setDuration(duration);
	}

	public void saveTimer(View view) {
		String name = timername.getText().toString();
		long duration = timerduration.getDuration();

		if (!name.equals(data.name) || duration != data.duration) {
			DatabaseHelper db = new DatabaseHelper(this);
			TimerTableHelper helper = new TimerTableHelper(db);
			data.name = name;
			data.duration = duration;
			data.stopTimer();
			data.startTimer();
			helper.update(data);
			db.close();
		}

		finish();
	}

	public static void start(Context context, TimerTableHelper.Data data) {
		Intent intent = new Intent(context, EditTimerActivity.class);
		Bundle bundle = data.toBundle();
		intent.putExtra("timer", bundle);
		context.startActivity(intent);
	}

	private TimerTableHelper.Data data;
	private EditText timername;
	private DurationEditText timerduration;
	private static final String TAG = MainActivity.class.getName();
}
