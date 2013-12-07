package com.example.emr;

import java.io.*;
import java.util.*;

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
import org.json.JSONArray;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EMR_login_MainActivity extends Activity {

	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb=null;
	EditText username;
	EditText password;
	Button login;
	Button cancel;
	TextView display;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        display = (TextView)findViewById(R.id.textView4);
        login = (Button)findViewById(R.id.login);
        cancel = (Button)findViewById(R.id.cancel);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	display.setText("Logging in...");
                login(username.getText().toString(), password.getText().toString());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0);
            }   
        });
        
        
      }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.emr__main, menu);
        return true;
    }
    
    
    public void login(String un, String pw){
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        //http post to php
        try{

            nameValuePairs.add(new BasicNameValuePair("username", un));
            nameValuePairs.add(new BasicNameValuePair("password", pw));
      	  
             HttpClient httpclient = new DefaultHttpClient();
             HttpPost httppost = new HttpPost("http://emrophthalmology.comli.com/login.php");
             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             HttpResponse response = httpclient.execute(httppost);
             HttpEntity entity = response.getEntity();
             is = entity.getContent();
             }catch(Exception e){
                 Log.e("log_tag", "Error in http connection"+e.toString());
            }
        //response>>>string
        try{
      	  BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);

            int current = 0;
             
            while((current = bis.read()) != -1){
                baf.append((byte)current);
            }  

            String resp = new String(baf.toByteArray());
			Log.d("httppost response", resp);
            if(resp.charAt(0) != 'e'){
            	display.setText("Log in success!");
            	Intent it = new Intent(this, patientList.class);
            	Bundle b = new Bundle();
            	b.putString("doctor_ID", resp.split("\n")[0].split(" ")[0]);
            	Log.d("doc id",b.getString("doctor_ID"));
            	it.putExtras(b);
				startActivity(it);           
            	display.setText(""); 
            }
            else{display.setText("Incorrect username or password");}
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
    }
    
    
}
