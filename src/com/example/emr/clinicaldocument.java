package com.example.emr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.emr.modulepage;
import com.example.emr.R;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class clinicaldocument extends Activity {
	int count = 2;
	JSONObject jObj = null;
	String json = "";
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb = null;
	LinearLayout ll;
	ArrayList<AutoCompleteTextView> eyediseases = new ArrayList<AutoCompleteTextView>();
	EditText etdescription;
	Bundle b;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clinicaldocument);

		b = getIntent().getExtras();
		AutoCompleteTextView eyeDisease = (AutoCompleteTextView) findViewById(R.id.actvEyeDisease);
		etdescription = (EditText) findViewById(R.id.etdescription);
		eyediseases.add(eyeDisease);
		Button save = (Button) findViewById(R.id.bSave);
		Button btnadd2 = (Button) findViewById(R.id.btnadd2);

		ll = (LinearLayout) findViewById(R.id.ll);

		ArrayAdapter<String> adapterEyeDisease = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, eyediseaseList);
		eyeDisease.setThreshold(2);
		eyeDisease.setAdapter(adapterEyeDisease);

		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveData();
			}
		});
		btnadd2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addEyedisease();
			}
		});

	}

	protected void saveData() {
		String data = "";
		for (int i = 0; i < eyediseases.size(); i++){//AutoCompleteTextView at : eyediseases){
			if(!eyediseases.get(i).getText().toString().equals("")){
				if(i > 0)
					data = data + "; ";
				data = data + eyediseases.get(i).getText().toString();
			}
			eyediseases.get(i).setText("");
		}
		if(data.equals("") && etdescription.getText().toString().equals("")){
	    	final Toast t = Toast.makeText(this, "No data saved.", Toast.LENGTH_SHORT);
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

		try {

			nameValuePairs.add(new BasicNameValuePair("eyedisease", data));
			nameValuePairs.add(new BasicNameValuePair("patient_ID", b
					.getString("patient_ID")));
			nameValuePairs.add(new BasicNameValuePair("doctor_ID", b
					.getString("doctor_ID")));
			nameValuePairs.add(new BasicNameValuePair("etdescription",
					etdescription.getText().toString()));
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://emrophthalmology.comli.com/addEyediseases.php");
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
			if (resp.charAt(0) != 'e') {
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
		} catch (IOException e) {
		}
		etdescription.setText("");
	}

	protected void addEyedisease() {
		AutoCompleteTextView newac = new AutoCompleteTextView(this);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, eyediseaseList);
		newac.setThreshold(2);
		newac.setAdapter(adapter2);
		ll.addView(newac, count++);
		eyediseases.add(newac);
	}

	private static final String[] eyediseaseList = new String[] { "Hordeolum",
			"Chalazion", "Blepharitis", "Entropion", "trichiasis", "Ectropion",
			"Lagophthalmos", "Blepharochalasis", "Ptosis", "Xanthelasma",
			"Dermatitis", "Demodex", "leishmaniasis", "loiasis",
			"onchocerciasis", "phthiriasis", "herpes viral infection",
			"herpes simplex", "leprosy", "molluscum contagiosum",
			"tuberculosis", "yaws", "zoster", "impetigo", "Dacryoadenitis",
			"Epiphora", "exophthalmos", "Conjunctivitis", "Scleritis",
			"Keratitis", "Corneal ulcer", "Corneal abrasion", "Snow blindness",
			"Arc eye", "Thygeson's superficial punctate keratopathy",
			"keratopathy", "Corneal neovascularization", "Fuchs' dystrophy",
			"Keratoconus", "Keratoconjunctivitis sicca", "Iritis", "Uveitis",
			"Sympathetic ophthalmia", "Cataract", "Chorioretinal inflammation",
			"Focal chorioretinal inflammation", "chorioretinitis",
			"choroiditis", "retinitis", "retinochoroiditis",
			"Disseminated chorioretinal inflammation", "exudative retinopathy",
			"Posterior cyclitis", "Pars planitis",
			"chorioretinal inflammations", "Harada's disease",
			"Chorioretinal inflammation", "Chorioretinal scars",
			"Macula scars", "posterior pole", "postinflammatory",
			"post-traumatic", "Solar retinopathy", "Choroidal degeneration",
			"Atrophy", "Sclerosis", "angioid streaks", "choroidal dystrophy",
			"Choroideremia", "Dystrophy", "choroidal", "central areolar",
			"peripapillary", "Gyrate atrophy", "choroid", "ornithinaemia",
			"Choroidal haemorrhage", "rupture",
			"NOS (Not Otherwise Specified)", "expulsive",
			"Choroidal detachment", "Chorioretinal disorders",
			"Chorioretinal inflammation", "infectious", "parasitic diseases",
			"Chorioretinitis", "syphilitic", "toxoplasma", "tuberculous",
			"Retinal detachment", "distorted vision", "Retinoschisis",
			"Retinal vascular occlusions", "Hypertensive retinopathy",
			"Diabetic retinopathy", "Retinopathy", "Retinopathy",
			"prematurity", "Age-related macular degeneration", " macula",
			"malfunction", "Macular degeneration", "Bull's Eye Maculopathy",
			"Epiretinal membrane", "Peripheral retinal degeneration",
			"Hereditary retinal dystrophy", "Retinitis pigmentosa",
			"Retinal haemorrhage", "retinal layers",
			"Central serous retinopathy", "Retinal detachment",
			"retinal disorders", "Macular edema", "swollen", "macula",
			"Retinal disorder", "Glaucoma", "optic neuropathy",
			"Glaucoma suspect", "ocular hypertension",
			"Primary open-angle glaucoma", "open-angle glaucoma",
			"Primary angle-closure glaucoma", "angle-closure glaucoma",
			"Floaters", "Leber's hereditary optic neuropathy",
			"Optic disc drusen", "Strabismus", "Crossed eye", "Wandering eye",
			"Walleye", "Ophthalmoparesis",
			"Progressive external ophthaloplegia", "Esotropia", "Exotropia",
			"Disorders of refraction", "accommodation", "Hypermetropia",
			"Farsightedness", "Myopia", "Nearsightedness", "Astigmatism",
			"Anisometropia", "Presbyopia", "ophthalmoplegia",
			"internal ophthalmoplegia", "Amblyopia", "lazy eye",
			"Leber's congenital amaurosis", "Scotoma", "blind spot", "Anopsia",
			"Color blindness", "Achromatopsia", "Maskun", "cone cells",
			"Nyctalopia", "Nightblindness", "Blindness", "River blindness",
			"Micropthalmia", "coloboma", "optic nerve", "spinal cord",
			"Red eye", "Argyll Robertson pupil", "pupils", "Keratomycosis",
			"fungal infection", "cornea", "Xerophthalmia", "dry eyes",
			"Aniridia", "congenital eye", "iris", "Retinoblastoma",
			"droopy eyelid", "Grave's Disease" };

    
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
