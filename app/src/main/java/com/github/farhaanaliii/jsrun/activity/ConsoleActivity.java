package com.github.farhaanaliii.jsrun.activity;

import android.os.Bundle;
import java.io.File;
import java.util.Objects;

import android.net.Uri;
import com.github.farhaanaliii.jsrun.Handler.Constants;
import com.github.farhaanaliii.jsrun.R;
import com.github.farhaanaliii.jsrun.Utils.Utils;
import com.github.farhaanaliii.jsrun.views.ConsoleWebView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ConsoleActivity extends AppCompatActivity {
    ConsoleWebView web;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        Toolbar toolbar=findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        web = findViewById(R.id.webView);
        web.init(ConsoleActivity.this);
        File file = new File(getFilesDir(), Constants.WEBVIEW_MAIN_FILE);
        web.loadUrl(Uri.fromFile(file).toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        if (id == R.id.refresh) {
            web.reload();
        } else if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
