// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class TimerCursorAdapter extends ResourceCursorAdapter {
	public TimerCursorAdapter(Context context, Cursor cursor) {
		super(context, R.layout.timer_line, cursor, false);
		Resources resources = context.getResources();
		overdue_color = resources.getColor(R.color.timer_overdue);
/* FIXME
		timernameIndex = cursor.getColumnIndex(TimerTableHelper.COLUMN_NAME_NAME);
		timerdurationIndex = cursor.getColumnIndex(TimerTableHelper.COLUMN_NAME_DURATION
*/
	}

	@Override
	public void bindView (View view, Context context, Cursor cursor) {
		TimerEntry te = new TimerEntry(view, overdue_color);
		view.setTag(te);

		String timername = cursor.getString(timernameIndex);
		te.setName(timername);

		String timerduration = cursor.getString(timerdurationIndex);
		te.setDuration(Long.parseLong(timerduration));
	}

	private int overdue_color;
	private final int timernameIndex = 1, timerdurationIndex = 2;
}
