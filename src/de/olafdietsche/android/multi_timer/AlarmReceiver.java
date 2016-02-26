// -*- mode: java -*-
// Copyright (c) 2016 Olaf Dietsche

package de.olafdietsche.android.multi_timer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		long id = intent.getLongExtra("id", -1);
		String title = intent.getStringExtra("name");
		long when = intent.getLongExtra("deadline", -1);
		Notification notification = makeNotification(context, title, when);
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify((int) id, notification);
	}

	public static void scheduleAlarm(final Context context, final TimerTableHelper.Data data) {
		PendingIntent pendingIntent = makeAlarmIntent(context, data);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, data.deadline * 1000, pendingIntent);
	}

	public static void cancelAlarm(final Context context, final TimerTableHelper.Data data) {
		PendingIntent pendingIntent = makeAlarmIntent(context, data);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pendingIntent);
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel((int) data.id);
	}

	private static PendingIntent makeAlarmIntent(final Context context, final TimerTableHelper.Data data) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		Uri uri = Uri.fromParts("android-app", MainActivity.PACKAGE_NAME + "/" + data.id, null);
		intent.setData(uri);
		Bundle bundle = data.toBundle();
		intent.putExtras(bundle);
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	}

	private static PendingIntent makeNotificationIntent(final Context context) {
		Intent contentIntent = new Intent(context, MainActivity.class);
		contentIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		PendingIntent pendingIntent =
			PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		return pendingIntent;
	}

	private static Notification makeNotification(final Context context, final String message, final long when) {
		Notification notification = new Notification(android.R.drawable.ic_lock_idle_alarm, message, when * 1000);
		String title = context.getResources().getString(R.string.app_name);
		PendingIntent pendingIntent = makeNotificationIntent(context);
		notification.setLatestEventInfo(context, title, message, pendingIntent);
		return notification;
	}
}
