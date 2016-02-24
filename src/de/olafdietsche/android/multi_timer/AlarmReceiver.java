// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
	}

	public static void scheduleAlarm(final Context context, final TimerTableHelper.Data data) {
		PendingIntent pendingIntent = makePendingIntent(context, data);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, data.deadline * 1000, pendingIntent);
	}

	public static void cancelAlarm(final Context context, final TimerTableHelper.Data data) {
		PendingIntent pendingIntent = makePendingIntent(context, data);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pendingIntent);
	}

	private static PendingIntent makePendingIntent(final Context context, final TimerTableHelper.Data data) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		Uri uri = Uri.fromParts("android-app", MainActivity.PACKAGE_NAME + "/" + data.id, null);
		intent.setData(uri);
		Bundle bundle = data.toBundle();
		intent.putExtras(bundle);
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	}
}
