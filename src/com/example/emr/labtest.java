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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class labtest extends Activity {
	ListView lv;
	Bundle b;
	JSONObject jObj = null;
	String json = "";
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb = null;
	ArrayList<String> entries = new ArrayList<String>();
	private static final String googleDocsUrl = "http://docs.google.com/viewer?url=";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labtest);

		lv = (ListView) findViewById(R.id.listView1);
		b = getIntent().getExtras();
		getLabtests();
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, entries);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {

				JSONObject labtest;
				try {
					labtest = jArray.getJSONObject(position);
					if (labtest.getString("filetype").equalsIgnoreCase("pdf")) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(
								Uri.parse(googleDocsUrl
										+ labtest.getString("filedirc")),
								"text/html");
						startActivity(intent);
					} else if (labtest.getString("filetype").equalsIgnoreCase(
							"image")) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(labtest.getString("filedirc"))));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}

	public void getLabtests() {
		String patient_ID = b.getString("patient_ID");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {

			nameValuePairs
					.add(new BasicNameValuePair("patient_ID", patient_ID));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://emrophthalmology.comli.com/getPatientLabtest.php");
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

				jArray = jObj.getJSONArray("labtest");

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject labtest = jArray.getJSONObject(i);
					entries.add(labtest.getString("filename") + "\n"
							+ labtest.getString("time"));
				}
			} else {
				entries.add("No data found.");
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
