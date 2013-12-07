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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class appointment extends Activity {
	TextView tvDate, tvID;
	EditText etDisplay;
	Button b1, b2, b3, b4,b5;
	Bundle b;
	JSONObject jObj = null;
	String json = "";
	JSONArray jArray;
	String result = null;
	String eyeexam="", eyedisease="", order="";
	InputStream is = null;
	StringBuilder sb = null;
	ArrayList<String> entries = new ArrayList<String>();
	ArrayList<String> eyeexamID = new ArrayList<String>();
	ArrayList<String> eyediseaseID = new ArrayList<String>();
	ArrayList<String> orderID = new ArrayList<String>();
	int currentPage = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appointment);

		b = getIntent().getExtras();
		Log.d("ori bundle", b.toString());
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvDate.setText(b.getString("date"));
		tvID = (TextView) findViewById(R.id.tvID);
		tvID.setText("Patient ID: " + b.getString("patient_ID"));
		etDisplay = (EditText) findViewById(R.id.etDisplay);
		b1 = (Button) findViewById(R.id.button1);
		b3 = (Button) findViewById(R.id.button2);
		b2 = (Button) findViewById(R.id.button3);
		b4 = (Button) findViewById(R.id.button4);
		b5 = (Button) findViewById(R.id.button5);
		getAppointment();
		etDisplay.setText(eyeexam);
		if(b.getString("editable").equals("false"))
		{
			b4.setEnabled(false);
		}
		b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		etDisplay.setText(eyeexam);
        		currentPage = 1;
        		if(!eyeexam.equals("No data found.") && b.getString("editable").equals("true"))
        			b4.setEnabled(true);
        		else
        			b4.setEnabled(false);
            }
        });
		b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		etDisplay.setText(eyedisease);
        		currentPage = 2;
        		if(!eyedisease.equals("No data found.") && b.getString("editable").equals("true"))
        			b4.setEnabled(true);
        		else
        			b4.setEnabled(false);
            }
        });
		b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		etDisplay.setText(order);
        		Log.d("order String",order);
        		currentPage = 3;
        		if(!order.equals("No data found.") && b.getString("editable").equals("true"))
        			b4.setEnabled(true);
        		else
        			b4.setEnabled(false);
            }
        });
		b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(currentPage == 1)
            		openEditeyetest();
            	else if(currentPage == 2)
            		openEditeyedisease();
            	else if(currentPage == 3)
            		openEditorder();
            }
        });
		b5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				backMH();
				
			}
		});
	}

	protected void openEditorder() {
		Intent it = new Intent(this, editorder.class);
		b.putString("id", orderID.get(0));
		it.putExtras(b);
		startActivity(it);
		finish();
	}

	protected void openEditeyedisease() {
		Intent it = new Intent(this, editclinicaldocument.class);
		b.putString("id", eyediseaseID.get(0));
		it.putExtras(b);
		startActivity(it);
		finish();
	}

	protected void openEditeyetest() {
		Intent it = new Intent(this, editeyeexam.class);
		b.putString("id", eyeexamID.get(0));
		it.putExtras(b);
		startActivity(it);
		finish();
	}
	
	protected void backMH() {
		Log.d("ori bundle", b.toString());
		Intent modulepage = new Intent(this, modulepage.class);
		Bundle tb = new Bundle();
		tb.putString("doctor_ID", b.getString("doctor_ID"));
		tb.putString("patient_ID", b.getString("patient_ID"));
		modulepage.putExtras(tb);
		Log.d("temp bundle", tb.toString());
    	startActivity(modulepage);
	}

	public void getAppointment() {
		String patient_ID = b.getString("patient_ID");
		String date = b.getString("date");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {

			nameValuePairs
					.add(new BasicNameValuePair("patient_ID", patient_ID));
			nameValuePairs.add(new BasicNameValuePair("date", date));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://emrophthalmology.comli.com/getAppointmentDetails.php");
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

		Log.d("jObj", jObj.toString());
		try {

			int success = jObj.getInt("success");
			if (success == 1) {
				jArray = jObj.getJSONArray("eyeexam");

				for (int i = 0; i < jArray.length(); i++) {
					eyeexam = eyeexam + jArray.getJSONObject(i).getString("data") + "\n\n";
					eyeexamID.add(jArray.getJSONObject(i).getString("id"));
				}
			} else {
				eyeexam = "No data found.";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			eyeexamID.add("0");
		}

		try {

			int success = jObj.getInt("success");
			Log.d("jObj", jObj.toString());
			if (success == 1) {
				jArray = jObj.getJSONArray("orders");

				for (int i = 0; i < jArray.length(); i++) {
					order = order + jArray.getJSONObject(i).getString("data") + "\n\n";
					orderID.add(jArray.getJSONObject(i).getString("id"));
				}
			} else {
				order = "No data found.";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			orderID.add("0");
		}

		try {

			int success = jObj.getInt("success");
			Log.d("jObj", jObj.toString());
			if (success == 1) {
				
				jArray = jObj.getJSONArray("eyedisease");

				for (int i = 0; i < jArray.length(); i++) {
					eyedisease = eyedisease + jArray.getJSONObject(i).getString("data") + "\n\n";
					eyediseaseID.add(jArray.getJSONObject(i).getString("id"));
				}
			} else {
				eyedisease = "No data found.";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			eyediseaseID.add("0");
		}

		if(eyeexam.equals(""))
			eyeexam = "No data found.";
		if(order.equals(""))
			order = "No data found.";
		else
			order = order.replace(';', ',');
		if(eyedisease.equals(""))
			eyedisease = "No data found.";
		else
			eyedisease = eyedisease.replace(';',',');
		
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
