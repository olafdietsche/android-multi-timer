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
		nameIndex = cursor.getColumnIndex(TimerTableHelper.COLUMN_NAME_NAME);
		durationIndex = cursor.getColumnIndex(TimerTableHelper.COLUMN_NAME_DURATION
*/
	}

	@Override
	public void bindView (View view, Context context, Cursor cursor) {
		TimerEntry te = new TimerEntry(view, overdue_color);
		view.setTag(te);

		String name = cursor.getString(nameIndex);
		te.setName(name);

		String duration = cursor.getString(durationIndex);
		te.setDuration(Long.parseLong(duration));
	}

	private int overdue_color;
	private final int nameIndex = 1, durationIndex = 2;
}
