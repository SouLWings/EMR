package com.example.emr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class eyeexam extends Activity {
	CheckBox xray, urinetest, bloodtest, head, leg, hand;
	Button btnorder, btntable, btnstarttest;
	InputStream is = null;
	Spinner charttype,colorblindness;
	EditText l4, r4, slitlamp;
	Bundle b;
	AutoCompleteTextView l1, l2, l3, r1, r2, r3, b1, b2, leye1, reye1, bino;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyeexam);

		b = getIntent().getExtras();
		l1 = (AutoCompleteTextView) findViewById(R.id.l1);
		l2 = (AutoCompleteTextView) findViewById(R.id.l2);
		l3 = (AutoCompleteTextView) findViewById(R.id.l3);
		l4 = (EditText) findViewById(R.id.l4);
		b1 = (AutoCompleteTextView) findViewById(R.id.b1);
		b2 = (AutoCompleteTextView) findViewById(R.id.b2);
		r1 = (AutoCompleteTextView) findViewById(R.id.r1);
		r2 = (AutoCompleteTextView) findViewById(R.id.r2);
		r3 = (AutoCompleteTextView) findViewById(R.id.r3);
		r4 = (EditText) findViewById(R.id.r4);
		leye1 = (AutoCompleteTextView) findViewById(R.id.leye1);
		reye1 = (AutoCompleteTextView) findViewById(R.id.reye1);
		bino = (AutoCompleteTextView) findViewById(R.id.bino);
		slitlamp = (EditText) findViewById(R.id.slitlamp);
		
		charttype = (Spinner) findViewById(R.id.charttypeSpinner);
		colorblindness = (Spinner) findViewById(R.id.colorblindness);


		ArrayAdapter<String> contrastAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, contrastSensitive);
		l1.setThreshold(1);
		l2.setThreshold(1);
		l3.setThreshold(1);
		r1.setThreshold(1);
		r2.setThreshold(1);
		r3.setThreshold(1);
		b1.setThreshold(1);
		b2.setThreshold(1);
		l1.setAdapter(contrastAdapter);
		l2.setAdapter(contrastAdapter);
		l3.setAdapter(contrastAdapter);
		r1.setAdapter(contrastAdapter);
		r2.setAdapter(contrastAdapter);
		r3.setAdapter(contrastAdapter);
		b1.setAdapter(contrastAdapter);
		b2.setAdapter(contrastAdapter);

		ArrayAdapter<String> chartadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, chartTypeArray);
		ArrayAdapter<String> coloradapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, bindnessArray);
		charttype.setAdapter(chartadapter);
		colorblindness.setAdapter(coloradapter);
		btnorder = (Button) findViewById(R.id.button1);
		btntable = (Button) findViewById(R.id.btntable);
		btnstarttest = (Button) findViewById(R.id.btnstarttest);

    	
		btntable.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startTable();
			}
		});
		btnstarttest.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startIshihara();
			}
		});
		btnorder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addOrder();
			}
		});

		charttype.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (charttype.getSelectedItem().equals("logMAR chart")) {
					setScaleAdapter(logmar);
				} else if (charttype.getSelectedItem().equals("Jaeger chart")) {
					setScaleAdapter(jaeger);
				} else {
					setScaleAdapter(others);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				charttype.setSelection(0);
			}

		});
	}

	protected void startIshihara() {
		Intent it = new Intent(this, IshiharaTest.class);
		startActivity(it);
	}

	protected void startTable() {
		Intent it = new Intent(this, Acuity_Scale_table.class);
		startActivity(it);
	}

	protected void setScaleAdapter(String[] scale) {
		leye1.setText("");
		reye1.setText("");
		bino.setText("");
		ArrayAdapter<String> scaleadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, scale);
		leye1.setThreshold(1);
		reye1.setThreshold(1);
		bino.setThreshold(1);
		leye1.setAdapter(scaleadapter);
		reye1.setAdapter(scaleadapter);
		bino.setAdapter(scaleadapter);
	}

	public void addOrder() {
		if(leye1.getText().toString().equals("") || bino.getText().toString().equals("") || reye1.getText().toString().equals("")){
	    	final Toast t = Toast.makeText(this, "Data not saved. \nPlease fill in all acuity fields.", Toast.LENGTH_SHORT);
	        t.show();
	    	Handler h = new Handler();
	        h.postDelayed(new Runnable() {
	           @Override
	           public void run() {
	               t.cancel(); 
		           }
		    }, 2000);
	        return;
		}
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// http post to php

		nameValuePairs
				.add(new BasicNameValuePair("l1", l1.getText().toString()));
		nameValuePairs
				.add(new BasicNameValuePair("l2", l2.getText().toString()));
		nameValuePairs
				.add(new BasicNameValuePair("l3", l3.getText().toString()));
		nameValuePairs
				.add(new BasicNameValuePair("l4", l4.getText().toString()));
		nameValuePairs
				.add(new BasicNameValuePair("b1", b1.getText().toString()));
		nameValuePairs
				.add(new BasicNameValuePair("b2", b2.getText().toString()));
		nameValuePairs
				.add(new BasicNameValuePair("r1", r1.getText().toString()));
		nameValuePairs
				.add(new BasicNameValuePair("r2", r2.getText().toString()));
		nameValuePairs
				.add(new BasicNameValuePair("r3", r3.getText().toString()));
		nameValuePairs
				.add(new BasicNameValuePair("r4", r4.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("bino", bino.getText()
				.toString()));
		nameValuePairs.add(new BasicNameValuePair("leye1", leye1.getText()
				.toString()));
		nameValuePairs.add(new BasicNameValuePair("reye1", reye1.getText()
				.toString()));
		nameValuePairs.add(new BasicNameValuePair("slitlamp", slitlamp
				.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("colorblindness", colorblindness
				.getSelectedItem().toString()));
		nameValuePairs.add(new BasicNameValuePair("charttype", charttype
				.getSelectedItem().toString()));
		nameValuePairs.add(new BasicNameValuePair("patient_ID", ""
				+ b.getString("patient_ID")));
		nameValuePairs.add(new BasicNameValuePair("doctor_ID", ""
				+ b.getString("doctor_ID")));
		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://emrophthalmology.comli.com/addOrder.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString() + " "
					+ e.getMessage());
		}
		// response>>>string
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			String resp = new String(baf.toByteArray());
			Log.d("httppost response", resp);
			if (resp.charAt(0) != 'e') {
				leye1.setText("");
				reye1.setText("");
				bino.setText("");
				l1.setText("");
				l2.setText("");
				l3.setText("");
				l4.setText("");
				r1.setText("");
				r2.setText("");
				r3.setText("");
				r4.setText("");
				b1.setText("");
				b2.setText("");
				slitlamp.setText("");
				
		    	final Toast toast = Toast.makeText(this, "Success!\nData saved successfully. Record can be viewed in Medical History page", Toast.LENGTH_SHORT);
		        toast.show();
		    	Handler handler = new Handler();
		        handler.postDelayed(new Runnable() {
		           @Override
		           public void run() {
		               toast.cancel(); 
			           }
			    }, 2000);
		    	
			} else {
				final Toast toast = Toast.makeText(this, "Error!\nSomething wrong with the server. Please contact system admin for help.", Toast.LENGTH_SHORT);
		        toast.show();
		    	Handler handler = new Handler();
		        handler.postDelayed(new Runnable() {
		           @Override
		           public void run() {
		               toast.cancel(); 
			           }
			    }, 2000);
			}
		} catch (ClientProtocolException e) {
			Log.e("Error", e.toString());
		} catch (IOException e) {
			Log.e("Error", e.toString());
		}
	}

	String[] chartTypeArray = { "logMAR chart", "Jaeger chart",
			"Snellen chart", "'Tumbling E' Eye Chart", "Landolt C", "Lea test" };
	String[] logmar = { "1.00", " 0.90", " 0.80", " 0.70", "0.60", "0.50",
			"0.40", "0.30", "0.20", "0.10", "0.00", "-0.10", "-0.20", "-0.30" };
	String[] jaeger = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18",
			"19", "20" };
	String[] others = { "0.10", "0.125", "0.16", "0.20", "0.25", "0.32",
			"0.40", "0.50", "0.63", "0.80", "1.00", "1.25", "1.60", "2.00" };
	String[] contrastSensitive = { "0.00", "0.15", "0.30", "0.45", "0.60",
			"0.75", "0.90", "1.05", "0.20", "1.35", "1.50", "1.65", "1.80",
			"1.95", "2.10", "2.25" };
	String[] bindnessArray = {"Normal","Monochromacy","Dichromacy","Anomalous trichromacy","Rod monochromacy","Cone monochromacy","Protanopia","Deuteranopia","Tritanopia","Protanomaly","Deuteranomaly","Tritanomaly"};

    
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
