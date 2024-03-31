package com.github.farhanaliofficial.jsrun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import android.content.pm.PackageManager;
import android.os.Build;
import java.io.FileWriter;
import android.text.TextWatcher;
import android.text.Editable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import java.io.InputStreamReader;
import android.util.Log;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.util.Base64;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.nio.charset.StandardCharsets;
import com.github.farhanaliofficial.jsrun.Handler.Constants;
import com.github.farhanaliofficial.jsrun.Handler.CopyAssets;
import com.github.farhanaliofficial.jsrun.Utils.Utils;
import android.support.v7.app.AppCompatActivity;
import com.github.farhanaliofficial.jsrun.R;
import com.github.farhanaliofficial.jsrun.views.CodeView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    CodeView code;
    Menu menu;

    private static float textSize = 14.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		//code = findViewById(R.id.code);
        initCodeView();
        init();

        String ss = Utils.readFile(new File(getFilesDir(), Constants.TMP_JS));
        code.setText(ss);
        code.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                    try {
                        //File f = new File(getFilesDir(), Constants.TMP_JS);
                        //if(f.createNewFile()){}
                        MenuItem ii = menu.findItem(R.id.save);
                        ii.setEnabled(true);
                        ii.getIcon().setAlpha(255);

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void afterTextChanged(Editable p1) {}
            });
    }
	@Override
    public boolean onCreateOptionsMenu(Menu menuu) {
        getMenuInflater().inflate(R.menu.main_menu, menuu);
        menu = menuu;
        MenuItem item = menu.findItem(R.id.save);
        item.setEnabled(false);
        item.getIcon().setAlpha(130);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.save){
            try {
                item.setEnabled(false);
                item.getIcon().setAlpha(130);
                File f = new File(getFilesDir(), Constants.TMP_JS);
                f.createNewFile();
                Utils.saveFile(f.getAbsolutePath(), code.getText().toString(), this);
                makeToast("Saved!");
            } catch (Exception e) {
                makeToast(e.toString());
            }
		} else if (id == R.id.run) {
            try {
                File f = new File(getFilesDir(), Constants.TMP_JS);
                f.createNewFile();
                Utils.saveFile(f.getAbsolutePath(), code.getText().toString(), this);
            } catch (Exception e) {}

            startActivity(new Intent(MainActivity.this, RunActivity.class));
        }
        else if(id == R.id.theme){
            if(code.currentTheme == 1){
                code.setTheme(0);
            }else{
                code.setTheme(1);
            }
            code.refresh();
        }
        else if(id == R.id.line){
            if(code.isLineNumbersOn){
                code.setLineNumbersVisibility(false);
            }
            else{
                code.setLineNumbersVisibility(true);
            }
            code.refresh();
        }
        else if(id == R.id.font){
            if(code.isMonospace == 1){
                code.setFont(0);
                code.refresh();
            }
            else{
                code.setFont(1);
                code.refresh();
            }
        }
		return true;
	}
	public void makeToast(String text) {
		Toast.makeText(MainActivity.this, text, 0).show();
	}
    private void init() {
        String sb = Utils.readFile(new File(getFilesDir(), "js/" + Constants.ERUDA_JS));
        try {
            File ff = new File(getFilesDir(), Constants.ERUDA_JS);
            if (ff.createNewFile()) {
                return;
            }
            FileWriter fw = new FileWriter(ff.getAbsolutePath());
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            code.setText(e.toString());
            makeToast(e.toString());
        }
    }
    public static float getTextSize() {
        return textSize;
    }
    public void initCodeView() {
        code = findViewById(R.id.code);
        code.setTheme(0);
        code.setFont(1);
        code.setLineNumbersVisibility(true);
        code.setPairedAutoCloseOn(true);
        code.setTextSize(0, (textSize * getResources().getDisplayMetrics().density) + 0.5f);
        code.setTabWidth(4);
        code.setVerticalScrollBarEnabled(true);
        code.setHorizontalScrollBarEnabled(true);
        code.refresh();
        code.setHorizontallyScrolling(true);
    }
    public void setContentModified(boolean z) {
        String n = "n";
    }
}
