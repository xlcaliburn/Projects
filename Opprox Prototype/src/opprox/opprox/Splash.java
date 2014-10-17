package opprox.opprox;

import android.R;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Splash extends ActionBarActivity {

	public Button foodButton;
	public Button clothingButton;
	public Button healthButton;
	public Button entertainmentButton;
	public double lat = 0.0;
	public double lon = 0.0;
	public String category;
	TextView title;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			lat = extras.getDouble("lat");
			lon = extras.getDouble("lon");
		}
		
		//Title
		title = (TextView) findViewById(R.id.title);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/raleway.otf");
		title.setTypeface(tf);
		title.setText("SELECT A\nCATEGORY");
		
		//Food Button
		foodButton = (Button) findViewById(R.id.food_button);
        foodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		Intent k = new Intent(Splash.this, ListOfItems.class);
            		k.putExtra("lat", lat);
            		k.putExtra("lon", lon);
            		k.putExtra("category", "food");
            		startActivity(k);
            	} catch(Exception e){
            		e.printStackTrace();
            	}
            }
        });
		
		//Clothing Button
		clothingButton = (Button) findViewById(R.id.clothing_button);
        clothingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		Intent k = new Intent(Splash.this, ListOfItems.class);
            		k.putExtra("lat", lat);
            		k.putExtra("lon", lon);
            		k.putExtra("category", "clothing");
            		startActivity(k);
            	} catch(Exception e){
            		e.printStackTrace();
            	}
            }
        });
		
		//Health Button
        healthButton = (Button) findViewById(R.id.health_button);
        healthButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		Intent k = new Intent(Splash.this, ListOfItems.class);
            		k.putExtra("lat", lat);
            		k.putExtra("lon", lon);
            		k.putExtra("category", "health");
            		startActivity(k);
            	} catch(Exception e){
            		e.printStackTrace();
            	}
            }
        });
        
        //Entertainment Button
		entertainmentButton = (Button) findViewById(R.id.entertainment_button);
        entertainmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
            		Intent k = new Intent(Splash.this, ListOfItems.class);
            		k.putExtra("lat", lat);
            		k.putExtra("lon", lon);
            		k.putExtra("category", "entertainment");
            		startActivity(k);
            	} catch(Exception e){
            		e.printStackTrace();
            	}
            }
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_splash,
					container, false);
			return rootView;
		}
	}

}
