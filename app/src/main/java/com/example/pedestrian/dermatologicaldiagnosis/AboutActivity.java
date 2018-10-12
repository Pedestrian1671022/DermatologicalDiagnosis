package com.example.pedestrian.dermatologicaldiagnosis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutactivity);
        WebView wView = (WebView)findViewById(R.id.webview);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.addJavascriptInterface(AboutActivity.this,"android");
        wView.loadUrl("file:///android_asset/about.html");
    }

    @JavascriptInterface
    public void startFunction(String str){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(str));
        startActivity(intent);
    }
}
