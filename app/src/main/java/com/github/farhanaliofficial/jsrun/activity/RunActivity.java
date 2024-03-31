package com.github.farhanaliofficial.jsrun.activity;

import android.app.Activity;
import android.os.PersistableBundle;
import android.os.Bundle;
import java.io.File;
import com.github.farhanaliofficial.jsrun.Handler.Constants;
import com.github.farhanaliofficial.jsrun.R;
import com.github.farhanaliofficial.jsrun.views.CompilerWebView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class RunActivity extends AppCompatActivity {
    CompilerWebView web;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        Toolbar toolbar=findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
        web = findViewById(R.id.webView);
        web.init(RunActivity.this);
        File file = new File(getFilesDir(),Constants.WEBVIEW_MAIN_FILE);
        web.loadUrl(file.getAbsolutePath());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        switch(id){
            case R.id.refresh:
                web.reload();
                break;
        }
        return true;
    }
}
