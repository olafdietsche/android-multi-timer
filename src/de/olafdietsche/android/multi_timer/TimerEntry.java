// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import java.text.SimpleDateFormat;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class TimerEntry {
	public TimerEntry(View view) {
		timername = (TextView) view.findViewById(R.id.timername);
		timerduration = (TextView) view.findViewById(R.id.timerduration);
		timerstart = (Button) view.findViewById(R.id.timerstart);
		timerstop = (Button) view.findViewById(R.id.timerstop);
		timerpause = (Button) view.findViewById(R.id.timerpause);
		timerresume = (Button) view.findViewById(R.id.timerresume);
	}

	public void setName(String name) {
		timername.setText(name);
	}

	public void setDuration(long duration) {
		this.duration = duration;
		displayDuration(duration);
	}

	public void setRemaining(long remaining) {
		this.remaining = remaining;
		displayDuration(remaining);
	}

	public void startTimer() {
		timerstart.setVisibility(View.GONE);
		timerstop.setVisibility(View.VISIBLE);
		timerpause.setVisibility(View.VISIBLE);
	}

	public void stopTimer() {
		timerstart.setVisibility(View.VISIBLE);
		timerstop.setVisibility(View.GONE);
		timerpause.setVisibility(View.GONE);
		timerresume.setVisibility(View.GONE);
	}

	public void pauseTimer() {
		timerpause.setVisibility(View.GONE);
		timerresume.setVisibility(View.VISIBLE);
	}

	public void resumeTimer() {
		timerpause.setVisibility(View.VISIBLE);
		timerresume.setVisibility(View.GONE);
	}

	public void displayDuration(long duration) {
		long secs = duration % 60;
		long mins = (duration / 60) % 60;
		long hours = duration / 3600;
		String s = String.format("%d:%02d:%02d", hours, mins, secs);
		timerduration.setText(s);
	}

	private long duration, remaining;
	private TextView timername, timerduration;
	private Button timerstart, timerstop, timerpause, timerresume;
}
