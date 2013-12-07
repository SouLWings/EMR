package com.example.emr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class IshiharaTest extends Activity {
	//variable for selection intent
		private final int PICKER = 1;
		//variable to store the currently selected image
		private int currentPic = 0;
		//adapter for gallery view
		private PicAdapter imgAdapt;
		//gallery object
		private Gallery picGallery;
		int answerIndex = 0;
		Toast toast=null;
		String [] answer = {
				"¡°12¡å	All people should see a number 12, including those with total color blindness",
				"¡°8¡å	Those with normal color vision see an 8.\n¡°3¡å	Those with red green color blindness see a 3.\nNothing	Those with total color blindness see nothing.",
				"¡°29¡å	Those with normal color vision see a 29.\n¡°70¡å	Those with red green color blindness see a 70.\nNothing	Those with total color blindness see nothing.",
				"¡°5¡å	Those with normal color vision see a 5.\n¡°2¡å	Those with red green color blindness see a 2.\nNothing	Those with total color blindness see nothing.",
				"¡°3¡å	Those with normal color vision see a 3.\n¡°5¡å	Those with red green color blindness see a 5.\nNothing	Those with total color blindness see nothing.",
				"¡°15¡å	Those with normal color vision see a 15.\n¡°17¡å	Those with red green color blindness see a 17.\nNothing	Those with total color blindness see nothing.",
				"¡°74¡å	Those with normal color vision see a 74.\n¡°21¡å	Those with red green color blindness see a 21.\nNothing	Those with total color blindness see nothing.",
				"¡°6¡å	Those with normal color vision see a 6.\nNothing	The majority of color blind people cannot see this number clearly.",
				"¡°45¡å	Those with normal color vision see a 45.\nNothing	The majority of color blind people cannot see this number clearly.",
				"¡°5¡å	Those with normal color vision see a 5.\nNothing	The majority of color blind people cannot see this number clearly.",
				"¡°7¡å	Those with normal color vision see a 7.\nNothing	The majority of color blind people cannot see this number clearly.",
				"¡°16¡å	Those with normal color vision see a 16.\nNothing	The majority of color blind people cannot see this number clearly.",
				"¡°73¡å	Those with normal color vision see a 73.\nNothing	The majority of color blind people cannot see this number clearly.",
				"Nothing	People with normal vision or total color blindness should not be able to see any number.\n¡°5¡å	Those with red green color blindness should see a 5.",
				"Nothing	People with normal vision or total color blindness should not be able to see any number.\n¡°45¡å	Those with red green color blindness should see a 45.",
				"¡°26¡å	Those with normal color vision should see a 26.\n6, faint 2	Red color blind (protanopia) people will see a 6, mild red color blind people (prontanomaly)will also faintly see a number 2.\n2, faint 6	Green color blind (deuteranopia) people will see a 2, mild green color blind people(deuteranomaly) may also faintly see a number 6.",
				"¡°42¡å	Those with normal color vision should see a 42.\n2, faint 4	Red color blind (protanopia) people will see a 2, mild red color blind people (prontanomaly)will also faintly see a number 4.\n4, faint 2	Green color blind (deuteranopia) people will see a 4, mild green color blind people(deuteranomaly) may also faintly see a number 2.",
				"Those with normal color vision should be able to trace along both the purple and red lines.\nThose with Protanopia (red colorblind) should be able to trace the purple line, those with protanomaly (weak red vision) may be able to trace the red line, with increased difficulty.\nThose with Deuteranopia (green color blind) should be able to trace the red line, those with Deuteranomaly(weak green vision) may be able to trace the purple line, with increased difficulty.",
				"Those with normal color vision or total color blindness should be unable to trace the line.\nMost people with red green color blindness can trace the wiggly line, depending on the severity of the condition.",
				"Those with normal color vision should be able to trace a green wiggly line.\nMost people with any form of color blindness will be unable to trace the correct line.",
				"Those with normal color vision should be able to trace an orange wiggly line.\nMost people with any form of color blindness will be unable to trace the correct line.",
				"Those with normal color vision should be able to trace the blue-green/yellow-green wiggly line.\nRed green color blind people will trace the blue-green and red line.\nPeople with total color blindness will be unable to trace any line.",
				"Those with normal color vision should be able to trace the red and orange wiggly line.\nRed green color blind people will trace the red and blue-green wiggly line.\nPeople with total color blindness will be unable to trace any line.",
				"Everyone should be able to trace this wiggly line."};
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ishihara);
	        //get the gallery view
	        picGallery = (Gallery) findViewById(R.id.galleryIshihara);
	        //create a new adapter
	        imgAdapt = new PicAdapter(this);
	        //set the gallery adapter
	        picGallery.setAdapter(imgAdapt);
	        picGallery.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					answerIndex = position;
					getDialog();
				}	
	        });
	    }
	   
	    public class PicAdapter extends BaseAdapter {
	    	
	    	//use the default gallery background image
	        int defaultItemBackground;
	        
	        //gallery context
	        private Context galleryContext;

	        //array to store bitmaps to display
	        private Bitmap[] imageBitmaps;
	        //placeholder bitmap for empty spaces in gallery
	        Bitmap []placeholder;

	        //constructor
	        public PicAdapter(Context c) {
	        	
	        	//instantiate context
	        	galleryContext = c;
	        	
	        	//create bitmap array
	            imageBitmaps  = new Bitmap[24];
	            placeholder  = new Bitmap[24];
	            //decode the placeholder image
	            placeholder[0] = BitmapFactory.decodeResource(getResources(), R.drawable.plate1);
	            placeholder[1] = BitmapFactory.decodeResource(getResources(), R.drawable.plate2);
	            placeholder[2] = BitmapFactory.decodeResource(getResources(), R.drawable.plate3);
	            placeholder[3] = BitmapFactory.decodeResource(getResources(), R.drawable.plate4);
	            placeholder[4] = BitmapFactory.decodeResource(getResources(), R.drawable.plate5);
	            placeholder[5] = BitmapFactory.decodeResource(getResources(), R.drawable.plate6);
	            placeholder[6] = BitmapFactory.decodeResource(getResources(), R.drawable.plate7);
	            placeholder[7] = BitmapFactory.decodeResource(getResources(), R.drawable.plate8);
	            placeholder[8] = BitmapFactory.decodeResource(getResources(), R.drawable.plate9);
	            placeholder[9] = BitmapFactory.decodeResource(getResources(), R.drawable.plate10);
	            placeholder[10] = BitmapFactory.decodeResource(getResources(), R.drawable.plate11);
	            placeholder[11] = BitmapFactory.decodeResource(getResources(), R.drawable.plate12);
	            placeholder[12] = BitmapFactory.decodeResource(getResources(), R.drawable.plate13);
	            placeholder[13] = BitmapFactory.decodeResource(getResources(), R.drawable.plate14);
	            placeholder[14] = BitmapFactory.decodeResource(getResources(), R.drawable.plate15);
	            placeholder[15] = BitmapFactory.decodeResource(getResources(), R.drawable.plate16);
	            placeholder[16] = BitmapFactory.decodeResource(getResources(), R.drawable.plate17);
	            placeholder[17] = BitmapFactory.decodeResource(getResources(), R.drawable.plate18);
	            placeholder[18] = BitmapFactory.decodeResource(getResources(), R.drawable.plate19);
	            placeholder[19] = BitmapFactory.decodeResource(getResources(), R.drawable.plate20);
	            placeholder[20] = BitmapFactory.decodeResource(getResources(), R.drawable.plate21);
	            placeholder[21] = BitmapFactory.decodeResource(getResources(), R.drawable.plate22);
	            placeholder[22] = BitmapFactory.decodeResource(getResources(), R.drawable.plate23);
	            placeholder[23] = BitmapFactory.decodeResource(getResources(), R.drawable.plate24);
	            
	            //set placeholder as all thumbnail images in the gallery initially
	            for(int i=0; i<imageBitmaps.length; i++)
	            	imageBitmaps[i]=placeholder[i];
	            
	            //get the styling attributes - use default Andorid system resources
	            TypedArray styleAttrs = galleryContext.obtainStyledAttributes(R.styleable.PicGallery);
	            //get the background resource
	            defaultItemBackground = styleAttrs.getResourceId(
	            		R.styleable.PicGallery_android_galleryItemBackground, 0);
	            //recycle attributes
	            styleAttrs.recycle();
	        }

	        //BaseAdapter methods
	        
	        //return number of data items i.e. bitmap images
	        public int getCount() {
	            return imageBitmaps.length;
	        }

	        //return item at specified position
	        public Object getItem(int position) {
	            return position;
	        }

	        //return item ID at specified position
	        public long getItemId(int position) {
	            return position;
	        }

	        //get view specifies layout and display options for each thumbnail in the gallery
	        @SuppressWarnings("deprecation")
			public View getView(int position, View convertView, ViewGroup parent) {
	        	Display display = getWindowManager().getDefaultDisplay(); 
	        	int width = (int) (display.getWidth()*0.9);  // deprecated
	        	int height = (int) (display.getHeight()*0.9);  // deprecated
	        	//create the view
	            ImageView imageView = new ImageView(galleryContext);
	            //specify the bitmap at this position in the array
	            imageView.setImageBitmap(imageBitmaps[position]);
	            //set layout options
	            imageView.setLayoutParams(new Gallery.LayoutParams(width, height));
	            //scale type within view area
	            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	            //set default gallery item background
	            imageView.setBackgroundResource(defaultItemBackground);
	            //return the view
	            return imageView;
	            
	            
	        }
	        
	        //custom methods for this app
	        
	        //helper method to add a bitmap to the gallery when the user chooses one
	        public void addPic(Bitmap newPic)
	        {
	        	//set at currently selected index
	        	imageBitmaps[currentPic] = newPic;
	        }
	        
	        //return bitmap at specified position for larger display
	        public Bitmap getPic(int posn)
	        {
	        	//return bitmap at posn index
	        	return imageBitmaps[posn];
	        }
	        
	    }
	    public void getDialog(){
	    	if (toast!=null){
	    	toast.cancel();	    	
	    	}
	    	toast = Toast.makeText(this, answer[answerIndex], Toast.LENGTH_SHORT);
	        toast.show();
	    	Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
	           @Override
	           public void run() {
	               toast.cancel(); 
	           }
	    }, 2000);
	    	}
        }
	    
	    
	