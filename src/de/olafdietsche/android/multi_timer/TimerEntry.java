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
		running = true;
	}

	public void stopTimer() {
		timerstart.setVisibility(View.VISIBLE);
		timerstop.setVisibility(View.GONE);
		timerpause.setVisibility(View.GONE);
		timerresume.setVisibility(View.GONE);
		running = false;
	}

	public void pauseTimer() {
		timerpause.setVisibility(View.GONE);
		timerresume.setVisibility(View.VISIBLE);
		pausing = true;
	}

	public void resumeTimer() {
		timerpause.setVisibility(View.VISIBLE);
		timerresume.setVisibility(View.GONE);
		pausing = false;
	}

	public void displayDuration(long duration) {
		long hours = duration / 3600;
		long secs = Math.abs(duration) % 60;
		long mins = (Math.abs(duration) / 60) % 60;
		String s = String.format("%d:%02d:%02d", hours, mins, secs);
		timerduration.setText(s);
	}

	private long duration, remaining;
	private boolean running, pausing;
	private TextView timername, timerduration;
	private Button timerstart, timerstop, timerpause, timerresume;
}
