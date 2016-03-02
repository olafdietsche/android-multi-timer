// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;

public class BootCompleteReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
		AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... unused) {
				DatabaseHelper db = new DatabaseHelper(context);
				TimerTableHelper helper = new TimerTableHelper(db);
				Cursor cursor = helper.query(TimerTableHelper.Data.projection, null, null, null);
				boolean alarmScheduled = false;
				if (cursor.moveToFirst()) {
					do {
						TimerTableHelper.Data data = new TimerTableHelper.Data(cursor);
						if (data.isRunning()) {
							AlarmReceiver.scheduleAlarm(context, data);
							alarmScheduled = true;
						}
					} while (cursor.moveToNext());
				}

				return alarmScheduled;
			}

			@Override
			protected void onPostExecute(Boolean alarmScheduled) {
				if (alarmScheduled)
					enableReceiver(context);
			}
		};

		task.execute();
	}

	public static void enableReceiver(Context context) {
		setEnabledState(context, PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
	}

	public static void disableReceiver(Context context) {
		setEnabledState(context, PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
	}
	
	private static void setEnabledState(Context context, int state) {
		ComponentName receiver = new ComponentName(context, BootCompleteReceiver.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(receiver, state, PackageManager.DONT_KILL_APP);
	}
}
