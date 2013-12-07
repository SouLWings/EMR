package com.example.emr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class medicalhistory extends Activity {
	ListView lv;
	Bundle b;
	JSONObject jObj = null;
	String json = "";
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb = null;
	ArrayList<String> appointments = new ArrayList<String>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicalhistory);
        
        lv = (ListView) findViewById(R.id.listView1);
		b = getIntent().getExtras();
		getAppointments();
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, appointments);
		lv.setAdapter(adapter);
		Log.d("adapter", adapter.toString());

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				openAppointmentIntent(position);
			}

		});
        
    }
    
    public void openAppointmentIntent(int position){
    	try {
			b.putString("date", jArray.getJSONObject(position).getString("date"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(position == 0)
    		b.putString("editable", "true");
    	else
    		b.putString("editable", "false");
    		
		Intent it = new Intent(this, appointment.class);
    	it.putExtras(b);
		startActivity(it);
		finish();
    }
    
    public void getAppointments() {
		String patient_ID = b.getString("patient_ID");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {

			nameValuePairs
					.add(new BasicNameValuePair("patient_ID", patient_ID));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://emrophthalmology.comli.com/getAppointments.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// parse string>>> JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		try {

			int success = jObj.getInt("success");

			if (success == 1) {

				jArray = jObj.getJSONArray("appointments");

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject appointment = jArray.getJSONObject(i);
					appointments.add("Appointment_"+(jArray.length()-i)+" "+appointment.getString("date"));
				}
				if(jArray.length() == 0)
					appointments.add("No medical history.");
			} else {
				appointments.add("No data found.");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emr__main, menu);
		return true;
	}
	
	
	@Override  
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {  
    	case R.id.menusearch:  
    		Intent backSearch = new Intent(this,patientList.class);
    		Bundle tb = new Bundle();
    		tb.putString("doctor_ID", b.getString("doctor_ID"));
    		backSearch.putExtras(tb);
        	startActivity(backSearch);
        	finish();
        return true;
    	
    	case R.id.menulogout:  
        	Intent it = new Intent(Intent.ACTION_MAIN);
        	it.addCategory(Intent.CATEGORY_HOME);
        	it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(it);
        	finish();
        return true;    

    	default:
            return true;    
    	}
	}
}


