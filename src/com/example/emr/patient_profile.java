package com.example.emr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class patient_profile extends Activity {
	
	ImageView patientPic;
	EditText etRN,etName,etIC,etGender,etDOB,etAdd,etPhone;
	Button bnext;
	Bundle b;
    JSONObject jObj = null;
    String json = "";
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb=null;
	URL ivURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		
		b = getIntent().getExtras();
		
		patientPic = (ImageView)findViewById(R.id.patientPic);
		etRN = (EditText)findViewById(R.id.etRN);
		etName = (EditText)findViewById(R.id.etName);
		etIC = (EditText)findViewById(R.id.etIC);
		etGender = (EditText)findViewById(R.id.etGender);
		etDOB = (EditText)findViewById(R.id.etDOB);
		etAdd = (EditText)findViewById(R.id.etAddress);
		etPhone = (EditText)findViewById(R.id.etphone);
		bnext = (Button)findViewById(R.id.bNext);
		
		try {
			  ivURL = new URL("http://emrophthalmology.comli.com/img/profile_pic/profilepicdefault.jpg");
			  Bitmap bitmap = BitmapFactory.decodeStream(ivURL.openConnection().getInputStream());
			  patientPic.setImageBitmap(bitmap); 
			} catch (MalformedURLException e) {
			  e.printStackTrace();
			} catch (IOException e) {
			  e.printStackTrace();
			}
		
		
		setPatientData(b.getString("patient_ID"));

        bnext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent modulepage = new Intent(patient_profile.this, modulepage.class);
            	modulepage.putExtras(b);
            	startActivity(modulepage);
            	finish();
            }
        });
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	public void setPatientData(String patient_ID){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        try{

            nameValuePairs.add(new BasicNameValuePair("patient_ID", patient_ID));
            	      	  
             HttpClient httpclient = new DefaultHttpClient();
             HttpPost httppost = new HttpPost("http://emrophthalmology.comli.com/getPatientData.php");
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
 
        //parse string>>> JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        
        try {
            
            int success = jObj.getInt("success");

            if (success == 1) {
                
            	jArray = jObj.getJSONArray("patientData");

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject patient = jArray.getJSONObject(i);
            		etRN.setText(patient.getString("ID"));
            		etName.setText(patient.getString("Name"));
            		etIC.setText(patient.getString("IC_no"));
            		etGender.setText(patient.getString("Gender"));
            		etDOB.setText(patient.getString("DoB"));
            		etAdd.setText(patient.getString("Address"));
            		etPhone.setText(patient.getString("Phone"));
                }
            } else {
				Dialog d = new Dialog(this);
				TextView tv = new TextView(this);
				tv.setText("No patient data received.\nPlease contact system admin for help.");
				d.setTitle("Error!");
				d.setContentView(tv);
				d.show();
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
