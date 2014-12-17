package com.example.notification_grabber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * NotificationListenerService
 * - Vereinigung der Implementierung von onNotificationPosted fuer Jellybean und Kitkat 
 * - Noetig da es nur eine Intanz des NotificationListenerServices geben kann
 * - Fallunterscheidung sollte das Problem loesen
 **/

public class NotificationGrabberService extends NotificationListenerService {

	Notification_data noti;


	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		
		/*Extrahiert aus der StatusBarNotification die Notification. In deren Subklasse Bundle liegen alle Informationen, 
		 die man mit den entsprechenden Keys "EXTRA_TITLE", "EXTRA_TEXT", usw. holen kann. Diese werden dann an einen
		 Intent gehangen und verschickt.*/
		
		// API 19 and above
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {			
			System.out.println("KK-Version of onNotificationPosted() executed");
			
			Notification mNotification = sbn.getNotification();
			if (mNotification != null) {
				System.out.println("Notification posted");
				Bundle extras = mNotification.extras;
				String notificationTitle = sbn.getPackageName() + ": " + extras
						.getString(Notification.EXTRA_TITLE);
				// Bitmap notificationIcon = ((Bitmap) extras
				// .getParcelable(Notification.EXTRA_SMALL_ICON));
				String notificationText = extras.getString(Notification.EXTRA_TEXT);
				noti = new Notification_data(notificationTitle,notificationText, null);
//				Intent intent = new Intent(MainActivity.INTENT_ACTION_NOTIFICATION);
//				intent.putExtra("title", notificationTitle);
//				intent.putExtra("text", notificationText);
//				System.out.println("Title in grabber API>18: " + notificationTitle);
//				System.out.println("Text in grabber API>18: " + notificationText);
				// intent.putExtra("icon", bitmap_to_bytearray(notificationIcon));
//				sendBroadcast(intent);
				
				//Log.v("Notification_title", sbn.getTag() );
				//Log.v("Notification_text", sbn.getNotification().tickerText.toString());
				
				//this.getApplication().get
				
				MainActivity.receiveNoti(noti);				
				}			
		}
		
		/*Holt aus der StatusBarNotification die Notification und extrahiert deren Infos mit Reflections(?). Diese Infos
		 werden an einen Intent gehangen und verschickt.
		 http://stackoverflow.com/questions/9292032/extract-notification-text-from-parcelable-contentview-or-contentintent
		 Hier ist der Link, wo ich den try-catch-Block her habe. Ist TomTasche's Antwort, die darunter liefert dazu noch eine
		 verständliche Erklärung.*/
		// API 18
		else if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {			
			System.out.println("JB-Version of onNotificationPosted() executed");
			
			Notification mNotification = sbn.getNotification();
			if (mNotification != null) {
				System.out.println("Notification posted");
				RemoteViews views = mNotification.contentView;
				Class secretClass = views.getClass();

				try {
					Map<Integer, String> text = new HashMap<Integer, String>();

					Field outerFields[] = secretClass.getDeclaredFields();
					for (int i = 0; i < outerFields.length; i++) {
						if (!outerFields[i].getName().equals("mActions"))
							continue;

						outerFields[i].setAccessible(true);

						@SuppressWarnings("unchecked")
						ArrayList<Object> actions = (ArrayList<Object>) outerFields[i]
								.get(views);
						for (Object action : actions) {
							Field innerFields[] = action.getClass()
									.getDeclaredFields();

							Object value = null;
							Integer type = null;
							Integer viewId = null;
							for (Field field : innerFields) {
								field.setAccessible(true);
								if (field.getName().equals("value")) {
									value = field.get(action);
								} else if (field.getName().equals("type")) {
									type = field.getInt(action);
								} else if (field.getName().equals("viewId")) {
									viewId = field.getInt(action);
								}
							}

							if (type == 9 || type == 10) {
								text.put(viewId, value.toString());
							}
						}
						noti = new Notification_data(text.get(16908310),text.get(16908358), null);
//						Intent intent = new Intent(
//								MainActivity.INTENT_ACTION_NOTIFICATION);
//						intent.putExtra("title", text.get(16908310));
//						intent.putExtra("text", text.get(16908358));
						// System.out.println("title is: " + text.get(16908310));
						// System.out.println("info is: " + text.get(16909082));
						// System.out.println("text is: " + text.get(16908358));
						System.out.println("Title in grabber API18: "
								+ text.get(16908310));
						System.out.println("Text in grabber API18: "
								+ text.get(16908358));
//						sendBroadcast(intent);
						MainActivity.receiveNoti(noti);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification arg0) {
		// TODO Auto-generated method stub

	}
}
