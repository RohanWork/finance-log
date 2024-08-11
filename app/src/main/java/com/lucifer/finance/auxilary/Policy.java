package com.lucifer.finance.auxilary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.lucifer.finance.R;

public class Policy extends AppCompatActivity {

    WebView policy;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Privacy Policy");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        setSupportActionBar(toolbar);

//        policy = new WebView(getApplicationContext());
//        policy.getSettings().setJavaScriptEnabled(true);
//        policy.getSettings().setBuiltInZoomControls(true);
//        policy.loadUrl("file:///android_asset/FinancePolicy.html");
//        setContentView(policy);

        WebView policy = findViewById(R.id.policy);
        policy.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        policy.loadUrl("file:///android_asset/FinancePolicy.html");
    }

    @Override
    public void onBackPressed() {
        if (policy != null && policy.canGoBack()) {
            policy.goBack(); // Navigate back in WebView history
        } else {
            super.onBackPressed(); // If no history, exit activity
        }
    }
}