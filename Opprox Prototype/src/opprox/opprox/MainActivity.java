package opprox.opprox;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	private double lat = 0.0;
	private double lon = 0.0;
	private double screenWidth, screenHeight;
	private TextView tv;
	private ImageView logo;
	private Button locationButton, retryButton;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private AnimationDrawable frameAnimation;
	private boolean backPressed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
        //Stuff
        int x = 0;
        int y = x / 0;
        Toast.makeText(getApplicationContext(), y, Toast.LENGTH_LONG).show();
		
		//Get screen size (Try to)
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		
		try {
			Point size = new Point();
			display.getSize(size);
			screenWidth = size.x;
			screenHeight = size.y;
		} catch (Exception e) {
			screenWidth = display.getWidth();
			screenHeight = display.getHeight();
		}
		
		//Initialize logo
		logo = (ImageView) findViewById(R.id.logo);
		logo.getLayoutParams().width = (int)(0.60 * screenWidth);
		logo.getLayoutParams().height = (int)(0.60 * screenWidth);
		LayoutParams params = (LayoutParams) logo.getLayoutParams();
		params.setMargins(0, (int)(screenHeight * 0.15), 0, 0);
		logo.setLayoutParams(params);
		logo.setBackgroundResource(R.drawable.animation_list);
		frameAnimation = (AnimationDrawable) logo.getBackground();
		frameAnimation.start();
		
		//Initialize TextView
		tv = (TextView)findViewById(R.id.location);
		tv.setText("Location scouting...");
		
		//Check for internet connection
		if (!isNetworkAvailable()) {
			tv.setText("We'll need a bit of\ninternet first!\n");
			logo.setBackgroundResource(R.drawable.main);
			frameAnimation.stop();
			
			//Retry by restarting activity
			retryButton = (Button) findViewById(R.id.retry_button);
			retryButton.setTextColor(getApplication().getResources().getColor(R.color.white));
			retryButton.setText("Retry");
			retryButton.setVisibility(View.VISIBLE);
			retryButton.setOnClickListener(new View.OnClickListener() {
				 public void onClick(View v) {
					 Intent intent = getIntent();
					 finish();
					 startActivity(intent);
				 }
			});
			locationButton = (Button) findViewById(R.id.location_button);
			locationButton.setTextColor(getApplication().getResources().getColor(R.color.white));
			locationButton.setText("Check Internet Settings");
			locationButton.setVisibility(View.VISIBLE);
			locationButton.setOnClickListener(new View.OnClickListener() {
				 public void onClick(View v) {
					 Intent myIntent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					 startActivity(myIntent);
				 }
			});
		} 
		else //There is internet connection
		{
	        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			// Define a listener that responds to location updates
			locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					if (!backPressed) {
						// Called when a new location is found by the network location provider.
						lat = location.getLatitude();
						lon = location.getLongitude();
						frameAnimation.stop();
		            	locationManager.removeUpdates(locationListener);
		            	try {
		            		Intent k = new Intent(MainActivity.this, Splash.class);
		            		k.putExtra("lat", lat);
		            		k.putExtra("lon", lon);
		            		startActivity(k);
		            	} catch(Exception e){
		            		e.printStackTrace();
		            	}
		            	finish();
					}
				}
		
				public void onStatusChanged(String provider, int status, Bundle extras) {}
				public void onProviderEnabled(String provider) {}
				public void onProviderDisabled(String provider) {
					tv.setText("Looks like you haven't\nenabled location services");
					frameAnimation.stop();
					
					//Retry by restarting activity
					retryButton = (Button) findViewById(R.id.retry_button);
					retryButton.setTextColor(getApplication().getResources().getColor(R.color.white));
					retryButton.setText("Retry");
					retryButton.setVisibility(View.VISIBLE);
					retryButton.setOnClickListener(new View.OnClickListener() {
						 public void onClick(View v) {
							 Intent intent = getIntent();
							 finish();
							 startActivity(intent);
						 }
					});
					locationButton = (Button) findViewById(R.id.location_button);
					locationButton.setTextColor(getApplication().getResources().getColor(R.color.white));
					locationButton.setText("Check Settings");
					locationButton.setVisibility(View.VISIBLE);
					locationButton.setOnClickListener(new View.OnClickListener() {
						 public void onClick(View v) {
							 Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							 startActivity(myIntent);
						 }
					});
					//retryButton = (Button) findViewById(R.id.retry);
	
				}
			};
			// Register the listener with the Location Manager to receive location updates
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}
	}
	
	//Internet connection checker
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
		} else if (id == R.id.exit_button) {
			finish();
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		backPressed = true;
    		finish();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
}
