package ru.maximoff.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import ru.maximoff.notifications.R;

public class Notification {
	private static final int NOT_ID = 1001;

	public static void sendNotification(Activity act, String header, String text, int ledColor) {
		Uri sound = null;
		try {
			sound = Uri.parse("android.resource://" + act.getPackageName() + "/" + R.raw.done);  
		} catch (Exception e) {
			sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		}
		final NotificationManager notificationManager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
		final String channelId = "channel-" + NOT_ID;
		final String channelName = act.getString(R.string.channel_test);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
			mChannel.enableLights(true);
			mChannel.enableVibration(true);
			mChannel.setLightColor(Color.BLUE);
			mChannel.setDescription(channelName);
			if (sound != null) {
				AudioAttributes attributes = new AudioAttributes.Builder()
					.setUsage(AudioAttributes.USAGE_NOTIFICATION)
					.build();
				mChannel.setSound(sound, attributes);
			}
			notificationManager.createNotificationChannel(mChannel);
		}

		Intent intent = new Intent(act, act.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(act, 0, intent, 0);
		Notification.Builder notBuilder = new Notification.Builder(act, channelId);
        notBuilder.setTicker(header)
            .setContentTitle(header)
            .setContentText(text)
			.setWhen(System.currentTimeMillis())
			.setShowWhen(true)
			.setLargeIcon(BitmapFactory.decodeResource(act.getResources(), R.drawable.notification))
            .setSmallIcon(R.drawable.notification)
			.setContentIntent(pendingIntent);
		Notification notification = notBuilder.build();
		notification.ledARGB = ledColor;
		notification.ledOffMS = 0;
		notification.ledOnMS = 1;
		if (sound != null) {
			notification.sound = sound;
			notification.defaults = Notification.DEFAULT_VIBRATE;
		} else {
			notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		}

		notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(NOT_ID, notification);
	}
}
