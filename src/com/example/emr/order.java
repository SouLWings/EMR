package com.example.emr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class order extends Activity {
	EditText leye1, reye1, medcine, medcinedose;
	CheckBox xray, urinetest, bloodtest, head, leg, hand;
	Button btnorder, btnaddsurgery, btnaddlabtest, btnaddmedcine;
	InputStream is = null;
	Bundle b;
	AutoCompleteTextView labtest, surgery;
	LinearLayout ll;
	int labtestCount = 2;
	int surgeryCount = 2;
	int medcineCount = 2;
	Button mPickDate;
	int mYear;
	int mMonth;
	int mDay;
	Button mPickTime;
	private int mhour;
	int mminute;
	final int TIME_DIALOG_ID = 1;
	final int DATE_DIALOG_ID = 0;
	ArrayList<AutoCompleteTextView> labtests = new ArrayList<AutoCompleteTextView>();
	ArrayList<AutoCompleteTextView> surgeries = new ArrayList<AutoCompleteTextView>();
	ArrayList<EditText> medcines = new ArrayList<EditText>();
	ArrayList<EditText> doses = new ArrayList<EditText>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);

		b = getIntent().getExtras();

		ArrayAdapter<String> labtestAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, labtestlist);
		labtest = (AutoCompleteTextView) findViewById(R.id.labtest);
		labtest.setThreshold(2);
		labtest.setAdapter(labtestAdapter);
		labtests.add(labtest);

		ArrayAdapter<String> surgeryAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, surgerylist);
		surgery = (AutoCompleteTextView) findViewById(R.id.surgery);
		surgery.setThreshold(2);
		surgery.setAdapter(surgeryAdapter);
		surgeries.add(surgery);

		medcine = (EditText) findViewById(R.id.medcine);
		medcinedose = (EditText) findViewById(R.id.medcinedose);

		medcines.add(medcine);
		doses.add(medcinedose);

		btnorder = (Button) findViewById(R.id.btnorder);
		btnaddlabtest = (Button) findViewById(R.id.btnlabtest);
		btnaddsurgery = (Button) findViewById(R.id.btnsurgery);
		btnaddmedcine = (Button) findViewById(R.id.btnmedcine);

		ll = (LinearLayout) findViewById(R.id.ll);
		btnorder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addOrder();
			}
		});
		btnaddlabtest.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addlabtest();
			}
		});
		btnaddsurgery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addSurgery();
			}
		});
		btnaddmedcine.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addmedcine();
			}
		});
		// date time picker
		mPickDate = (Button) findViewById(R.id.bdate);
		mPickTime = (Button) findViewById(R.id.btime);
		// Pick time's click event listener
		mPickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);
			}
		});
		// PickDate's click event listener
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mhour = c.get(Calendar.HOUR_OF_DAY);
		mminute = c.get(Calendar.MINUTE);
	}

	public void addlabtest() {
		AutoCompleteTextView newlabtest = new AutoCompleteTextView(this);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, labtestlist);
		newlabtest.setThreshold(2);
		newlabtest.setAdapter(adapter2);
		ll.addView(newlabtest, labtestCount++);
		labtests.add(newlabtest);
	}

	public void addSurgery() {
		AutoCompleteTextView newsurgery = new AutoCompleteTextView(this);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, surgerylist);
		newsurgery.setThreshold(2);
		newsurgery.setAdapter(adapter2);
		ll.addView(newsurgery, labtestCount + surgeryCount++);
		surgeries.add(newsurgery);
	}

	public void addmedcine() {
		EditText newmedcine = new EditText(this);
		newmedcine.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
		EditText newdose = new EditText(this);
		newdose.setHint("Dose");

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.addView(newmedcine);
		linearLayout.addView(newdose);
		ll.addView(linearLayout, labtestCount + surgeryCount + medcineCount++);
		medcines.add(newmedcine);
		doses.add(newdose);
	}

	public void addOrder() {
		String labtestString = "";
		String surgeryString = "";
		String medcineString = "";
		for (int i = 0; i < labtests.size(); i++) {
			if (!labtests.get(i).getText().toString().equals("")) {
				if (i > 0)
					labtestString = labtestString + "; ";
				labtestString = labtestString
						+ labtests.get(i).getText().toString();
			}
			labtests.get(i).setText("");
		}
		for (int i = 0; i < surgeries.size(); i++) {
			if (!surgeries.get(i).getText().toString().equals("")) {
				if (i > 0)
					surgeryString = surgeryString + "; ";
				surgeryString = surgeryString
						+ surgeries.get(i).getText().toString();
			}
			surgeries.get(i).setText("");
		}
		for (int i = 0; i < medcines.size(); i++) {
			if (!medcines.get(i).getText().toString().equals("")
					&& !doses.get(i).getText().toString().equals("")) {
				if (i > 0)
					medcineString = medcineString + "; ";
				medcineString = medcineString
						+ medcines.get(i).getText().toString() + " - "
						+ doses.get(i).getText().toString();
			}
			medcines.get(i).setText("");
			doses.get(i).setText("");
		}
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		String time = mPickDate.getText().toString() + " "
				+ mPickTime.getText().toString();
		if (time.charAt(0) == 'C')
			time = "";

		if (mPickDate.getText().toString().equals("")
				&& mPickTime.getText().toString().equals("")
				&& labtestString.equals("") && surgeryString.equals("")
				&& medcineString.equals("")) {
			final Toast t = Toast.makeText(this, "No data saved.",
					Toast.LENGTH_SHORT);
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

		try {

			nameValuePairs.add(new BasicNameValuePair("appointmentdate", time));
			nameValuePairs
					.add(new BasicNameValuePair("labtest", labtestString));
			nameValuePairs
					.add(new BasicNameValuePair("surgery", surgeryString));
			nameValuePairs
					.add(new BasicNameValuePair("medcine", medcineString));
			nameValuePairs.add(new BasicNameValuePair("patient_ID", ""
					+ b.getString("patient_ID")));
			nameValuePairs.add(new BasicNameValuePair("doctor_ID", ""
					+ b.getString("doctor_ID")));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://emrophthalmology.comli.com/addOrder.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
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
			Log.d("response", resp);
			if (resp.charAt(0) != 'e') {
				final Toast toast = Toast
						.makeText(
								this,
								"Success!\nData saved successfully. Record can be viewed in Medical History page",
								Toast.LENGTH_SHORT);
				toast.show();
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						toast.cancel();
					}
				}, 2000);
			} else {
				final Toast toast = Toast
						.makeText(
								this,
								"Error!\nSomething wrong with the server. Please contact system admin for help.",
								Toast.LENGTH_SHORT);
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
		} catch (IOException e) {
		}
		mPickDate.setText("Choose a date");
		mPickTime.setText("Choose a time");
	}

	private static final String[] labtestlist = new String[] { "Elisa",
			"Fasting blood sugar level", "Urine test",
			"Blood test-Hemoglobin levels", "Blood test-Leukocytes count",
			"Blood test-Platelet count", "Bleeding time", "Clotting time",
			"Activated Partial Thromboplastin Time(APTT)" };
	private static final String[] surgerylist = new String[] { "Hordeolum",
			"Laser eye surgery", "Radial keratotomy", "Cataract surgery",
			"Glaucoma surgery", "Canaloplasty", "Refractive surgery",
			"Keratomilleusis", "Automated lamellar keratoplasty(ALK)", "ALK",
			"Laser assisted in-situ keratomileusis(LASIK)", "LASIK",
			"IntraLASIK", "LASEK", "Epi-LASIK",
			"Photorefractive keratectomy (PRK)", "PRK",
			"Laser thermal keratoplasty (LTK)", "LTK",
			"Conductive keratoplasty (CK)", "CK",
			"Limbal relaxing incisions (LRI)", "LRI",
			"Astigmatic keratotomy (AK)", " AK", "Arcuate keratotomy",
			"Transverse keratotomy", "Radial keratotomy (RK)", "RK",
			"Mini Asymmetric Radial Keratotomy (M.A.R.K.)", "M.A.R.K.",
			"Hexagonal keratotomy (HK)", "HK", "Epikeratophakia",
			"Intracorneal rings (ICRs)", "ICRs",
			"corneal ring segments (Intacs) ", "Intacs",
			"Implantable contact lenses", "Presbyopia reversal",
			"Anterior ciliary sclerotomy (ACS)", "ACS",
			"Laser reversal of presbyopia (LRP)", "LRP",
			"Scleral expansion bands (SEB)", "SEB", "The Karmra inlay",
			"Karmar inlay", "Scleral reinforcement surgery", "Corneal surgery",
			"Corneal transplant surgery", "Penetrating keratoplasty (PK)",
			"PK", "Keratoprosthesis(KPro)", "KPro",
			"Phototherapeutic keratectomy (PTK)", "PTK", "Pterygium excision",
			"Corneal tattooing", "Osteo-Odonto-Keratoprosthesis (OOKP)",
			"OOKP", "Vitreo-retinal surgery", "Vitrectomy",
			"Anterior vitrectomy", "Pars plana vitrectomy (PPV)", " PPV",
			"trans pars plana vitrectomy (TPPV)", "TPPV",
			"Pan retinal photocoagulation (PRP)", "PRP",
			"Retinal detachment repair", "Ignipuncture", " scleral buckle",
			"Laser", "Photocoagulation", "Photocoagulation therapy",
			"Pneumatic retinopexy", "Retinal cryopexy", "Retinal cryotherapy",
			"Macular hole repair", "Partial lamellar sclerouvectomy",
			"Partial lamellar sclerocyclochoroidectomy",
			"Partial lamellar sclerochoroidectomy", "Posterior sclerotomy",
			"Radial optic neurotomy", "Macular translocation surgery",
			"Eye muscle surgery", "Oculoplastic surgery", "Blepharoplasty",
			"Asian Blepharoplasty", "Eyelift", "Ptosis", "Entropion repair",
			"Entropion", "Canthal resection", "canthectomy", "Cantholysis",
			"Canthopexy", "canthoplasty", "canthorrhaphy", "canthotomy",
			"lateral canthotomy", "Epicanthoplasty", "Tarsorrhaphy",
			"Orbital surgery", "Orbital reconstruction", "Ocular prosthetics",
			"False Eyes", "Orbital decompression", "Botox injections",
			"Ultrapeel Microdermabrasion", "Endoscopic forehead", "browlift",
			"Face lift", "Rhytidectomy", "Liposuction", "Browplasty Surgery",
			"lacrimal apparatus", "Dacryocystorhinostomy (DCR)", " DCR",
			"Dacryocystorhinotomy", "Canaliculodacryocystostomy",
			"Canaliculotomy", "Ddacryoadenectomy", "Dacryocystectomy",
			"Dacryocystostomy", "Dacryocystotomy", "Eye removal",
			"Enucleation", "Evisceration", "Exenteration", "Ciliarotomy",
			" Ciliectomy", " Ciliotomy", "Conjunctivoanstrostomy",
			"Conjuctivoplasty", " Conjunctivorhinostomy", "Corectomedialysis",
			"Coretomedialysis", "Corectomy", "Coretomy", " Corelysis",
			"Coremorphosis", "Coreplasty", "Coreoplasty", "Coreoplasy",
			"Laser pupillomydriasis", "Cyclectomy", " Cyclotomy",
			"Cyclicotomy", "Cycloanemization", "Iridectomesodialsys",
			"Iridodialysis", "Coredialysis", "Iridencleisis", "Corenclisis",
			" Basal iridencleisis", "Total iridencleisis", "Iridesis",
			"Iridocorneosclerectomy", "Iridocyclectomy", "Iridocystectomy",
			"Iridosclerectomy", "Iridosclerotomy", " Rhinommectomy",
			"Trepanotrabeculectomy" };

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	// Datepicker dialog generation
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			mPickDate.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mDay).append("/").append(mMonth + 1).append("/")
					.append(mYear).append(" "));
		}
	};
	// Timepicker dialog generation
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mhour = hourOfDay;
			mminute = minute;
			mPickTime.setText(new StringBuilder().append(pad(mhour))
					.append(":").append(pad(mminute)));
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);

		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mhour, mminute,
					false);
		}
		return null;
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
