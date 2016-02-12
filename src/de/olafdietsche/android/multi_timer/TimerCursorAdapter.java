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
	}

	@Override
	public void bindView (View view, Context context, Cursor cursor) {
		TimerTableHelper.Data data = new TimerTableHelper.Data(cursor);
		TimerEntry te = (TimerEntry) view.getTag();
		if (te != null) {
			te.setData(data);
		} else {
			te = new TimerEntry(view, data);
			view.setTag(te);
		}
	}

}
