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
	private int NOT_ID = 1001;
	private int color = Color.BLUE;
	private Activity activity;
	private NotificationManager notificationManager;

	public Notification(Activity act) {
		activity = act;
		notificationManager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void setId(int id) {
		NOT_ID = id;
	}

	public void cancel() {
		notificationManager.cancel(NOT_ID);
	}

	public int getId() {
		return NOT_ID;
	}
	
	public void setColor(int c) {
		color = c;
	}
	
	public int getColor() {
		return color;
	}

	public void sendNotification(String header, String text) {
		sendNotification(header, text, color, false);
	}

	public void sendNotification(String header, String text, int ledColor) {
		sendNotification(header, text, ledColor, false);
	}
	
	public void sendNotification(String header, String text, boolean ongoing) {
		sendNotification(header, text, color, ongoing);
	}

	public void sendNotification(String header, String text, int ledColor, boolean ongoing) {
		Uri sound = null;
		try {
			sound = Uri.parse("android.resource://" + activity.getPackageName() + "/" + R.raw.done);  
		} catch (Exception e) {
			sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		}
		final String channelId = "channel-" + NOT_ID;
		final String channelName = activity.getString(R.string.channel_test);

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

		Intent intent = new Intent(activity, activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
		Notification.Builder notBuilder;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			notBuilder = new Notification.Builder(activity, channelId);
		} else {
			notBuilder = new Notification.Builder(activity);
		}
        notBuilder.setTicker(header)
            .setContentTitle(header)
            .setContentText(text)
			.setWhen(System.currentTimeMillis())
			.setShowWhen(true)
			.setOngoing(ongoing)
			.setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.notification))
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
		notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;
		if (!ongoing) {
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
		}
		notificationManager.notify(NOT_ID, notification);
	}
}
