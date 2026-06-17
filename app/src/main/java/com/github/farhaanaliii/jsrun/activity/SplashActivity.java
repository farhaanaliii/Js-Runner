package com.github.farhaanaliii.jsrun.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.widget.ImageView;

import com.github.farhaanaliii.jsrun.Handler.Constants;
import com.github.farhaanaliii.jsrun.R;
import android.os.Handler;
import com.github.farhaanaliii.jsrun.Handler.CopyAssets;
import com.github.farhaanaliii.jsrun.Utils.Utils;
import android.content.Intent;
import java.io.File;
import android.view.WindowManager;
import android.view.View;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    LinearLayout layout;
    public boolean fileExtracted;
    CopyAssets copy_assets = new CopyAssets();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(getColor(R.color.colorPrimary));

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.icon2);

        layout.addView(imageView);
        
        setContentView(layout);
        fileExtracted = Utils.getBoolean(this, Constants.DATA_EXTRACTED, false);
        if(!fileExtracted) {
            new Thread(() -> {
                copy_assets.setCtx(getApplicationContext());
                copy_assets.setFromPathDirectoryAssets("data");
                copy_assets.setToDirectoryTarget(getFilesDir().getPath());
                copy_assets.start();
                
                Utils.putBoolean(this, Constants.DATA_EXTRACTED, true);
            }).start();
        }
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }, 3000);

    }
}
