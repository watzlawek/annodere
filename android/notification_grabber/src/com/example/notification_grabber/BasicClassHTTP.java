package com.example.notification_grabber;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

public class BasicClassHTTP {

	private Context context;
	public void setContext(Context in_context) {
			this.context = in_context;
	}

	@SuppressWarnings("unchecked")
	public void doBasicClassHTTPRequest() {	
		final RequesterAsyncThread tsk = new RequesterAsyncThread();
		tsk.setRequestserver("http://www.google.de");
		tsk.setFakeHeader("Toller Client");
		tsk.execute();	

		Thread thread1 = new Thread(){
		public void run(){
                try {
                	int timeout = getNetworkConnectionType().equals("WIFI") ? getThreadTimeoutWLANTime() : 
                		getNetworkConnectionType().equals("MOBILE") ? getThreadTimeoutMobileTime() : 0;
                	Log.v("timeout", timeout + "");
                    tsk.get(timeout, TimeUnit.MILLISECONDS);  //set timeout in milisecond

                } catch (Exception e) {
                    tsk.cancel(true);                           
                    Log.v("Thread Canceled", "YES");
                }
            }
        };
        thread1.start();		
	}
	
	public int getThreadTimeoutWLANTime() { //in Milliseconds		
		return 2000;
	}
	
	public int getThreadTimeoutMobileTime() { //in Milliseconds	
		return 	8000;	    	
	}
	
	public Vector<String> machEtwasMitdemInputStream(InputStream stream) {
	 	//Vector<String> vecstr = new Vector<String>();		
	 	Vector<String> output = new Vector<String>();
	 	BufferedReader  br = null;
	 	if (stream != null) {
			br = new BufferedReader(new InputStreamReader(stream));
			String zeile = null; 
			try {
				
				//z.B: einfach Strings in den Vector einfuegen...
				while ((zeile = br.readLine()) != null) {
					output.add(zeile.toString());
				}
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	 	}	
	 	return output;	
	}
	
	public String getNetworkConnectionType() {
		    boolean haveConnectedWifi = false;
		    boolean haveConnectedMobile = false;

		    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		    for (NetworkInfo ni : netInfo) {
		        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
		            if (ni.isConnected())
		                return "WIFI";
		        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
		            if (ni.isConnected())
		            	return "MOBILE";
		    }
		    return "OFFLINE";
	}
	
	public boolean haveNetworkConnection() {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}

	private class RequesterAsyncThread extends AsyncTask<String, Integer, Vector<String>> {

		private String requestserver;
		
		private InputStream resultline;		
		
		private String fake_header = "";

		/**
		 * Request Server festlegen, von dem Daten hier geholt werden sollen: HTTP Server 
		 * @param input the requestserver
		 */
		public void setRequestserver(String input) {
    		this.requestserver = input;
    	}
		/**
		* Custome Header festlegen
		*
		*/
		public void setFakeHeader(String str) {
			this.fake_header = str;
		}
		
		public InputStream getResultline() {
			return resultline;
		}
    	
    	/**
    	 * Getter for requestserver    	 * 
    	 * @return requestserver The url of server.
    	 */
    	public String getRequestserver() {
    		return this.requestserver;
    	}
		
    	 private String convertInputStreamToString(InputStream inputStream) throws IOException{
    	        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
    	        String line = "";
    	        String result = "";
    	        while((line = bufferedReader.readLine()) != null)
    	            result += line;

    	        //inputStream.close();
    	        return result;

    	    }
    	
    	
		public Vector<String> requester() {    	  
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);	 	   
    	    InputStream inputStream = null;

    	    try {	  	
    	    	Log.v("ThreadState", "running");   
    	    	
    	    	/*HttpParams params = new BasicHttpParams();
    	    	params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    	    	params.setParameter(CoreProtocolPNames.USER_AGENT,  fake_header);
    	    	HttpClient httpclient = new DefaultHttpClient(params);    
    			HttpResponse httpResponse = httpclient.execute(new HttpGet(requestserver));
    			inputStream = httpResponse.getEntity().getContent();   */
    	    	
    	    	URL url = new URL(requestserver);
    	    	
    	    	if (haveNetworkConnection()) {    	    		    	    	
    	    	
    	    		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();	    	    		
    	    		urlConnection.setUseCaches(false);
    	    		urlConnection.setDefaultUseCaches(false);
    	    		//urlConnection.connect(); nicht mehr noetig hier

    	    		urlConnection.setRequestProperty("User-Agent", fake_header);
    	    		inputStream = new BufferedInputStream(urlConnection.getInputStream());
    	    		
    	    		//hier eigenen Code einsetzen
    	    		Vector<String> lines = machEtwasMitdemInputStream(inputStream);					
					//hier eigenen Cide einsetzen
    	    		
    	    		
    	    		urlConnection.disconnect();    			
    		
    	    		return lines;
    	    	}
    	    	return null;
    	         
    	    } 
    	    catch (ClientProtocolException e) {
    	    	Log.v("ClientProtocolException", e.getMessage());
    	    } 
    	    catch (IOException e) {
    	    	Log.v("IOException", e.getMessage());    	 
    	    }  	    
    	    return null;
    	}		
    	
    	@Override
    	protected void onProgressUpdate(Integer... progress) {
    		super.onProgressUpdate(progress);    		
    	}    	


		@Override
		protected Vector<String> doInBackground(String... params) {						
			return requester();
		
		}
		
		@Override
    	protected void onPostExecute(Vector<String> result) {	
    		if (result != null) { // z.B.: Inhalte nach Ausführung ausgeben		
    			String concatst = "";
    			for(int i = 0; i < result.size(); i++)
    				concatst += result.get(i) + "\n";   			
 
				Log.v("Inhalt-GET:", concatst);
    		} 
    	
		}
    	
	
	}
}