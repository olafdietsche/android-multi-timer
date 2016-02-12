// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerEntry {
	public TimerEntry(View view) {
		timername = (TextView) view.findViewById(R.id.timername);
		timerduration = (TextView) view.findViewById(R.id.timerduration);
		getTextColors(timerduration);
		timerstart = (Button) view.findViewById(R.id.timerstart);
		timerstop = (Button) view.findViewById(R.id.timerstop);
		timerpause = (Button) view.findViewById(R.id.timerpause);
		timerresume = (Button) view.findViewById(R.id.timerresume);
	}

	public TimerEntry(View view, TimerTableHelper.Data data) {
		this(view);
		setData(data);
	}

	public TimerTableHelper.Data getData() {
		return data;
	}

	public void setData(TimerTableHelper.Data data) {
		this.data = data;
		timername.setText(data.name);
		if (data.isPausing()) {
			pauseTimer();
		} else if (data.isRunning()) {
			startTimer();
		} else {
			stopTimer();
		}
	}

	public void startTimer() {
		data.startTimer();
		timerstart.setVisibility(View.GONE);
		timerstop.setVisibility(View.VISIBLE);
		timerpause.setVisibility(View.VISIBLE);
		timerresume.setVisibility(View.GONE);
		updateTimer();
	}

	public void stopTimer() {
		data.stopTimer();
		timerstart.setVisibility(View.VISIBLE);
		timerstop.setVisibility(View.GONE);
		timerpause.setVisibility(View.GONE);
		timerresume.setVisibility(View.GONE);
		displayDuration(data.duration);
	}

	public void pauseTimer() {
		data.pauseTimer();
		timerstart.setVisibility(View.GONE);
		timerstop.setVisibility(View.VISIBLE);
		timerpause.setVisibility(View.GONE);
		timerresume.setVisibility(View.VISIBLE);
		displayDuration(data.remaining);
	}

	public void resumeTimer() {
		data.resumeTimer();
		timerstart.setVisibility(View.GONE);
		timerstop.setVisibility(View.VISIBLE);
		timerpause.setVisibility(View.VISIBLE);
		timerresume.setVisibility(View.GONE);
		updateTimer();
	}

	public boolean updateTimer() {
		if (!data.isRunning())
			return false;

		long now = System.currentTimeMillis() / 1000;
		long remaining = data.deadline - now;
		displayDuration(remaining);
		return true;
	}

	private void displayDuration(long duration) {
		long hours = Math.abs(duration / 3600);
		long secs = Math.abs(duration) % 60;
		long mins = (Math.abs(duration) / 60) % 60;
		String s = String.format("%d:%02d:%02d", hours, mins, secs);
		timerduration.setText(s);
		timerduration.setTextColor(duration > 0 ? textColor : overdueColor);
	}

	private static void getTextColors(TextView view) {
		if (textColor != 0)
			return;

		ColorStateList colors = view.getTextColors();
		textColor = colors.getDefaultColor();

		Context context = view.getContext();
		Resources resources = context.getResources();
		overdueColor = resources.getColor(R.color.timer_overdue);
	}

	private TimerTableHelper.Data data;
	private TextView timername, timerduration;
	private Button timerstart, timerstop, timerpause, timerresume;

	private static int textColor = 0, overdueColor;
}
