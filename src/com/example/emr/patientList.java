package com.example.emr;

import java.io.*;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class patientList extends Activity{

	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb=null;
	EditText etSearch;
	ImageButton searchbtn;
	ListView patientlistview;
    JSONObject jObj = null;
    String json = "";
    Bundle b;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.patient_list);
	        b = getIntent().getExtras();
	        etSearch = (EditText)findViewById(R.id.search);
	        searchbtn = (ImageButton)findViewById(R.id.searchbtn);
	        searchbtn.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	String keyword = etSearch.getText().toString();
	            	etSearch.setText("searching...");
	            	search(keyword);
	            	
	            	etSearch.setText("");
	            }
	
	            });
	 }
	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.emr__main, menu);
	        return true;
	    }
	    
	    
	    public void search(String searchKeyword){
	    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        	LinearLayout linearList = (LinearLayout) findViewById(R.id.linearList);
        	linearList.removeAllViews();
	        try{

	            nameValuePairs.add(new BasicNameValuePair("search", searchKeyword));
	            	      	  
	             HttpClient httpclient = new DefaultHttpClient();
	             HttpPost httppost = new HttpPost("http://emrophthalmology.comli.com/patientlist.php");
	             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	             HttpResponse response = httpclient.execute(httppost);
	             HttpEntity entity = response.getEntity();
	             is = entity.getContent();
	             }catch(Exception e){
	                 Log.e("log_tag", "Error in http connection"+e.toString());
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
	        Log.d("json string",json);
	        //parse string>>> JSON object
	        try {
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }

			Log.d("httppost response", jObj.toString());
	        try {
                
                int success = jObj.getInt("success");
 
                if (success == 1) {
                    
                    //patientlist
                	jArray = jObj.getJSONArray("patient");
 
                    
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject patient = jArray.getJSONObject(i);
            	    	LinearLayout linearLayout = new LinearLayout(this);
            	        linearLayout.setOrientation(LinearLayout.VERTICAL);
            	        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
            	            LinearLayout.LayoutParams.MATCH_PARENT,
            	            LinearLayout.LayoutParams.MATCH_PARENT);
            	    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
            	    	        (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    	TextView x = new TextView(this);
                    	TextView y = new TextView(this);
                    	x.setText("Patient ID: " + patient.getString("ID"));
                    	y.setText("Patient Name: " + patient.getString("Name"));
                    	x.setLayoutParams(new LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT));
                    	y.setLayoutParams(new LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT));
                    	final String patient_ID = patient.getString("ID");
                    	
            	        x.setOnClickListener(new View.OnClickListener() {
            	            public void onClick(View v) {
            	            	Intent patient_profile = new Intent(patientList.this, patient_profile.class);
            	            	b.putString("patient_ID", patient_ID);
            	            	patient_profile.putExtras(b);
            	            	startActivity(patient_profile);
            	            	finish();
            	            }
            	        });
            	        
            	        y.setOnClickListener(new View.OnClickListener() {
            	            public void onClick(View v) {
            	            	Intent patient_profile = new Intent(patientList.this, patient_profile.class);
            	            	b.putString("patient_ID", patient_ID);
            	            	patient_profile.putExtras(b);
            	            	startActivity(patient_profile);
            	            	finish();
            	            }
            	        });
                    	
            			linearLayout.addView(x, layoutParams);
            			linearLayout.addView(y, layoutParams);

            			linearList.addView(linearLayout, llp);
                    }
                } else {
                	TextView e = new TextView(this);
                	e.setText("No record found.");
                	((LinearLayout)findViewById(R.id.linearList)).addView(e);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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