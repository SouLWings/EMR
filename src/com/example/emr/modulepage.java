package com.example.emr;
import com.example.emr.R;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class modulepage extends TabActivity {
    /** Called when the activity is first created. */
    Bundle b;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_module_page);
        
        TabHost tabHost = getTabHost();
        
        b = getIntent().getExtras();

        // Tab for clinicaldocument
        TabSpec notespec = tabHost.newTabSpec("Notes");
        notespec.setIndicator("Notes");
        Intent noteIntent = new Intent(this, clinicaldocument.class);
        noteIntent.putExtras(b);
        notespec.setContent(noteIntent);
        
        // Tab for eyeexam
        TabSpec eyespec = tabHost.newTabSpec("Eye Exam");
        eyespec.setIndicator("Eye Exam");
        Intent eyeIntent = new Intent(this, eyeexam.class);
        eyeIntent.putExtras(b);
        eyespec.setContent(eyeIntent);
        
        // Tab for order
        TabSpec orderspec = tabHost.newTabSpec("Order");
        orderspec.setIndicator("Order");
        Intent ordersIntent = new Intent(this, order.class);
        ordersIntent.putExtras(b);
        orderspec.setContent(ordersIntent);
        
        // Tab for labtest
        TabSpec labtestspec = tabHost.newTabSpec("Lab test");
        labtestspec.setIndicator("Lab test");
        Intent labtestIntent = new Intent(this, labtest.class);
        labtestIntent.putExtras(b);
        labtestspec.setContent(labtestIntent);
        
        // Tab for medical history
        TabSpec mHistoryspec = tabHost.newTabSpec("Medical History");
        mHistoryspec.setIndicator("Medical History");
        Intent mHistoryIntent = new Intent(this, medicalhistory.class);
        mHistoryIntent.putExtras(b);
        mHistoryspec.setContent(mHistoryIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(eyespec); // Adding eye tab
        tabHost.addTab(notespec); // Adding clinical document tab
        tabHost.addTab(orderspec); // Adding order tab
        tabHost.addTab(labtestspec); // Adding lab test tab
        tabHost.addTab(mHistoryspec); // Adding medical history tab
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
        return true;
    	
    	case R.id.menulogout:  
        	System.exit(0);
        return true;    

    	default:
            return true;    
    	}
	}
       
}
