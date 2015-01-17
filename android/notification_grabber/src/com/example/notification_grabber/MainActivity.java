package com.example.notification_grabber;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
//import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

//	protected MyReceiver mReceiver = new MyReceiver();
	public static final String INTENT_ACTION_NOTIFICATION = "com.example.notification_grabber";
	private static int noti_counter = 0;
	
	private static int noti_counter_internal = -1;

	protected static TextView title1;
	protected static TextView title2;
	protected static TextView title3;
	protected static TextView text1;
	protected static TextView text2;
	protected static TextView text3;
	
	protected static String notification_title = "";
	protected static String notification_text = "";
	
	protected static TextView texted_noti_counter;
	
	protected Handler handler;	

	// protected ImageView icon1, icon2, icon3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Starte Runnable Thread fuer die UI-Aktualisierung
		this.handler = new Handler();
    	this.handler.postDelayed(runnable,500);
    	
		/*Checkt die Version und benutzt dann die dementsprechende Klasse zum Extrahieren der Notifications*/
		/*// API 19 and above
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			Mr_grabsKK grabber = new Mr_grabsKK();
			System.out.println("____________________Mr_grabsKK initialised");
		}
		// API 18
		else if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			Mr_grabsJB grabber = new Mr_grabsJB();
			System.out.println("____________________Mr_grabsJB initialised");
		}
		// API 17 and below*/
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			Mr_grabs grabber = new Mr_grabs();
			System.out.println("____________________Mr_grabs initialised");
		}
		//TEST FOR GITHUB
		title1 = (TextView) findViewById(R.id.noti_title1);
		title2 = (TextView) findViewById(R.id.noti_title2);
		title3 = (TextView) findViewById(R.id.noti_title3);
		text1 = (TextView) findViewById(R.id.noti_text1);
		text2 = (TextView) findViewById(R.id.noti_text2);
		text3 = (TextView) findViewById(R.id.noti_text3);
		texted_noti_counter = (TextView) findViewById(R.id.notification_counter);
		// icon1 = (ImageView) findViewById(R.id.noti_icon1);
		// icon2 = (ImageView) findViewById(R.id.noti_icon2);
		// icon3 = (ImageView) findViewById(R.id.noti_icon3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*Methode um schnell zu den Optionen zu kommen und den NotificationListener zu erlauben.*/
	public void unlock(View view) {
		
		
		
		/*noti_counter = 0;
		noti_counter_internal = 0;

		Intent intent = new Intent(
				"android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
		startActivity(intent);*/
		
		
		BasicClassHTTP bsk = new BasicClassHTTP();
		bsk.doBasicClassHTTPRequest();
		bsk.setContext(this);
		
		if (bsk.haveNetworkConnection()) {
			bsk.doBasicClassHTTPRequest();    	

    	}
		
	}

	
	private final Runnable runnable = new Runnable() {
	    public void run()   {	    	
	    		handler.postDelayed(runnable,500);
	    		updateUI();
	    }
	};
	
	protected void updateUI() {
		
		Integer tmp = new Integer(noti_counter);
		texted_noti_counter.setText("Notifications: "+ tmp.toString());	
		
		switch (noti_counter_internal % 3) {
		case 0:
			title1.setText(notification_title);
			text1.setText(notification_text);
			// if(notificationIcon!=null) {
			// icon1.setImageBitmap(notificationIcon);
			// }
			break;
		case 1:
			title2.setText(notification_title);
			text2.setText(notification_text);
			// if(notificationIcon!=null) {
			// icon2.setImageBitmap(notificationIcon);
			// }
			break;
		case 2:
			title3.setText(notification_title);
			text3.setText(notification_text);
			// if(notificationIcon!=null) {
			// icon3.setImageBitmap(notificationIcon);
			// }
			noti_counter_internal = -1; // Reset!
			break;

		default:
			break;
		}
		
	
		
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUI();
//		if (mReceiver == null)
//			mReceiver = new MyReceiver();
//		registerReceiver(mReceiver,
//				new IntentFilter(INTENT_ACTION_NOTIFICATION));
	}

	@Override
	protected void onPause() {
		super.onPause();
//		unregisterReceiver(mReceiver);
	}
	
	public static void receiveNoti(Notification_data noti) {
		notification_title = noti.getTitle();
		notification_text = noti.getText();
		noti_counter++;		
		noti_counter_internal++;
	}
	
	

	/*Receiver zum Erhalten von Broadcasts, die die jeweilige Mr_grabs Klasse schickt. In dem Intent des Broadcasts
	 sind die Strings mit den relevanten Informationen enthalten und mit den Keys "title" und "text" abrufbar.*/
//	public class MyReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			System.out.println("Intent received");
//			String notificationTitle = null;
//			String notificationText = null;
//			// Bitmap notificationIcon = null;
//			if (intent != null) {
//				notificationTitle = intent.getStringExtra("title");
//				notificationText = intent.getStringExtra("text");
//				System.out.println("Title in Main: " + notificationTitle);
//				System.out.println("Text in Main: " + notificationText);
//				// notificationIcon =
//				// bytearray_to_bitmap(intent.getByteArrayExtra("icon"));
//				switch (noti_counter % 3) {
//				case 0:
//					title1.setText(notificationTitle);
//					text1.setText(notificationText);
//					// if(notificationIcon!=null) {
//					// icon1.setImageBitmap(notificationIcon);
//					// }
//					break;
//				case 1:
//					title2.setText(notificationTitle);
//					text2.setText(notificationText);
//					// if(notificationIcon!=null) {
//					// icon2.setImageBitmap(notificationIcon);
//					// }
//					break;
//				case 2:
//					title3.setText(notificationTitle);
//					text3.setText(notificationText);
//					// if(notificationIcon!=null) {
//					// icon3.setImageBitmap(notificationIcon);
//					// }
//					break;
//
//				default:
//					break;
//				}
//				noti_counter++;
//			}
//
//		}

		// private Bitmap bytearray_to_bitmap(byte[] byteArray) {
		// // byte[] byteArray = getIntent().getByteArrayExtra("image");
		// Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0,
		// byteArray.length);
		// return bmp;
		// }
//	}
}
