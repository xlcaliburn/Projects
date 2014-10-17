package opprox.opprox;

import android.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListOfItems extends ActionBarActivity {

	private WebView mWebView;
	private double lat = 0.0;
	private double lon = 0.0;
	private String category;
	private String url;
	private TextView tv;
	private ProgressBar pg;
	private Handler mHandler = new Handler();
	private boolean failed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_of_items);

		//Get bundles
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			lat = extras.getDouble("lat");
			lon = extras.getDouble("lon");
			category = extras.getString("category");
			//Uppercase first letter
			category = Character.toUpperCase(category.charAt(0)) + category.substring(1);
			if (category.equals("Health"))
				setTitle("Health & Beauty");
			else
				setTitle(category);
			url = "http://opprox.com/business/currentdeals.php?latitude=" + lat + "&longitude=" + lon + "&category=" + category + "&radius=10";
		}
		
		tv = (TextView) findViewById(R.id.text_view);
		tv.setText("One Sec\nDeal hunting....");
		pg = (ProgressBar) findViewById(R.id.progress_bar);
		pg.animate();
		
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.clearCache(true);
        mWebView.setWebViewClient(new MyAppWebViewClient(){
            // Load URL
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	try {
            		Intent k = new Intent(ListOfItems.this, DealActivity.class);
            		k.putExtra("url", url);
            		startActivity(k);
            	} catch(Exception e){
            		e.printStackTrace();
            	}
                return true;
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            	failed = true;
            }
            
            // When page is finished loading
            public void onPageFinished(WebView view, String url) {
            	if (failed) {
            		tv.setText("Seems like your\ninternet connection\nwent out :(");
            		pg.setVisibility(View.GONE);
            	} else {
	            	//Hide loading image
	            	pg.setVisibility(View.GONE);
	            	tv.setText("We got it!");
	            	TranslateAnimation animate = new TranslateAnimation(0,-findViewById(R.id.splashback).getWidth(),0,0);
	            	animate.setDuration(500);
	            	animate.setFillAfter(true);
	            	findViewById(R.id.splashback).startAnimation(animate);
	            	findViewById(R.id.splashback).setVisibility(View.GONE);
	            	mHandler.postDelayed(new Runnable() {
	                    public void run() {
	                        findViewById(R.id.deals).setVisibility(View.VISIBLE);
	                    }
	                }, 400);            	
            	}
            }
        });
        mWebView.loadUrl(url);
	}
	
    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }
}
