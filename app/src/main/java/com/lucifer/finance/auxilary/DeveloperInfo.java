package com.lucifer.finance.auxilary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.lucifer.finance.R;

public class DeveloperInfo extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView contactDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devcontact);

        // Set up the status bar color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));

        // Set up the toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Developer Info");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the back button in toolbar

        // Set up the WebView
        contactDev = findViewById(R.id.webView);
        contactDev.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = contactDev.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript

        contactDev.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("mailto:")) {
                    // Handle mailto links
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    // Handle other URLs
                    view.loadUrl(url);
                    return true;
                }
            }
        });

        // Load the local HTML file
        contactDev.loadUrl("file:///android_asset/FinanceDeveloper.html");
    }

    @Override
    public void onBackPressed() {
        if (contactDev != null && contactDev.canGoBack()) {
            contactDev.goBack(); // Navigate back in WebView history
        } else {
            super.onBackPressed(); // If no history, exit activity
        }
    }
}
