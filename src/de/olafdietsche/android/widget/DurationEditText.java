// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import de.olafdietsche.android.multi_timer.R;

public class DurationEditText extends LinearLayout {
	public DurationEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_duration, this, true);
		hoursView = (EditText) getChildAt(0);
		minutesView = (EditText) getChildAt(1);
		secondsView = (EditText) getChildAt(2);
	}

	public DurationEditText(Context context) {
		this(context, null);
	}

	public long getDuration() {
		long duration = 0;
		String s;
		s = hoursView.getText().toString();
		if (s.length() > 0)
			duration += Long.parseLong(s) * 3600;

		s = minutesView.getText().toString();
		if (s.length() > 0)
			duration += Long.parseLong(s) * 60;

		s = secondsView.getText().toString();
		if (s.length() > 0)
			duration += Long.parseLong(s);

		return duration;
	}

	public void setDuration(long duration) {
		long hours = duration / 3600;
		hoursView.setText(Long.toString(hours));
		long minutes = (duration % 3600) / 60;
		minutesView.setText(Long.toString(minutes));
		long seconds = duration % 60;
		secondsView.setText(Long.toString(seconds));
	}

	private EditText hoursView, minutesView, secondsView;
}
