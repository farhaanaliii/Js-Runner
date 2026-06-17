package com.github.farhaanaliii.jsrun.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.File;
import java.util.Objects;

import android.text.TextWatcher;
import android.text.Editable;
import android.content.Intent;
import com.github.farhaanaliii.jsrun.Handler.Constants;
import com.github.farhaanaliii.jsrun.Utils.Utils;
import androidx.appcompat.app.AppCompatActivity;
import com.github.farhaanaliii.jsrun.R;
import com.github.farhaanaliii.jsrun.views.CodeView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

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

        initCodeView();

        String code_text = Utils.readFile(new File(getFilesDir(), Constants.CODE_JS));
        code.setText(code_text);
        code.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                    try {
                        MenuItem save_menu = menu.findItem(R.id.save);
                        save_menu.setEnabled(true);
                        Objects.requireNonNull(save_menu.getIcon()).setAlpha(255);

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void afterTextChanged(Editable p1) {}
            });
    }
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        MenuItem item = this.menu.findItem(R.id.save);
        item.setEnabled(false);
        Objects.requireNonNull(item.getIcon()).setAlpha(130);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.save){
            try {
                item.setEnabled(false);
                Objects.requireNonNull(item.getIcon()).setAlpha(130);
                File f = new File(getFilesDir(), Constants.CODE_JS);
                Utils.writeFile(code.getText().toString(), f.getAbsolutePath());
                makeToast("Saved!");
            } catch (Exception e) {
                makeToast(e.toString());
            }
		} else if (id == R.id.run) {
            try {
                String js_code = code.getText().toString();
                File code_js = new File(getFilesDir(), Constants.CODE_JS);
                File template_file = new File(getFilesDir(), Constants.WEBVIEW_TEMPLATE);
                File preview_file = new File(getFilesDir(), Constants.WEBVIEW_MAIN_FILE);

                Utils.writeFile(js_code, code_js.getAbsolutePath());
                
                String html_template = Utils.readFile(template_file);
                if (!html_template.isEmpty()) {
                    String html = html_template.replace("__USER_SCRIPT__", js_code);
                    Utils.writeFile(html, preview_file.getAbsolutePath());
                }

            } catch (Exception e) {
                makeToast(e.toString());
            }

            startActivity(new Intent(MainActivity.this, ConsoleActivity.class));
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
		Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
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
