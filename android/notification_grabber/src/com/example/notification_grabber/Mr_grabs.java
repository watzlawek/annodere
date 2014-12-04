package com.example.notification_grabber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RemoteViews;

public class Mr_grabs extends AccessibilityService {

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			Notification notification = (Notification) event
					.getParcelableData();
			System.out.println("Notification posted");
			RemoteViews views = notification.contentView;
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
					Intent intent = new Intent(
							MainActivity.INTENT_ACTION_NOTIFICATION);
					intent.putExtra("title", text.get(16908310));
					intent.putExtra("text", text.get(16908358));
					// System.out.println("title is: " + text.get(16908310));
					// System.out.println("info is: " + text.get(16909082));
					// System.out.println("text is: " + text.get(16908358));
					System.out.println("Title in grabber API<18: "
							+ text.get(16908310));
					System.out.println("Text in grabber API<18: "
							+ text.get(16908358));
					sendBroadcast(intent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

}
