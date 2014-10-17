package opprox.opprox;

import android.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DealActivity extends ActionBarActivity {

	private String url;
	private WebView dealWebView;
	private boolean failed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal);

		//Get bundles
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			url = extras.getString("url");
		}
		setTitle("Opprox");
        dealWebView = (WebView) findViewById(R.id.dealView);
        WebSettings webSettings = dealWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        dealWebView.setWebViewClient(new MyAppWebViewClient(){
            // Load URL
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	if (url.substring(0,21).equals("http://maps.google.ca")) {
            		view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            		return false;
            	} else
            		return true;
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            	failed = true;
            }
            
            // When page is finished loading
            public void onPageFinished(WebView view, String url) {
            	if (failed)
            		finish();
            }
        });
        dealWebView.loadUrl(url);
	}

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        finish();
    }

}
