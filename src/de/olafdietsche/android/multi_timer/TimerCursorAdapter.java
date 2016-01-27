// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class TimerCursorAdapter extends ResourceCursorAdapter {
	public TimerCursorAdapter(Context context, Cursor cursor) {
		super(context, R.layout.timer_line, cursor, false);
/* FIXME
		timernameIndex = cursor.getColumnIndex(TimerTableHelper.COLUMN_NAME_NAME);
		timerdurationIndex = cursor.getColumnIndex(TimerTableHelper.COLUMN_NAME_DURATION
*/
	}

	@Override
	public void bindView (View view, Context context, Cursor cursor) {
		TextView tv;
		String timername = cursor.getString(timernameIndex);
		tv = (TextView) view.findViewById(R.id.timername);
		tv.setText(timername);

		String timerduration = cursor.getString(timerdurationIndex);
		tv = (TextView) view.findViewById(R.id.timerduration);
		tv.setText(timerduration);
	}

	private final int timernameIndex = 1, timerdurationIndex = 2;
}
